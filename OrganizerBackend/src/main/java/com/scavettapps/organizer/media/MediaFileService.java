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
import com.scavettapps.organizer.transcoding.BrampTranscodingService;
import com.scavettapps.organizer.transcoding.TranscodingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

/**
 *
 * @author Vincent Scavetta
 */
@Service
public class MediaFileService {

   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   public static final String VIDEO = "/video";

   private final MediaFileRepository mediaFileRepository;
   private final MediaFileSpecification mediaFileSpecification;
   private final TagRepository tagRepository;

   @Autowired
   public MediaFileService(
       MediaFileRepository mediaFileRepository,
       MediaFileSpecification mediaFileSpecification,
       TagRepository tagRepository
   ) {
      this.mediaFileRepository = mediaFileRepository;
      this.mediaFileSpecification = mediaFileSpecification;
      this.tagRepository = tagRepository;
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

   /**
    * Save a media file to the database.
    * @param file The media file that should be saved
    * @return The updated media file
    */
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

      MediaFile file = getMediaFile(hash).orElseThrow(EntityNotFoundException::new);

      if (file.getMimetype().equals("video/x-matroska")) {
         // Convert
      }

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
    * Content length.
    *
    * @param filePath String.
    * @return Long.
    */
   public Long getFileSize(String filePath) {
      return Optional.ofNullable(filePath)
          .map(file -> Paths.get(file))
          .map(this::sizeFromFile)
          .orElse(0L);
   }

   /**
    * Getting the size from the path.
    *
    * @param path Path.
    * @return Long.
    */
   private Long sizeFromFile(Path path) {
      try {
         return Files.size(path);
      } catch (IOException ioException) {
         logger.error("Error while getting the file size", ioException);
      }
      return 0L;
   }

   /**
    * This method will update the media file's tags using the collection of provided tags.
    *
    * The method will clear all of the previously attached tags from this media and add each of the new tags in the
    * collection to the media file if the tag exists. If the tag does not exist, an exception is thrown and the media
    * file is not updated.
    *
    * @param mediaId The media file to update.
    * @param tags The full collection of tags that will be attached to this media file.
    * @return The updated media file containing the new list of tags
    */
   @Transactional
   public MediaFile setMediasTags(long mediaId, Collection<Long> tags) {
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

   /**
    * Builds a specification object using the search parameters provided to this method. This specification object will
    * be use to query a list of media from the database.
    *
    * TODO: There has to be a better way than null checking every time
    *
    * @param params The options to filter media on.
    * @return The MediaFile specification object to be used in a query
    */
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
      if (params.getMediaType() != null) {
         if (specs == null) {
            specs = Specification.where(mediaFileSpecification.getStringTypeSpecification(
                "mimetype",
                params.getMediaType()
            ));
         } else {
            specs = specs.and(mediaFileSpecification.getStringTypeSpecification(
                "mimetype",
                params.getMediaType()
            ));
         }
      }

      if (params.getIsFavorite()) {
         if (specs == null) {
            specs = Specification.where(mediaFileSpecification.getBooleanTypeSpecification(
                "isFavorite",
                "eq:True"
            ));
         } else {
            specs = specs.and(mediaFileSpecification.getBooleanTypeSpecification(
                "isFavorite",
                "eq:True"
            ));
         }
      }

      if (!params.getShowIgnored()) {
         if (specs == null) {
            specs = Specification.where(mediaFileSpecification.getBooleanTypeSpecification(
               "isIgnored",
               "eq:False"
            ));
         } else {
            specs = specs.and(mediaFileSpecification.getBooleanTypeSpecification(
               "isIgnored",
               "eq:False"
            ));
         }
      }

      return specs;
   }

   /**
    * Add a view to the specified media
    * @param mediaId The media to increment its view count
    * @return
    */
   public MediaFile addView(long mediaId) {
      MediaFile file = getMediaFile(mediaId).orElseThrow(EntityNotFoundException::new);
      file.incrementViews();
      return this.mediaFileRepository.save(file);
   }

   /**
    * @return A list of media that contains duplicate files
    */
   public List<MediaFile> findAllMediaWithDuplicates() {
      return this.mediaFileRepository.findAllByDuplicatePathsNotEmpty();
   }

   /**
    * Sets if a media file should be set as a favorite.
    * @param mediaId The media file to update
    * @param isFavorite if this media is now a favorite or not
    * @return The updated media file
    */
   public MediaFile setFavorite(long mediaId, boolean isFavorite) {
      MediaFile file = getMediaFile(mediaId).orElseThrow(EntityNotFoundException::new);
      file.setIsFavorite(isFavorite);
      return this.mediaFileRepository.save(file);
   }

   /**
    * Sets if a media file should be set as ignored
    * @param mediaId The media file to update
    * @param isIgnored if this media is now ignored or not
    * @return The updated media file
    */
   public MediaFile setIgnored(long mediaId, boolean isIgnored) {
      MediaFile file = getMediaFile(mediaId).orElseThrow(EntityNotFoundException::new);
      file.setIsIgnored(isIgnored);
      return this.mediaFileRepository.save(file);
   }

   /**
    * @return a random video from the database.
    */
   public MediaFile getRandomVideo() {
      var list = this.mediaFileRepository.findAllByMimetypeContaining("video");
      var rng = new Random();
      return list.get(rng.nextInt(list.size()));
   }
}
