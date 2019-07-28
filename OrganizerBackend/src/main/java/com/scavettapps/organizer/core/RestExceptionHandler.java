/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.core;

import com.scavettapps.organizer.core.response.ErrorResponse;
import com.scavettapps.organizer.core.EntityNotFoundException;
import java.io.FileNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author vstro
 */
@ControllerAdvice
public class RestExceptionHandler {
   
   private static final Logger LOGGER = LogManager.getLogger(RestExceptionHandler.class.getName());
   
   @ExceptionHandler(value = { FileNotFoundException.class })
   public ResponseEntity fileNotFound(FileNotFoundException ex, WebRequest request) {
      LOGGER.debug("handling FileNotFoundException...");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
   }

   @ExceptionHandler(value = { EntityNotFoundException.class })
   public ResponseEntity entityNotFoundException(EntityNotFoundException ex, WebRequest request) {
      LOGGER.debug("handling EntityNotFoundException...");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getLocalizedMessage()));
   }
   
   @ExceptionHandler(value = { EntityAlreadyExistsException.class })
   public ResponseEntity entityAlreadyExistsException(EntityAlreadyExistsException ex, WebRequest request) {
      LOGGER.debug("handling EntityAlreadyExistsException...");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getLocalizedMessage()));
   }
}