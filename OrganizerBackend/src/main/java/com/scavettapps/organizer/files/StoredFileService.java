//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gnostech Inc.
 */
@Service
public class StoredFileService {
   
   private StoredFileRepository storedFileRepository;

   @Autowired
   public StoredFileService(StoredFileRepository storedFileRepository) {
      this.storedFileRepository = storedFileRepository;
   }
   
   
}
