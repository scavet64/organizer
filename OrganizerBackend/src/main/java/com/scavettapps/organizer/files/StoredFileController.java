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

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scavettapps.organizer.core.OrganizerRestController;
import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.Response;

/**
 *
 * @author Vincent Scavetta.
 */
@OrganizerRestController
@RequestMapping("/stored")
public class StoredFileController {

   private StoredFileService StoredFileService;

   @Autowired
   public StoredFileController(StoredFileService StoredFileService) {
      this.StoredFileService = StoredFileService;
   }

   @GetMapping(value = "/{fileID}/full", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
   public ResponseEntity<Resource> getFullVideo(@PathVariable long fileID)
       throws FileNotFoundException {

      Resource fileResource = StoredFileService.loadFileAsResource(fileID);
      ResponseEntity<Resource> resp = ResponseEntity.status(HttpStatus.OK).body(fileResource);
      return resp;

   }

   @GetMapping(value = "/all")
   public ResponseEntity<Response> getAllStoredFiles()
       throws FileNotFoundException {
      return ResponseEntity.ok(new DataResponse(this.StoredFileService.getAllStoredFiles()));
   }

}
