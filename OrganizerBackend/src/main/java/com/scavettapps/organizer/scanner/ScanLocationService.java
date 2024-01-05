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

import java.io.File;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scavettapps.organizer.core.EntityNotFoundException;

/**
 *
 * @author Vincent Scavetta
 */
@Service
public class ScanLocationService {

   private static final Logger LOGGER = LoggerFactory.getLogger(ScanLocationService.class);
   private final ScanLocationRepository scanLocationRepository;

   @Autowired
   public ScanLocationService(ScanLocationRepository scanLocationRepository) {
      this.scanLocationRepository = scanLocationRepository;
   }

   public ScanLocation createNewScanLocation(String path) throws IllegalScanningLocationException {
      throwIfInvalid(path);
      return this.scanLocationRepository.save(new ScanLocation(path));
   }

   public ScanLocation updateScanLocation(ScanLocation locationToSave) {
      return this.scanLocationRepository.save(locationToSave);
   }

   public Collection<ScanLocation> findAllScanLocations() {
      return this.scanLocationRepository.findAll();
   }
   
   public ScanLocation getScanLocation(long id) {
      return this.scanLocationRepository.findById(id).orElseThrow(() -> {
         throw new EntityNotFoundException("Could not find ScanLocation with ID: " + id);
      });
   }
   
   public boolean deleteScanLocation(long id) {
      ScanLocation toDelete = this.scanLocationRepository.findById(id).orElseGet(null);
      if (toDelete == null) {
         return false;
      } else {
         LOGGER.info("Deleting Scan Location: " + toDelete.getPath());
         this.scanLocationRepository.deleteById(id);
         return true;
      }
   }

   private void throwIfInvalid(String pathToTest) throws IllegalScanningLocationException {
      if (pathToTest != null && !pathToTest.isBlank()) {
         File file = new File(pathToTest);
         if(!file.exists()) {
            throw new IllegalScanningLocationException("Scanning path does not exist");
         } else if (!file.isDirectory()) {
            throw new IllegalScanningLocationException("Scanning path is not a valid directory");
         }
      } else {
         throw new IllegalScanningLocationException("Scanning path was not supplied");
      }
   }

}
