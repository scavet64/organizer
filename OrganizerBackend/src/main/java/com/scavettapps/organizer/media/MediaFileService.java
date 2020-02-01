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
package com.scavettapps.organizer.media;

import com.scavettapps.organizer.core.EntityNotFoundException;
import com.scavettapps.organizer.tag.Tag;
import com.scavettapps.organizer.tag.TagRepository;
import com.scavettapps.organizer.transcoding.ITranscodingService;
import com.scavettapps.organizer.transcoding.TranscodingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vincent Scavetta
 */
@Service
public class MediaFileService {

   private final MediaFileRepository mediaFileRepository;
   private final MediaFileSpecification mediaFileSpecification;
   private final TagRepository tagRepository;
   private final ITranscodingService transcodingService;

   @Autowired
   public MediaFileService(
       MediaFileRepository mediaFileRepository, 
       MediaFileSpecification mediaFileSpecification, 
       TagRepository tagRepository,
       @Qualifier("bramp") ITranscodingService transcodingService
   ) {
      this.mediaFileRepository = mediaFileRepository;
      this.mediaFileSpecification = mediaFileSpecification;
      this.tagRepository = tagRepository;
      this.transcodingService = transcodingService;
   }
   
   public Optional<MediaFile> getMediaFile(long id) {
      return this.mediaFileRepository.findById(id);
   }

   public Optional<MediaFile> getMediaFile(String hash) {
      return this.mediaFileRepository.findByHash(hash);
   }
   
   public Optional<MediaFile> getMediaFile(String fileName, long size) {
      return this.mediaFileRepository.findByNameAndSize(fileName, size);
   }
   
   public MediaFile saveMediaFile(MediaFile file) {
      return this.mediaFileRepository.save(file);
   }

   /**
    * finds the file on the file system and loads it into a resource
    *
    * @param hash the hash of the file
    * @return resource that is the file
    * @throws FileNotFoundException if the file is not found
    * @throws EntityNotFoundException if the hash is not found
    */
   public Resource loadFileAsResource(String hash) throws FileNotFoundException {

      MediaFile file = getMediaFile(hash).orElseThrow(() -> new EntityNotFoundException());

      try {
         Resource resource = new UrlResource(new File(file.getPath()).toURI());
         if (resource.exists()) {
            return resource;
         } else {
            throw new FileNotFoundException("File not found: " + file);
         }
      } catch (MalformedURLException ex) {
         throw new FileNotFoundException("Malformed URL Exception: " + file);
      }
   }

   /**
    * finds the file on the file system and loads it into a resource
    *
    * @param hash the hash of the file
    * @return resource that is the file
    * @throws FileNotFoundException if the file is not found
    * @throws EntityNotFoundException if the hash is not found
    */
   public Resource loadFileAsResource2(String hash) throws FileNotFoundException {

      MediaFile file = getMediaFile(hash).orElseThrow(() -> new EntityNotFoundException());

      try {
         File target;
         if (file.getName().toLowerCase().endsWith(".avi")) {
            target = transcodingService.transcodeMediaFile(file);
         } else {
            target = new File(file.getPath());
         }

         Resource resource = new UrlResource(target.toURI());
         if (resource.exists()) {
            return resource;
         } else {
            throw new FileNotFoundException("File not found: " + file);
         }
      } catch (MalformedURLException ex) {
         Logger.getLogger(MediaFileService.class.getName()).log(Level.SEVERE, null, ex);
         throw new FileNotFoundException("Malformed URL Exception: " + file);
      } catch (TranscodingException ex) {
         Logger.getLogger(MediaFileService.class.getName()).log(Level.SEVERE, null, ex);
         throw new FileNotFoundException("Transcoding Exception: " + file);
      }
   }

   public MediaFile addTagToMediaFile(long mediaId, long tagId) {
      if (mediaId < 0 || tagId < 0) {
         throw new IllegalArgumentException("Ids cannot be negative");
      }

      // Find the media file
      MediaFile file = mediaFileRepository.findById(mediaId).orElseThrow();

      // Find the Tag and add it.
      Tag tag = tagRepository.findById(tagId).orElseThrow();
      file.addTag(tag);

      return mediaFileRepository.save(file);
   }

   /**
    * TODO: This could be made a little more efficient so that it does not reach out to the database
    * so much, but since this is meant to be local/in memory for now, it is not as big of a deal.
    *
    * @param mediaId
    * @param tags
    * @return
    */
   @Transactional
   public MediaFile addTagToMediaFile(long mediaId, Collection<Long> tags) {
      if (mediaId < 0) {
         throw new IllegalArgumentException("Ids cannot be negative");
      }

      // Find the media file
      MediaFile file = mediaFileRepository.findById(mediaId).orElseThrow();

      file.getTags().clear();

      for (Long tagId : tags) {
         // Find the Tag and add it to the file.
         Tag tag = tagRepository.findById(tagId).orElseThrow();
         file.addTag(tag);
      }

      return mediaFileRepository.save(file);
   }

   @Transactional
   public Page<MediaFile> getPageOfMediaFiles(Pageable page, MediaFileRequest mediaFileRequest) {
      return this.mediaFileRepository.findAll(getDefaultSpecification(mediaFileRequest), page);
   }

   private Specification<MediaFile> getDefaultSpecification(MediaFileRequest params) {
      // Exposed attributes in API spec do not need to be same as Database table column names.
      Specification<MediaFile> specs = null;
      if (params.getName() != null) {
         specs = Specification.where(mediaFileSpecification.getStringTypeSpecification(
             "name",
             params.getName())
         );
      }
      if (params.getTags() != null) {
         for (Long tagId : params.getTags()) {
            if (specs == null) {
               specs = Specification.where(
                   mediaFileSpecification.getTagAttributeEquals("id", tagId)
               );
            } else {
               specs = specs.and(
                   mediaFileSpecification.getTagAttributeEquals("id", tagId)
               );
            }
         }
      }

      return specs;
   }

   public MediaFile addView(long mediaId) {
      MediaFile file = getMediaFile(mediaId).orElseThrow(() -> new EntityNotFoundException());
      file.incrementViews();
      return this.mediaFileRepository.save(file);
   }
   
   public List<MediaFile> findAllMediaWithDuplicates() {
      return this.mediaFileRepository.findAllByDuplicatePathsNotEmpty();
   }
}
