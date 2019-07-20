package com.scavettapps.organizer.scanner;

import com.scavettapps.organizer.core.entity.DuplicateMediaFilePath;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.transaction.Transactional;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.scavettapps.organizer.core.entity.Folder;
import com.scavettapps.organizer.core.entity.MediaFile;
import com.scavettapps.organizer.hashing.IHashService;
import com.scavettapps.organizer.core.repository.FileRepository;
import com.scavettapps.organizer.core.repository.FolderRepository;
import com.scavettapps.organizer.core.repository.ScanLocationRepository;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class FileScanningService {

   public static final int NUMBER_THREADS = 4;

   private Logger logger = LoggerFactory.getLogger(this.getClass());

   @Autowired
   private ScanLocationRepository scanLocationRepository;

   @Autowired
   private FileRepository fileRepository;

   @Autowired
   private FolderRepository folderRepository;

   @Autowired
   @Qualifier("QuickHash")
   private IHashService hashingService;

   @Transactional
   public Folder scanLocationForFiles(String path) throws InterruptedException,
           ExecutionException {

      Set<MediaFile> addedFiles = Collections.synchronizedSet(new HashSet<>());
      

      // Create the initial folder based on the passed in path. IF it already exists. get it.
      File scanningPathFile = new File(path);
      Folder initialFolder = folderRepository.findByPath(scanningPathFile.getPath()).orElse(null);
      if (initialFolder == null) {
         // new folder
         initialFolder = new Folder(scanningPathFile.getPath());
      }
      Folder tempFolder = initialFolder;

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
                          tempFolder,
                          addedFiles);
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         });
         t.start();
         threads.add(t);
      }

      logger.info("Kicked off threads");

      for (Thread treadToJoin : threads) {
         treadToJoin.join();
      }

      logger.info("Joined off threads");

      this.folderRepository.save(initialFolder);

      return initialFolder;
   }

   @Transactional
   private MediaFile processFile(File file) throws AlreadyExistsException, IOException {

      // Check to see if we know this file using name and size
      MediaFile existingFile = this.fileRepository.findByNameAndSize(file.getName(), file.length()).orElse(null);
      if (existingFile == null) {

         // Could not find the file in the database without hashing. Get hash and check using that.
         String hash = hashingService.getHash(file);
         existingFile = this.fileRepository.findByHash(hash).orElse(null);
         if (existingFile == null) {
            // Hash not found in database. Create the new media file object
            MediaFile newFile = new MediaFile(
                    hash,
                    file.getName(),
                    file.length(),
                    file.getPath()
            );
            logger.info(
                    "new file added - hash: " + newFile.getHash() + " name: " + newFile.getName()
            );
            return newFile;
         } else {
            logger.error(
                    "file hash already existed file added - hash: "
                    + hash
                    + " name: "
                    + file.getName());
            throw new AlreadyExistsException("file already existed");
         }
      } else {
         existingFile.updateLastSeen();
         this.fileRepository.save(existingFile);
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
      logger.info("Now Entering: " + currentFolder.getPath());
      for (File file : files) {
         if (file.isDirectory()) {
            // go deeper...
            Folder scanFolder = folderRepository.findByPath(file.getPath()).orElse(null);
            if (scanFolder == null) {
               // new folder
               scanFolder = new Folder(file.getPath());
            }
            scanFolder.setFolder(currentFolder);
            currentFolder.getFolders().add(scanFolder);
            recurseDirectory(file.listFiles(), scanFolder, scannedFiles);
         } else {
            try {
               // Process the file
               MediaFile processedFile = processFile(file);

               //Does this file already exist somewhere else in this current scanning session?
               MediaFile previousFile = scannedFiles.stream()
                       .filter(mediaFile -> (mediaFile.getHash().equalsIgnoreCase(processedFile.getHash())))
                       .collect(Collectors.toList()).stream().findFirst().orElse(null);

               // If this file was found, add to its duplicate file list
               if(previousFile != null) {
                  logger.info("duplicate hash found!");
                  previousFile.addDuplicatePath(
                          new DuplicateMediaFilePath(processedFile.getPath())
                  );
               } else {
                  currentFolder.addFile(processedFile);
                  scannedFiles.add(processedFile);
               }
            } catch (AlreadyExistsException ex) {
               logger.info("file already existed and is recorded. Skipping");
            }
         }
      }
   }
}
