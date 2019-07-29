/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.media;

import com.scavettapps.organizer.core.EntityNotFoundException;
import com.scavettapps.organizer.media.MediaFile;
import com.scavettapps.organizer.media.FileRepository;
import com.scavettapps.organizer.tag.Tag;
import com.scavettapps.organizer.tag.TagRepository;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Collection;
import javax.transaction.Transactional;
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
   @Autowired
   private TagRepository tagRepository;
   
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
   
   public MediaFile addTagToMediaFile(long mediaId, long tagId) {
      if (mediaId < 0 || tagId < 0) {
         throw new IllegalArgumentException("Ids cannot be negative");
      }
      
      // Find the media file
      MediaFile file = fileRepository.findById(mediaId).orElseThrow();
      
      // Find the Tag and add it.
      Tag tag = tagRepository.findById(tagId).orElseThrow();
      file.addTag(tag);
      
      return fileRepository.save(file);
   }
   
   /**
    * TODO: This could be made a little more efficient so that it does not reach out to the database
    * so much, but since this is meant to be local/in memory for now, it is not as big of a deal.
    * @param mediaId
    * @param tags
    * @return 
    */
   @Transactional
   public MediaFile addTagToMediaFile(long mediaId, Collection<Long> tags) {
      if (mediaId < 0) {
         throw new IllegalArgumentException("Ids cannot be negative");
      }
      
      // Find the media file
      MediaFile file = fileRepository.findById(mediaId).orElseThrow();
      
      file.getTags().clear();
      
      for (Long tagId: tags) {
         // Find the Tag and add it to the file.
         Tag tag = tagRepository.findById(tagId).orElseThrow();
         file.addTag(tag);
      }
      
      return fileRepository.save(file);
   }
}
