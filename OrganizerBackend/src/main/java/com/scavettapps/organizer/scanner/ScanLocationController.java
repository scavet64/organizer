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
package com.scavettapps.organizer.scanner;

import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.ErrorResponse;
import com.scavettapps.organizer.core.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Vincent Scavetta
 */
@RestController()
@RequestMapping("/scan")
public class ScanLocationController {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(ScanLocationController.class);
   
   private final ScanLocationSevice scanLocationSevice;

   @Autowired
   public ScanLocationController(ScanLocationSevice scanLocationSevice) {
      this.scanLocationSevice = scanLocationSevice;
   }
   
   @GetMapping
   public ResponseEntity<Response> getScanLocations() {
      return ResponseEntity.ok(new DataResponse(scanLocationSevice.findAllScanLocations()));
   }
   
   @DeleteMapping
   public ResponseEntity<Response> removeScanLocation(@RequestParam long id) {
      if (this.scanLocationSevice.deleteScanLocation(id)) {
         return ResponseEntity.ok(new DataResponse("Scan Location was successfully deleted"));
      } else {
         return ResponseEntity.badRequest().body(new ErrorResponse("Cannot delete something that does not exist"));
      }
      
   }
   
   @PostMapping("/create")
   public ResponseEntity<Response> addScanLocation(@RequestBody AddScanLocationRequest request) {
      
      if (request.getPath() == null || request.getPath().isBlank()) {
         return ResponseEntity.badRequest().body(new ErrorResponse("Path must be non blank"));
      }
      
      try {
         ScanLocation newLoc = this.scanLocationSevice.createNewScanLocation(request.getPath());
         return ResponseEntity.ok(new DataResponse(newLoc));
      } catch (IllegalScanningLocationException ex) {
         LOGGER.warn("Invalid Scanning path", ex);
         return ResponseEntity.badRequest().body(new ErrorResponse(ex.getLocalizedMessage()));
      }
   }
   
}
