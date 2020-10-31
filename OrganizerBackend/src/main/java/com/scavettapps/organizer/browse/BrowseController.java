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
package com.scavettapps.organizer.browse;

import com.scavettapps.organizer.core.OrganizerRestController;
import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.ErrorResponse;
import com.scavettapps.organizer.core.response.Response;
import java.io.IOException;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Vincent Scavetta
 */
@OrganizerRestController
@RequestMapping("/browse")
public class BrowseController {
   
   private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BrowseController.class);
   
   private final BrowseService browseService;

   @Autowired
   public BrowseController(BrowseService browseService) {
      this.browseService = browseService;
   }
   
   @GetMapping("/root")
   public ResponseEntity<Response> getDirectories() {
      return ResponseEntity.ok(new DataResponse(this.browseService.getDirectories()));
   }
   
   @PostMapping
   public ResponseEntity<Response> getDirectories(@RequestBody BrowseRequest body) {
      if (body.getPath() == null || body.getPath().isBlank()) {
         return ResponseEntity.badRequest().body(new ErrorResponse("Path must be provided"));
      }
      
      try {
         List<String> directories = this.browseService.getDirectories(
             body.getPath(), 
             body.isShowHidden()
         );
         return ResponseEntity.ok(new DataResponse(directories));
      } catch (IOException ex) {
         return ResponseEntity
             .badRequest()
             .body(new ErrorResponse("Path must exist and be a valid directory"));
      }
   }

}
