//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.scanner;

import java.io.File;
import java.util.Collection;
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

   public ScanLocation createNewScanLocation(String path) throws IllegalScanningLocationException {
      throwIfInvalid(path);
      return this.scanLocationRepository.save(new ScanLocation(path));
   }

   public Collection<ScanLocation> findAllScanLocations() {
      return this.scanLocationRepository.findAll();
   }

   public void throwIfInvalid(String pathToTest) throws IllegalScanningLocationException {
      if (pathToTest != null && !pathToTest.isBlank()) {
         File file = new File(pathToTest);
         if(!file.exists()) {
            throw new IllegalScanningLocationException("Scanning path does not exist");
         } else if (!file.isDirectory()) {
            throw new IllegalScanningLocationException("Scanning path is not a valid directory");
         }
      }
      throw new IllegalScanningLocationException("Scanning path was not supplied");
   }

}
