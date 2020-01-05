/**
 * Copyright 2019 Vincent Scavetta
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scavettapps.organizer.scanner;

import com.scavettapps.organizer.core.entity.DuplicateMediaFilePath;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.transaction.Transactional;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.scavettapps.organizer.folder.Folder;
import com.scavettapps.organizer.media.MediaFile;
import com.scavettapps.organizer.hashing.IHashService;
import com.scavettapps.organizer.folder.FolderService;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.stream.Collectors;
import com.scavettapps.organizer.media.MediaFileService;

/**
 * @author Vincent Scavetta
 */
@Service
public class FileScanningService {

   private static final Logger LOGGER = LoggerFactory.getLogger(FileScanningService.class);
   
   //TODO: Look into increasing this. I am seeing odd errors with this multi threaded
   private static final int NUMBER_THREADS = 1;
   
   private static Folder workingFolder;

   private final FolderService folderService;
   private final IHashService quickHash;
   private final MediaFileService mediaFileService;
   private final ScanLocationSevice scanLocationService;

   public FileScanningService(
       FolderService folderService, 
       @Qualifier("QuickHash") IHashService quickHash, 
       MediaFileService mediaFileService, 
       ScanLocationSevice scanLocationService) {
      this.folderService = folderService;
      this.quickHash = quickHash;
      this.mediaFileService = mediaFileService;
      this.scanLocationService = scanLocationService;
   }

   @Transactional
   public Folder scanLocationForFiles(String path) throws InterruptedException,
       ExecutionException {

      Set<MediaFile> addedFiles = Collections.synchronizedSet(new HashSet<>());

      // Create the initial folder based on the passed in path. IF it already exists. get it.
      File scanningPathFile = new File(path);
      workingFolder = folderService.findFolderByPath(scanningPathFile.getPath());
      if (workingFolder == null) {
         // new folder
         workingFolder = new Folder(scanningPathFile.getPath());
      }

      //Get the list of files to work with
      List<File> filesInLocation = Arrays.asList(scanningPathFile.listFiles());

      // Partition the list into equal parts for splitting into threads
      List<List<File>> parts = ListUtils
          .partition(filesInLocation, filesInLocation.size() / NUMBER_THREADS);

      List<Thread> threads = new ArrayList<>();
      for (List<File> partitionedList : parts) {
         Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
               try {
                  recurseDirectory(
                      partitionedList.toArray(new File[partitionedList.size()]),
                      workingFolder,
                      addedFiles);
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         });
         t.start();
         threads.add(t);
      }

      LOGGER.info("Kicked off threads");

      for (Thread treadToJoin : threads) {
         treadToJoin.join();
      }

      LOGGER.info("Joined off threads");

      this.folderService.saveFolder(workingFolder);

      return workingFolder;
   }

   @Transactional
   private MediaFile processFile(File file) throws AlreadyExistsException, IOException {

      // Check to see if we know this file using name and size
      MediaFile existingFile = this.mediaFileService.getMediaFile(file.getName(), file.length()).orElse(null);
      if (existingFile == null) {

         // Could not find the file in the database without hashing. Get hash and check using that.
         String hash = quickHash.getHash(file);
         existingFile = this.mediaFileService.getMediaFile(hash).orElse(null);
         if (existingFile == null) {
            // Hash not found in database. Create the new media file object
            String mimetype = URLConnection.guessContentTypeFromName(file.getName());

            MediaFile newFile = new MediaFile(
                hash,
                file.getName(),
                file.length(),
                file.getPath(),
                mimetype
            );
            LOGGER.info(
                "new file added - hash: " + newFile.getHash() + " name: " + newFile.getName() + " mimetype: " + mimetype
            );
            return newFile;
         } else {
            LOGGER.error(
                "file hash already existed file added - hash: "
                + hash
                + " name: "
                + file.getName());
            throw new AlreadyExistsException("file already existed");
         }
      } else {
         String mimeType = URLConnection.guessContentTypeFromName(file.getName());
         LOGGER.info(
             "existing file based on name and size - hash: " + existingFile.getHash() + " name: " + existingFile.getName() + " mimetype: " + mimeType
         );
         existingFile.updateLastSeen();
         this.mediaFileService.saveMediaFile(existingFile);
         throw new AlreadyExistsException("file already existed");
      }
   }

   /**
    * Method recurses through a directory to find every file.
    *
    * @param files array of files to walk through
    * @param scannedFiles Set of scanned files to keep track of what was found this session
    * @param currentFolder the current folder that is being scanned
    */
   @Transactional
   public void recurseDirectory(File[] files, Folder currentFolder, Set<MediaFile> scannedFiles)
       throws IOException {
      LOGGER.info("Now Entering: " + currentFolder.getPath());
      for (File file : files) {
         if (file.isDirectory()) {
            // Do we have this file already?
            Folder scanFolder = folderService.findFolderByPath(file.getPath());
            if (scanFolder == null) {
               // new folder. Set it up for the first time
               scanFolder = new Folder(file.getPath());
            }
            scanFolder.setFolder(currentFolder); // Parent folder
            currentFolder.addFolder(scanFolder); // Add this folder to the current folder's list
            // go deeper...
            recurseDirectory(file.listFiles(), scanFolder, scannedFiles);
         } else {
            try {
               // Process the file
               MediaFile processedFile = processFile(file);

               //Does this file already exist somewhere else in this current scanning session?
               MediaFile previousFile = scannedFiles.stream()
                   .filter(mediaFile -> (mediaFile.getHash().equalsIgnoreCase(processedFile.getHash())))
                   .collect(Collectors.toList())
                   .stream()
                   .findFirst()
                   .orElse(null);

               // If this file was found, add to its duplicate file list
               if (previousFile != null) {
                  LOGGER.warn("duplicate hash found during this scanning session!");
                  previousFile.addDuplicatePath(
                      new DuplicateMediaFilePath(processedFile.getPath())
                  );
               } else {                  
                  currentFolder.addFile(processedFile);
                  scannedFiles.add(processedFile);
               }
            } catch (AlreadyExistsException ex) {
               LOGGER.info("file already existed and is recorded. Skipping");
            }
         }
      }
      // End of a directory search.
   }
}
