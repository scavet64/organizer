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
package com.scavettapps.organizer.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.scavettapps.organizer.core.EntityNotFoundException;

/**
 *
 * @author Vincent Scavetta
 */
@Service
public class StoredFileService {
   
   private final StoredFileRepository storedFileRepository;

   @Autowired
   public StoredFileService(StoredFileRepository storedFileRepository) {
      this.storedFileRepository = storedFileRepository;
   }
   
   public StoredFile getStoredFile(long id) {
      StoredFile file = this.storedFileRepository.findById(id).orElse(null);
      return file;
   }
   
   public StoredFile getStoredFile(String hash) {
      StoredFile file = this.storedFileRepository.findByHash(hash).orElse(null);
      return file;
   }
   
   public List<StoredFile> getAllStoredFiles() {
      return this.storedFileRepository.findAll();
   }
   
   /**
    * finds the file on the file system and loads it into a resource
    *
    * @param id the id of the file
    * @return resource that is the file
    * @throws FileNotFoundException if the file is not found
    * @throws EntityNotFoundException if the hash is not found
    */
   public Resource loadFileAsResource(long id) throws FileNotFoundException {

      StoredFile file = getStoredFile(id);

      try {
         Resource resource = new UrlResource(new File(file.getPath()).toURI());
         if (resource.exists()) {
            return resource;
         } else {
            throw new FileNotFoundException("File not found: " + file);
         }
      } catch (MalformedURLException ex) {
         throw new FileNotFoundException("Malformed URL Exception: " + file);
      }
   }
   
}
