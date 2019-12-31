//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.scanner;

/**
 *
 * @author Gnostech Inc.
 */
public class AddScanLocationRequest {
   private String path;

   public AddScanLocationRequest() {
   }

   public AddScanLocationRequest(String path) {
      this.path = path;
   }

   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }
}
