/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.tag;

import com.scavettapps.organizer.core.EntityAlreadyExistsException;
import com.scavettapps.organizer.core.EntityNotFoundException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author vstro
 */
@Service
public class TagService {
   
   @Autowired
   private TagRepository tagRepository;
   
   public Collection<Tag> getAllTags() {
      return tagRepository.findAll();
   }
   
   public Tag addNewTag(Tag newTag) {
      
      //check if tag already exists
      if (tagRepository.findByName(newTag.getName()).isPresent()) {
         throw new EntityAlreadyExistsException("Tag already exists with name: " + newTag.getName());
      }
      
      return tagRepository.save(newTag);
   }
   
   public Tag getTagByName(String name) {
      return tagRepository.findByName(name)
              .orElseThrow(() -> new EntityNotFoundException("Could not find Tag with name: " + name));
   }
   
   public void deleteAllTags() {
      tagRepository.deleteAll();
   }
   
   public boolean deleteTagById(long id) {
      int deleted = tagRepository.deleteById(id);
      return deleted > 0;
   }
   
}
