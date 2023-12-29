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
package com.scavettapps.organizer.core;

import com.scavettapps.organizer.core.response.ErrorResponse;
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
 * @author Vincent Scavetta
 */
@ControllerAdvice
public class RestExceptionHandler {
   
   private static final Logger LOGGER = LogManager.getLogger(RestExceptionHandler.class.getName());
   
   @ExceptionHandler(value = { FileNotFoundException.class })
   public ResponseEntity<ErrorResponse> fileNotFound(FileNotFoundException ex, WebRequest request) {
      LOGGER.debug("handling FileNotFoundException...");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
   }

   @ExceptionHandler(value = { EntityNotFoundException.class })
   public ResponseEntity<ErrorResponse> entityNotFoundException(EntityNotFoundException ex, WebRequest request) {
      LOGGER.debug("handling EntityNotFoundException...");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getLocalizedMessage()));
   }
   
   @ExceptionHandler(value = { EntityAlreadyExistsException.class })
   public ResponseEntity<ErrorResponse> entityAlreadyExistsException(EntityAlreadyExistsException ex, WebRequest request) {
      LOGGER.debug("handling EntityAlreadyExistsException...");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getLocalizedMessage()));
   }
}
