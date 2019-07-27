/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.tag;

import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.ErrorResponse;
import com.scavettapps.organizer.core.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vstro
 */
@RestController
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
