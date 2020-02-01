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

import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.Response;
import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Vincent Scavetta
 */
@RestController
@RequestMapping("/media")
public class MediaFileController {

   @Autowired
   private MediaFileService mediaFileService;

   /**
    *
    * @param fileHash the file ID corresponding to the video
    * @return returns the video as a resource stream
    */
   @GetMapping(value = "/{fileHash}/full", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
   public @ResponseBody
   ResponseEntity<Resource> getFullVideo(@PathVariable String fileHash)
       throws FileNotFoundException {

      Resource fileResource = mediaFileService.loadFileAsResource(fileHash);
      ResponseEntity<Resource> resp = ResponseEntity.status(HttpStatus.OK).body(fileResource);
      return resp;
   }

   @PutMapping("/tags")
   public ResponseEntity<Response> updateTagsForMedia(
       @RequestBody @Validated AddTagRequest request
   ) {

      MediaFile updatedFile = mediaFileService.addTagToMediaFile(
          request.getMediaId(),
          request.getTagIds()
      );

      return new ResponseEntity<>(
          new DataResponse(updatedFile),
          HttpStatus.OK
      );
   }

   @GetMapping
   public ResponseEntity<Response> findMedia(
       Pageable pageable,
       MediaFileRequest request
   ) {
      Page<MediaFile> mediaPage = this.mediaFileService.getPageOfMediaFiles(pageable, request);
      return new ResponseEntity(new DataResponse(mediaPage), HttpStatus.OK);
   }
   
   @PutMapping("/view")
   public ResponseEntity<Response> addView(@RequestBody Long mediaId) {
      mediaFileService.addView(mediaId);
      return ResponseEntity.ok(new DataResponse("View has been added"));
   }
   
   @GetMapping("/duplicate")
   public Response getDuplicates() {
      return new DataResponse(this.mediaFileService.findAllMediaWithDuplicates());
   }
}
