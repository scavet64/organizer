/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.controller;

import com.scavettapps.organizer.core.service.MediaFileService;
import com.scavettapps.organizer.media.ResourceService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vstro
 */
@RestController
public class VideoController {

   @Autowired
   private MediaFileService mediaFileService;

   /**
    *
    * @param fileID the file ID corresponding to the video
    * @return returns the video as a resource stream
    */
   @GetMapping("/media/{fileID}/full")
   public @ResponseBody
   Resource getFullVideo(@PathVariable String fileID)
           throws FileNotFoundException {

      Resource fileResource = mediaFileService.loadFileAsResource(fileID);
      return fileResource;
   }
}
