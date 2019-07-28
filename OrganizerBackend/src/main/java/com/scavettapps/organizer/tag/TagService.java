/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.tag;

import com.scavettapps.organizer.core.EntityAlreadyExistsException;
import com.scavettapps.organizer.core.EntityNotFoundException;
import java.util.Collection;
import javax.transaction.Transactional;
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
   
   @Transactional
   public Tag editTag(Tag editedTag) {
      
      //check if tag exists
      tagRepository.findById(editedTag.getId()).ifPresent((tag) -> {
         // Tag with this ID exists
         this.tagRepository.findByName(editedTag.getName()).ifPresent((tagWithName) -> {
            // Tag with this name exists. Check to make sure that the ID's match.
            // If not then there will be a conflict with two different tags having the same name
            if(!tagWithName.getId().equals(tag.getId())) {
               throw new EntityAlreadyExistsException(
                   "Tag already exists with name: " + tagWithName.getName()
               );
            }
         });
      });
      
      return tagRepository.save(editedTag);
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
