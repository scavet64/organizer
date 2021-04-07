/**
 * Copyright 2019 Vincent Scavetta
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scavettapps.organizer.management;

import com.scavettapps.organizer.files.StoredFileRepository;
import com.scavettapps.organizer.folder.FolderRepository;
import com.scavettapps.organizer.management.backup.BackupFile;
import com.scavettapps.organizer.management.backup.BackupMediaTags;
import com.scavettapps.organizer.media.MediaFileRepository;
import com.scavettapps.organizer.media.json.MediaTags;
import com.scavettapps.organizer.scanner.ScanLocationRepository;
import com.scavettapps.organizer.tag.Tag;
import com.scavettapps.organizer.tag.TagRepository;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Vincent Scavetta.
 */
@Service
public class ManagementService {

   private static final Logger LOGGER = LoggerFactory.getLogger(ManagementService.class);

   private FolderRepository folderRepository;
   private MediaFileRepository mediaFileRepository;
   private ScanLocationRepository scanLocationRepository;
   private StoredFileRepository storedFileRepository;
   private TagRepository tagRepository;

   @Autowired
   public ManagementService(
      FolderRepository folderRepository,
      MediaFileRepository mediaFileRepository,
      ScanLocationRepository scanLocationRepository,
      StoredFileRepository storedFileRepository,
      TagRepository tagRepository
   ) {
      this.folderRepository = folderRepository;
      this.mediaFileRepository = mediaFileRepository;
      this.scanLocationRepository = scanLocationRepository;
      this.storedFileRepository = storedFileRepository;
      this.tagRepository = tagRepository;
   }

   @Transactional
   public void clearAllScannedMedia() {
      try {
         this.mediaFileRepository.deleteAll();
         this.storedFileRepository.deleteAll();
         this.folderRepository.deleteAll();
         //this.scanLocationRepository.deleteAll();
         LOGGER.info("Successfully cleared all media");
      } catch (DataAccessException ex) {
         LOGGER.error("Failed to clear media", ex);
         throw new ManagementException(ex);
      }
   }

   public BackupMediaTags exportMediaTags() {

      var hashesToTags = new HashMap<String, Set<Tag>>();
      var allMedia = this.mediaFileRepository.findAll();
      allMedia.forEach(mediaFile -> {
         if (mediaFile.getTags().size() > 0) {
            hashesToTags.put(mediaFile.getHash(), mediaFile.getTags());
         }
      });

      var backupFile = BackupMediaTags.builder()
         .tags(this.tagRepository.findAll())
         .hashesToTags(hashesToTags)
         .build();

      return backupFile;
   }

   public void importMediaTags(BackupMediaTags importedData) {
      var allMedia = this.mediaFileRepository.findAll();
      this.tagRepository.saveAll(importedData.getTags());

      var hashesToTags = importedData.getHashesToTags();
      allMedia.forEach(mediaFile -> {
         var mediaTags = hashesToTags.get(mediaFile.getHash());
         if (mediaTags != null) {
            mediaTags.forEach(mediaFile::addTag);
         }
      });

      this.mediaFileRepository.saveAll(allMedia);
   }


}
