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
package com.scavettapps.organizer.media;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.Response;
import com.scavettapps.organizer.core.repository.DuplicateMediaFilePathRepository;
import com.scavettapps.organizer.scanner.FileScanningService;

/**
 * @author Vincent Scavetta
 */
@RestController
public class MediaFileController {

   @Autowired
   private MediaFileRepository fileRepo;

   @Autowired
   private FileScanningService fileScanningService;
   
   @Autowired
   private DuplicateMediaFilePathRepository duplicateMediaFilePathRepository;

   @RequestMapping("/file")
   public long getTest() {
      return fileRepo.count();
   }

   @RequestMapping("/all")
   public Response getAll() {
      return new DataResponse(fileRepo.findAll());
   }

   @RequestMapping("/scan")
   public Response scan(@RequestParam String path) throws InterruptedException, ExecutionException {
      return new DataResponse(fileScanningService.scanLocationForFiles(path));
   }

   @RequestMapping("/add")
   public Response addTest() {
      MediaFile test = new MediaFile(UUID.randomUUID().toString(), "name");
      test = fileRepo.save(test);
      return new DataResponse(test);
   }

//   @RequestMapping("/file/folder")
//   public Response findFilesInFolder(@RequestParam String folderName) {
//      return new DataResponse(this.fileRepo.findAllByFolder_Path(folderName));
//   }

   @RequestMapping("/file/dupes")
   public Response findDuplicates() {
      return new DataResponse(this.duplicateMediaFilePathRepository.findAll());
   }
   
   @RequestMapping("/file/dupes2")
   public Response findDuplicates2() {
      return new DataResponse(this.fileRepo.findAllByDuplicatePathsNotEmpty());
   }

}
