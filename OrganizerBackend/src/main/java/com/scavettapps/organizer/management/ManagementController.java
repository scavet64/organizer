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
package com.scavettapps.organizer.management;

import com.scavettapps.organizer.core.OrganizerRestController;
import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.ErrorResponse;
import com.scavettapps.organizer.core.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Vincent Scavetta.
 */
@OrganizerRestController
@RequestMapping("/management")
public class ManagementController {
   
   private ManagementService managementService;

   @Autowired
   public ManagementController(ManagementService managementService) {
      this.managementService = managementService;
   }

   @GetMapping
   public ResponseEntity<Response> clearAllMedia() {
      try {
         this.managementService.clearAllScannedMedia();
         return ResponseEntity.ok(new DataResponse("Successfully cleared scanned media"));
      } catch (ManagementException ex) {
         return ResponseEntity
             .status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(new ErrorResponse(ex.getMessage()));
      }
      
   }
   
}
