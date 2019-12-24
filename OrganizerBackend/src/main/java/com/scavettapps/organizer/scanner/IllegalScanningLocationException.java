//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.scanner;

/**
 *
 * @author Gnostech Inc.
 */
public class IllegalScanningLocationException extends Exception {

   public IllegalScanningLocationException() {
   }

   public IllegalScanningLocationException(String message) {
      super(message);
   }

   public IllegalScanningLocationException(String message, Throwable cause) {
      super(message, cause);
   }

   public IllegalScanningLocationException(Throwable cause) {
      super(cause);
   }

   public IllegalScanningLocationException(String message, Throwable cause, 
       boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
   
}
