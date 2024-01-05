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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scavettapps.organizer.core.OrganizerRestController;
import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.ErrorResponse;
import com.scavettapps.organizer.core.response.Response;

/**
 *
 * @author Vincent Scavetta
 */
@OrganizerRestController
@RequestMapping("tag")
public class TagController {

   @Autowired
   private TagService tagService;

   @GetMapping("/list")
   public ResponseEntity<Response> getAllTags() {
      return new ResponseEntity<>(
          new DataResponse(this.tagService.getAllTags()),
          HttpStatus.OK
      );
   }

   @PostMapping("/create")
   public ResponseEntity<Response> createNewTag(
       @RequestBody Tag newTag
   ) {
      newTag = tagService.addNewTag(newTag);

      return new ResponseEntity<>(
          new DataResponse(newTag),
          HttpStatus.OK
      );
   }
   
   @PutMapping("/edit")
   public ResponseEntity<Response> editTag(
       @RequestBody Tag newTag
   ) {
      newTag = tagService.editTag(newTag);

      return new ResponseEntity<>(
          new DataResponse(newTag),
          HttpStatus.OK
      );
   }

   @DeleteMapping("/all")
   public ResponseEntity<Response> deleteAllTags() {
      this.tagService.deleteAllTags();
      return new ResponseEntity<>(
          new DataResponse("Deleted all tags"),
          HttpStatus.OK
      );
   }

   @DeleteMapping()
   public ResponseEntity<Response> deleteTag(@RequestParam long id) {
      boolean deleted = this.tagService.deleteTagById(id);

      if (!deleted) {
         return new ResponseEntity<>(
             new ErrorResponse("Tag was not deleted."),
             HttpStatus.BAD_REQUEST
         );
      } else {
         return new ResponseEntity<>(
             new DataResponse("Deleted tag"),
             HttpStatus.OK
         );
      }

   }

}
