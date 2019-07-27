/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.core;

/**
 *
 * @author vstro
 */
public class EntityAlreadyExistsException extends RuntimeException {

   public EntityAlreadyExistsException() {
   }

   public EntityAlreadyExistsException(String message) {
      super(message);
   }

   public EntityAlreadyExistsException(String message, Throwable cause) {
      super(message, cause);
   }

   public EntityAlreadyExistsException(Throwable cause) {
      super(cause);
   }

   public EntityAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
   
}
