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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.ErrorResponse;
import com.scavettapps.organizer.core.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Vincent Scavetta
 */
@RestController
public class FolderController {

   @Autowired
   FolderRepository folderRepo;

   @RequestMapping("/folder/files")
   public Response findFilesInFolder(@RequestParam String folderName) {

      Folder folder = this.folderRepo.findByPath(folderName).orElse(null);
      if (folder == null) {
         return new ErrorResponse("Folder not found");
      }

      return new DataResponse(folder);
   }
   
   @RequestMapping("/folder")
   public Response findAllFolders() {
      return new DataResponse(this.folderRepo.findAll());
   }
   
   @RequestMapping("/folder/root")
   public Response findAllRootFolders() {
      
      Map<String, Object> returnMap = new HashMap<>();
      returnMap.put("Folders", this.folderRepo.findAllByFolderNull());
      return new DataResponse(returnMap);
   }
   
   @RequestMapping("/folder/search")
   public Response findFilesInFolder(@RequestParam long folderId) {

      return new DataResponse(this.folderRepo.findAllByFolder_Id(folderId));
   }
   
   @GetMapping("/folder")
   public Response findFileById(@RequestParam long folderId) {

      return new DataResponse(this.folderRepo.findById(folderId));
   }

}
