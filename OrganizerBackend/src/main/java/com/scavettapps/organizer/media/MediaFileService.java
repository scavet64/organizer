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
import com.scavettapps.organizer.transcoding.ITranscodingService;
import com.scavettapps.organizer.transcoding.TranscodingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Vincent Scavetta
 */
@Service
public class MediaFileService {

   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   public static final String VIDEO = "/video";

   public static final String CONTENT_TYPE = "Content-Type";
   public static final String CONTENT_LENGTH = "Content-Length";
   public static final String VIDEO_CONTENT = "video/";
   public static final String CONTENT_RANGE = "Content-Range";
   public static final String ACCEPT_RANGES = "Accept-Ranges";
   public static final String BYTES = "bytes";
   public static final int BYTE_RANGE = 1024;

   private final MediaFileRepository mediaFileRepository;
   private final MediaFileSpecification mediaFileSpecification;
   private final TagRepository tagRepository;
   
   @Autowired
   private  BrampTranscodingService transcodingService;

   @Autowired
   public MediaFileService(
       MediaFileRepository mediaFileRepository,
       MediaFileSpecification mediaFileSpecification,
       TagRepository tagRepository
       //@Qualifier("bramp") ITranscodingService transcodingService
   ) {
      this.mediaFileRepository = mediaFileRepository;
      this.mediaFileSpecification = mediaFileSpecification;
      this.tagRepository = tagRepository;
      //this.transcodingService = transcodingService;
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

   public ResponseEntity<byte[]> prepareContent(String hash, String range) throws TranscodingException {
      
      MediaFile file = getMediaFile(hash).orElseThrow(() -> new EntityNotFoundException());

      long rangeStart = 0;
      long rangeEnd;
      byte[] data;
      Long fileSize;
      String pathToFileToPlay;
      if (file.getMimetype().equals("video/x-matroska")) {
         // Convert
         //this.transcodingService.transcodeMediaFile();
         pathToFileToPlay = file.getPath();
      } else {
         pathToFileToPlay = file.getPath();
      }
      
      try {
         //fileSize = getFileSize(file.getPath());
         fileSize = file.getSize();
         if (range == null) {
            return ResponseEntity.status(HttpStatus.OK)
                //.header(CONTENT_TYPE, VIDEO_CONTENT + fileType)
                .header(CONTENT_LENGTH, String.valueOf(fileSize))
                .body(readByteRange(pathToFileToPlay, rangeStart, fileSize - 1)); // Read the object and convert it as bytes
         }
         String[] ranges = range.split("-");
         rangeStart = Long.parseLong(ranges[0].substring(6));
         if (ranges.length > 1) {
            rangeEnd = Long.parseLong(ranges[1]);
         } else {
            rangeEnd = fileSize - 1;
         }
         if (fileSize < rangeEnd) {
            rangeEnd = fileSize - 1;
         }
         data = readByteRange(pathToFileToPlay, rangeStart, rangeEnd);
      } catch (IOException e) {
         logger.error("Exception while reading the file {}", e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
      String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
      return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
          //.header(CONTENT_TYPE, VIDEO_CONTENT + fileType)
          .header(ACCEPT_RANGES, BYTES)
          .header(CONTENT_LENGTH, contentLength)
          .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
          .body(data);
   }

   /**
    * ready file byte by byte.
    *
    * @param filePath String.
    * @param start long.
    * @param end long.
    * @return byte array.
    * @throws IOException exception.
    */
   public byte[] readByteRange(String filePath, long start, long end) throws IOException {
      Path path = Paths.get(filePath);
      try ( InputStream inputStream = (Files.newInputStream(path));  ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
         byte[] data = new byte[BYTE_RANGE];
         int nRead;
         while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            bufferedOutputStream.write(data, 0, nRead);
         }
         bufferedOutputStream.flush();
         byte[] result = new byte[(int) (end - start) + 1];
         System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
         return result;
      }
   }

//   /**
//    * Get the filePath.
//    *
//    * @return String.
//    */
//   private String getFilePath() {
//      URL url = this.getClass().getResource(VIDEO);
//      return new File(url.getFile()).getAbsolutePath();
//   }

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

//   private ResourceRegion resourceRegion(UrlResource video, HttpHeaders headers) {
//	var contentLength = video.contentLength();
//	var range = headers.get("range");
//	if (range != null) {
//		var start = range.getRangeStart(contentLength);
//		var end = range.getRangeEnd(contentLength);
//		var rangeLength = min(1 * 1024 * 1024, end - start + 1);
//		ResourceRegion(video, start, rangeLength)
//	} else {
//		val rangeLength = min(1 * 1024 * 1024, contentLength)
//		ResourceRegion(video, 0, rangeLength)
//	}
//}
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
         //Logger.getLogger(MediaFileService.class.getName()).log(Level.SEVERE, null, ex);
         throw new FileNotFoundException("Malformed URL Exception: " + file);
      } catch (TranscodingException ex) {
         //Logger.getLogger(MediaFileService.class.getName()).log(Level.SEVERE, null, ex);
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

   /**
    * TODO: There has to be a better way than null checking every time
    *
    * @param params
    * @return
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

   public MediaFile setFavorite(long mediaId, boolean isFavorite) {
      MediaFile file = getMediaFile(mediaId).orElseThrow(() -> new EntityNotFoundException());
      file.setIsFavorite(isFavorite);
      return this.mediaFileRepository.save(file);
   }

   public MediaFile setIgnored(long mediaId, boolean isIgnored) {
      MediaFile file = getMediaFile(mediaId).orElseThrow(() -> new EntityNotFoundException());
      file.setIsFavorite(isIgnored);
      return this.mediaFileRepository.save(file);
   }
}
