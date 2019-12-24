//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.folder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gnostech Inc.
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
