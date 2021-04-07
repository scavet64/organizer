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
package com.scavettapps.organizer.scanner.engine;

import com.scavettapps.organizer.media.DuplicateMediaFilePath;
import com.scavettapps.organizer.files.StoredFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.transaction.Transactional;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.scavettapps.organizer.scanner.ScanLocation;
import com.scavettapps.organizer.scanner.ScanLocationSevice;
import com.scavettapps.organizer.transcoding.ITranscodingService;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Vincent Scavetta
 */
@Service
@Slf4j
public class FileScanningService {

   //TODO: Look into increasing this. I am seeing odd errors with this multi threaded
   private static final int NUMBER_THREADS = 1;

   private final FolderService folderService;
   private final IHashService quickHash;
   private final ITranscodingService transcodingService;
   private final MediaFileService mediaFileService;
   private final ScanLocationSevice scanLocationService;

   @Autowired
   public FileScanningService(
       FolderService folderService,
       @Qualifier("QuickHash") IHashService quickHash,
       @Qualifier("bramp") ITranscodingService transcodingService,
       MediaFileService mediaFileService,
       ScanLocationSevice scanLocationService
   ) {
      this.folderService = folderService;
      this.quickHash = quickHash;
      this.transcodingService = transcodingService;
      this.mediaFileService = mediaFileService;
      this.scanLocationService = scanLocationService;
   }

   @Transactional
   @Async
   public void initiateScanning(long id) {
      ScanLocation location = this.scanLocationService.getScanLocation(id);
      try {
         scanLocationForFiles(location.getPath());
         location.setLastScan(Instant.now());
         this.scanLocationService.updateScanLocation(location);
      } catch (InterruptedException | ExecutionException ex) {
         log.error("Failed to scan: " + location.getPath());
      }

   }

   @Transactional
   public Folder scanLocationForFiles(String path) throws InterruptedException, ExecutionException {
      log.info("Initializing Scanning of: " + path);

      Set<MediaFile> addedFiles = Collections.synchronizedSet(new HashSet<>());

      // Create the initial folder based on the passed in path. IF it already exists. get it.
      File scanningPathFile = new File(path);
      Folder workingFolder = folderService.findFolderByPath(scanningPathFile.getPath());
      if (workingFolder == null) {
         // new folder
         workingFolder = new Folder(scanningPathFile.getPath());
      }

      //Get the list of files to work with
      List<File> filesInLocation = Arrays.asList(scanningPathFile.listFiles());

      // Only scan if there are files
      if (!filesInLocation.isEmpty()) {

         // Partition the list into equal parts for splitting into threads
         List<List<File>> parts = ListUtils.partition(
             filesInLocation,
             filesInLocation.size() / NUMBER_THREADS
         );

         for (List<File> partitionedList : parts) {
            // Scan on this thread as there was an issue with transactions if this was ran on a different thread.
            // Since this is already async and the operation is more disk intensive, more threads wouldn't make it run much faster
            // TODO: Look into finding a way to get around the transactional error
            new ScanThreadTask(partitionedList, workingFolder, addedFiles).run();
         }
      }

      log.info("Finished Scanning: " + path);
      try {
         workingFolder = this.folderService.saveFolder(workingFolder);
         return workingFolder;
      } catch (Exception ex) {
         return workingFolder;
      }
   }

   /**
    * Process a single file.
    * @param file The file to process
    * @return The processed media file
    * @throws AlreadyExistsException
    * @throws IOException
    * @throws IllegalMimeTypeException
    */
   private MediaFile processFile(File file) throws AlreadyExistsException, IOException, IllegalMimeTypeException, MediaMovedException {
      // Check to see if we know this file using name and size
      MediaFile existingFile = this.mediaFileService.getMediaFile(file.getName(), file.length()).orElse(null);
      if (existingFile == null) {
         // Could not find the file in the database without hashing. Get hash and check using that.
         String hash = quickHash.getHash(file);
         existingFile = this.mediaFileService.getMediaFile(hash).orElse(null);
         if (existingFile == null) {
            // Hash not found in database. Create the new media file object
            String mimetype = getMimeType(file);

            // Only add media files, ignore anything thats not a video, image, or audio
            if (!isAllowedMimeType(mimetype)) {
               throw new IllegalMimeTypeException("Illegal mimetype: " + mimetype);
            }
            
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

            MediaFile newFile = new MediaFile(
                hash,
                file.getName(),
                file.length(),
                file.getPath(),
                mimetype,
                attr.creationTime().toInstant(),
                attr.lastModifiedTime().toInstant()
            );
            if (mimetype != null && mimetype.contains("video")) {
               // Video Detected. Grab the thumbnail.
               newFile.setThumbnail(getVideoThumb(newFile));
            }
            log.info(
                "new file added - hash: " + newFile.getHash() + " name: " + newFile.getName() + " mimetype: " + mimetype
            );
            return newFile;
         } else {
            log.error("file hash already existed file added - hash: " + hash + " name: " + file.getName());
            //existingFile.addDuplicatePath(new DuplicateMediaFilePath(file.getAbsolutePath()));
            throw new AlreadyExistsException("file already existed");
         }
      } else {
         // This file was found via its name and length

         // Check the path to see if it was a duplicate file or if it has moved.
         var existingFilePath = existingFile.getPath();
         if (!existingFilePath.equals(file.getPath())) {
            // Path does not equal what is in the database
            if (new File(existingFilePath).exists()) {
               // The recorded file in the database still exists on the file system so this new file must be a duplicate
               existingFile.addDuplicatePath(new DuplicateMediaFilePath(file.getAbsolutePath()));
            } else {
               // This file has moved, throw the exception to signal to the caller to update its folder.
               throw new MediaMovedException(existingFile);
            }
         }

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
      log.info("Now Entering: " + currentFolder.getPath());
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
            handleFile(currentFolder, scannedFiles, file);
         }
      }
      // End of a directory search. Save the folder
   }

   private void handleFile(Folder currentFolder, Set<MediaFile> scannedFiles, File file) throws IOException {
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
            log.warn("duplicate hash found during this scanning session!");
            previousFile.addDuplicatePath(
                new DuplicateMediaFilePath(processedFile.getPath())
            );
         } else {
            // Make sure the thumbnail is unique too if it has one.
            if (processedFile.getThumbnail() != null) {
               previousFile = scannedFiles
                   .stream()
                   .filter(mediaFile -> (mediaFile.getThumbnail() != null && mediaFile.getThumbnail().getHash().equalsIgnoreCase(processedFile.getThumbnail().getHash())))
                   .collect(Collectors.toList())
                   .stream()
                   .findFirst()
                   .orElse(null);
               if (previousFile != null) {
                  // Thumbnail file is not unique. Use the previously found's thumbnail
                  log.info("Found a duplicate thumbnail with hash [{}]", processedFile.getThumbnail().getHash());
                  File duplicateThumb = new File(processedFile.getThumbnail().getPath());
                  duplicateThumb.delete();

                  processedFile.setThumbnail(previousFile.getThumbnail());
               }
            }

            currentFolder.addFile(processedFile);
            scannedFiles.add(processedFile);
         }
      } catch (MediaMovedException ex) {
         log.info("detected that file {} was moved to {}", file.getName(), currentFolder.getPath());
         var movedMedia = ex.getMovedMedia();

         this.folderService.MoveMedia(movedMedia, currentFolder);

      } catch (AlreadyExistsException ex) {
         //log.info("file already existed and is recorded. Skipping");
      } catch (IllegalMimeTypeException ex) {
         log.info("file's mimetype was illegal: {}", ex.getMessage());
      }
   }

   private StoredFile getVideoThumb(MediaFile newFile) {
      log.info("Generating thumbnail for: " + newFile.getName());
      try {
         File thumbnailFile = transcodingService.getDefaultThumbnail(newFile);
         String thumbhash = this.quickHash.getHash(thumbnailFile);

         return new StoredFile(
             thumbhash,
             thumbnailFile.getAbsolutePath(),
             thumbnailFile.getName(),
             thumbnailFile.length()
         );
      } catch (IOException ex) {
         log.error("Could not generate thumbnail for video: " + newFile.getName());
         return null;
      }
   }

   private String getMimeType(File file) throws IOException {
      String mimetype = URLConnection.guessContentTypeFromName(file.getName());
      if (mimetype == null) {
         log.warn("Could not guess mimetype using name for file: {}", file.getName());
         try (FileInputStream fis = new FileInputStream(file)) {
            mimetype = URLConnection.guessContentTypeFromStream(fis);
         }
         if (mimetype == null) {
            log.warn("Could not guess mimetype using stream for file: {}", file.getName());
            Tika tika = new Tika();
            mimetype = tika.detect(file);
            if (mimetype == null) {
               log.error("Could not determine mimetype for file: {}", file.getName());
            }
         }
      }
      return mimetype;
   }

   private boolean isAllowedMimeType(String mimetype) {
      if (mimetype == null) {
         return false;
      } else {
         String lowered = mimetype.toLowerCase();
         return lowered.contains("video") || lowered.contains("image") || lowered.contains("audio");
      }
   }

   class ScanThreadTask implements Runnable {

      private final List<File> partitionedList;
      private final Folder workingFolder;
      private final Set<MediaFile> addedFiles;

      public ScanThreadTask(List<File> partitionedList, Folder workingFolder, Set<MediaFile> addedFiles) {
         this.partitionedList = partitionedList;
         this.workingFolder = workingFolder;
         this.addedFiles = addedFiles;
      }

      @Override
      public void run() {
         try {
            recurseDirectory(
                partitionedList.toArray(new File[partitionedList.size()]),
                workingFolder,
                addedFiles);
         } catch (IOException ex) {
            log.error("Failed recursing directory", ex);
         }
      }
   }
}
