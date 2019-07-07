package com.scavettapps.organizer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scavettapps.organizer.controller.response.DataResponse;
import com.scavettapps.organizer.controller.response.ErrorResponse;
import com.scavettapps.organizer.controller.response.Response;
import com.scavettapps.organizer.core.entity.Folder;
import com.scavettapps.organizer.core.repository.FolderRepository;
import java.util.HashMap;
import java.util.Map;

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

}
