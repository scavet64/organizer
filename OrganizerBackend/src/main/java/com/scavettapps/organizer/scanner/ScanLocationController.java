//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.scanner;

import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.ErrorResponse;
import com.scavettapps.organizer.core.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Gnostech Inc.
 */
@RestController("/scan")
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
   
   @PostMapping
   public ResponseEntity<Response> addScanLocation(AddScanLocationRequest request) {
      
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
