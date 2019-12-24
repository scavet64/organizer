//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.transcoding;

/**
 *
 * @author Gnostech Inc.
 */
public class TranscodingException extends Exception {

   public TranscodingException() {
   }

   public TranscodingException(String message) {
      super(message);
   }

   public TranscodingException(String message, Throwable cause) {
      super(message, cause);
   }

   public TranscodingException(Throwable cause) {
      super(cause);
   }

   public TranscodingException(String message, Throwable cause, boolean enableSuppression, 
       boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
   
}
