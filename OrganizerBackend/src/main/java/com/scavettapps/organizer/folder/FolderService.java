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
package com.scavettapps.organizer.folder;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scavettapps.organizer.core.EntityNotFoundException;
import com.scavettapps.organizer.media.MediaFile;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Vincent Scavetta
 */
@Service
@Slf4j
public class FolderService {
   
   private FolderRepository folderRepository;

   @Autowired
   public FolderService(FolderRepository folderRepository) {
      this.folderRepository = folderRepository;
   }
   
   public Folder saveFolder(Folder folder) {
      return this.folderRepository.save(folder);
   }
   
   public Folder findFolderByPath(String path) {
      return this.folderRepository.findByPath(path).orElse(null);
   }
   
//   /**
//    * This method will return a folder with the specified path regardless if its in the database
//    * @param path
//    * @return 
//    */
//   public Folder getFolderByPath(String path) {
//      return this.folderRepository.findByPath(path).orElse(new Folder(path));
//   }

   public Folder findFolderContainingFile(MediaFile file) {
      return this.folderRepository.findByFilesContaining(file).orElseThrow(EntityNotFoundException::new);
   }

   @Transactional(Transactional.TxType.REQUIRES_NEW)
   public boolean MoveMedia(MediaFile file, Folder newFolder) {
      var currentFolderOpt = this.folderRepository.findByFilesContaining(file);
      if (currentFolderOpt.isPresent()) {
         var currentFolder = currentFolderOpt.get();
         if (currentFolder.getFiles().remove(file)) {
            log.info("Successfully remove media [{}] from previous folder [{}]", file.getName(), currentFolder.getPath());
            newFolder.addFile(file);
            this.folderRepository.save(currentFolder);
            log.info("Successfully moved media [{}] to [{}]", file.getName(), newFolder.getPath());
            return true;
         } else {
            log.error("Could not remove media [{}] from previous folder [{}]", file.getName(), currentFolder.getPath());
         }
      } else {
         log.error("Could not find existing media with id [{}] with in any folders", file.getName());
      }
      return false;
   }
   
}
