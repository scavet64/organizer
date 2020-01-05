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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vincent Scavetta
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
      } else {
         throw new IllegalScanningLocationException("Scanning path was not supplied");
      }
   }

}
