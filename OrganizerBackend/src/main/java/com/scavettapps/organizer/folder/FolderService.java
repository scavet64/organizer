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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vincent Scavetta
 */
@Service
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
   
}
