/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.media;

import com.scavettapps.organizer.core.response.DataResponse;
import com.scavettapps.organizer.core.response.Response;
import com.scavettapps.organizer.media.ResourceService;
import com.scavettapps.organizer.tag.Tag;
import com.sun.swing.internal.plaf.metal.resources.metal;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vstro
 */
@RestController
@RequestMapping("/media")
public class MediaController {

   @Autowired
   private MediaFileService mediaFileService;

   /**
    *
    * @param fileID the file ID corresponding to the video
    * @return returns the video as a resource stream
    */
   @GetMapping("/{fileID}/full")
   public @ResponseBody
   Resource getFullVideo(@PathVariable String fileID)
           throws FileNotFoundException {

      Resource fileResource = mediaFileService.loadFileAsResource(fileID);
      return fileResource;
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
}
