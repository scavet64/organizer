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
package com.scavettapps.organizer.tag;

import com.scavettapps.organizer.core.EntityAlreadyExistsException;
import com.scavettapps.organizer.core.EntityNotFoundException;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vincent Scavetta
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
   
   @Transactional
   public boolean deleteTagById(long id) {
      int deleted = tagRepository.deleteById(id);
      return deleted > 0;
   }
   
}
