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

import com.scavettapps.organizer.Util.EnvironmentUtils;
import com.scavettapps.organizer.core.EntityNotFoundException;
import com.scavettapps.organizer.core.OrganizerRestController;
import com.scavettapps.organizer.core.response.ErrorResponse;
import com.scavettapps.organizer.media.json.AddTagRequest;
import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.Response;
import com.scavettapps.organizer.media.json.EditMultipleMediasTagsRequest;
import com.scavettapps.organizer.media.json.SetMediaFavoriteRequest;
import com.scavettapps.organizer.transcoding.BrampTranscodingService;
import com.scavettapps.organizer.transcoding.TranscodingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Vincent Scavetta
 */
@OrganizerRestController
@RequestMapping("/media")
public class MediaFileController {

   @Autowired
   private MediaFileService mediaFileService;
   
   @Autowired
   private BrampTranscodingService brampTranscodingService;

   /**
    *
    * @param fileHash the file ID corresponding to the video
    * @return returns the video as a resource stream
    */
   @GetMapping(value = "/{fileHash}/full", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
   public @ResponseBody
   ResponseEntity<Resource> getFullVideo(
       @PathVariable("fileHash") String fileHash,
       @RequestHeader(value = "Range", required = false) String httpRangeList
   ) throws FileNotFoundException {
      
      Resource fileResource = mediaFileService.loadFileAsResource(fileHash);
      ResponseEntity<Resource> resp = ResponseEntity.status(HttpStatus.OK).body(fileResource);
      return resp;
   }
   
   /**
    *
    * @param fileHash the file ID corresponding to the video
    * @return returns the video as a resource stream
    */
   @GetMapping(value = "/{fileHash}/info")
   public @ResponseBody
   ResponseEntity<Response> getVideoDetails(
       @PathVariable("fileHash") String fileHash,
       @RequestHeader(value = "Range", required = false) String httpRangeList
   ) throws FileNotFoundException {
      
      var mediaFile = this.mediaFileService.getMediaFile(fileHash).orElse(null);
      var details = this.brampTranscodingService.getMediaDetails(mediaFile);
      return ResponseEntity.status(HttpStatus.OK).body(new DataResponse (details));
   }
   
   /**
    *
    * @param fileHash the file ID corresponding to the video
    * @return returns the video as a resource stream
    */
   @GetMapping(value = "/{fileHash}/transcode", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
   public @ResponseBody
   ResponseEntity<Resource> transcodeVideo(
       @PathVariable("fileHash") String fileHash,
       @RequestHeader HttpHeaders headers
   ) throws FileNotFoundException, TranscodingException, MalformedURLException {

      var mediaFile = this.mediaFileService
         .getMediaFile(fileHash)
         .orElse(null);
      
      var playlistFile = brampTranscodingService.transcodeStream(mediaFile);
      Resource resource = new UrlResource(playlistFile.toURI());
      
      ResponseEntity<Resource> resp = ResponseEntity.status(HttpStatus.OK).body(resource);
      return resp;
   }
   
   /**
    *
    * @param fileHash the file ID corresponding to the video
    * @return returns the video as a resource stream
    */
   @GetMapping(value = "/{fileHash}/{partfile}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
   public @ResponseBody
   ResponseEntity<Resource> videoPartFile(
       @PathVariable("fileHash") String fileHash,
       @PathVariable("partfile") String partfile,
       @RequestHeader HttpHeaders headers
   )
       throws FileNotFoundException, TranscodingException, MalformedURLException {

      var mediaFile = this.mediaFileService.getMediaFile(fileHash).orElseThrow(EntityNotFoundException::new);
      
      File partFileTarget = Paths.get(EnvironmentUtils.getDataPath(), mediaFile.getHash(), partfile).toFile();
      
      Resource resource = new UrlResource(partFileTarget.toURI());
      
      ResponseEntity<Resource> resp = ResponseEntity.status(HttpStatus.OK).body(resource);
      return resp;
   }

   /**
    *
    * @param fileHash the file ID corresponding to the video
    * @return returns the video as a resource stream
    */
   @GetMapping(value = "/{fileHash}/deovr")
   public @ResponseBody
   ResponseEntity<Response> openVideoInDeoVR(
      @PathVariable("fileHash") String fileHash
   )
      throws FileNotFoundException {

      if (EnvironmentUtils.isRunningInsideDocker()){
         return ResponseEntity
               .status(HttpStatus.BAD_REQUEST)
               .body(new ErrorResponse("DeoVR Cannot be launched from a container"));
      }

      var mediaFile = this.mediaFileService.getMediaFile(fileHash).orElseThrow();
      try {
         // TODO: This will only work when running the jar locally and not via container.
         var deovrExe = new File("\"C:\\Program Files (x86)\\Steam\\steamapps\\common\\DeoVR Video Player\\DeoVR.exe\"");

         if (deovrExe.exists()){
            var builder = new ProcessBuilder();
            builder.command(deovrExe.getPath() + " \"" + mediaFile.getPath() + "\"");

            builder.start();
         } else {
            return ResponseEntity
               .status(HttpStatus.BAD_REQUEST)
               .body(new ErrorResponse("DeoVR is not installed."));
         }

         
      } catch (IOException ex) {
         return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("Failed to launch deovr: " + ex.getMessage()));
      }

      return ResponseEntity.ok().build();
   }

   /**
    * Update a single media file's tags
    *
    * @param request The request containing details about which media file and which tags to add/remove
    * @return The updated media file containing its new media tags.
    */
   @PutMapping("/tags")
   public ResponseEntity<Response> updateTagsForMedia(
       @RequestBody @Validated AddTagRequest request
   ) {

      MediaFile updatedFile = mediaFileService.setMediasTags(
          request.getMediaId(),
          request.getTagIds()
      );

      return new ResponseEntity<>(
          new DataResponse(updatedFile),
          HttpStatus.OK
      );
   }
   
   /**
    * Update multiple different media file's tags at once.
    *
    * <p>
    *    This is intended to be used in bulk media tag updates.
    * </p>
    *
    * @param request The request containing a list of media tags which contain the linkage between the tag and media.
    * @return The list of media that was updated.
    */
   @PutMapping("/tags/multiple")
   public ResponseEntity<Response> updateMultipleMediasTags(
       @RequestBody @Validated EditMultipleMediasTagsRequest request
   ) {
      
      List<MediaFile> updatedMedia = new ArrayList<>();
      
      request.getUpdatedMedia().forEach(mediaToTags -> {
         updatedMedia.add(
             mediaFileService.setMediasTags(
                 mediaToTags.getMediaId(), 
                 mediaToTags.getTagIds()
             )
         );
      });

      return new ResponseEntity<>(
          new DataResponse(updatedMedia),
          HttpStatus.OK
      );
   }


   /**
    * Find a page of media. This is the main method as to get all media.
    *
    * @param pageable The pageable object outlining which page and how many items per page to get.
    * @param request The request object containing the constraints on which the search should be filtered on.
    * @return Page of filtered media.
    */
   @GetMapping
   public ResponseEntity<Response> findMedia(
      Pageable pageable,
      MediaFileRequest request
   ) {
      Page<MediaFile> mediaPage = this.mediaFileService.getPageOfMediaFiles(pageable, request);
      return new ResponseEntity<Response>(new DataResponse(mediaPage), HttpStatus.OK);
   }

   /**
    * Get a random video from the database.
    *
    * @return a random video
    */
   @GetMapping("/random/video")
   public ResponseEntity<Response> getRandomVideo() {
      MediaFile mediaPage = this.mediaFileService.getRandomVideo();
      return new ResponseEntity<Response>(new DataResponse(mediaPage), HttpStatus.OK);
   }
   
   /**
    * Add a view to the media with the provided ID.
    *
    * @param mediaId the media's ID that should have its view count incremented
    * @return Result message
    */
   @PutMapping("/view")
   public ResponseEntity<Response> addView(@RequestBody Long mediaId) {
      mediaFileService.addView(mediaId);
      return ResponseEntity.ok(new DataResponse("View has been added"));
   }
   
   /**
    * Toggle a media file as a favorite.
    * @param req The json request containing information about which video and the current favorite status
    * @return Result message
    */
   @PutMapping("/favorite")
   public ResponseEntity<Response> toggleFavorite(@RequestBody SetMediaFavoriteRequest req) {
      MediaFile updated = mediaFileService.setFavorite(req.getMediaId(), req.getIsFavorite());
      return ResponseEntity.ok(new DataResponse(updated));
   }

   /**
    * Toggle a media file as a favorite.
    * @param req The json request containing information about which video and the current favorite status
    * @return Result message
    */
   @PutMapping("/ignore")
   public ResponseEntity<Response> toggleIgnored(@RequestBody SetMediaFavoriteRequest req) {
      MediaFile updated = mediaFileService.setIgnored(req.getMediaId(), req.getIsFavorite());
      return ResponseEntity.ok(new DataResponse(updated));
   }
   
   /**
    * get a list of files that contain duplicates.
    *
    * @return List of duplicate media files
    */
   @GetMapping("/duplicate")
   public Response getDuplicates() {
      return new DataResponse(this.mediaFileService.findAllMediaWithDuplicates());
   }
}
