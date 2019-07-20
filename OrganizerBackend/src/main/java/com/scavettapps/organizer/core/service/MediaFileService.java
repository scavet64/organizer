/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.core.service;

import com.scavettapps.organizer.core.EntityNotFoundException;
import com.scavettapps.organizer.core.entity.MediaFile;
import com.scavettapps.organizer.core.repository.FileRepository;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

/**
 *
 * @author vstro
 */
@Service
public class MediaFileService {
   
   @Autowired
   private FileRepository fileRepository;
   
   public MediaFile getMediaFile(String hash) { 
      return fileRepository.findByHash(hash).orElseThrow(() -> new EntityNotFoundException());
   }

   /**
    * finds the file on the file system and loads it into a resource
    *
    * @param hash the hash of the file
    * @return resource that is the file
    * @throws FileNotFoundException if the file is not found
    * @throws EntityNotFoundException if the hash is not found
    */
   public Resource loadFileAsResource(String hash) throws FileNotFoundException {
      
      MediaFile file = getMediaFile(hash);
      
      try {
         Resource resource = new UrlResource(new File(file.getPath()).toURI());
         if (resource.exists()) {
            return resource;
         } else {
            throw new FileNotFoundException("File not found: " + file);
         }
      } catch (MalformedURLException ex) {
         throw new FileNotFoundException("Malformed URL Exception: " + file);
      }
   }
}
