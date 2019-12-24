//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gnostech Inc.
 */
@Service
public class ScanLocationSevice {
   
   private final ScanLocationRepository scanLocationRepository;

   @Autowired
   public ScanLocationSevice(ScanLocationRepository scanLocationRepository) {
      this.scanLocationRepository = scanLocationRepository;
   }
   
   public ScanLocation createNewScanLocation(String path) {
      return this.scanLocationRepository.save(new ScanLocation(path));
   }
   
}
