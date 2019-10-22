/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.media;

import com.scavettapps.organizer.core.EntityNotFoundException;
import com.scavettapps.organizer.tag.Tag;
import com.scavettapps.organizer.tag.TagRepository;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.VideoAttributes;
import ws.schild.jave.VideoSize;

/**
 *
 * @author vstro
 */
@Service
public class MediaFileService {

   @Autowired
   private FileRepository fileRepository;
   @Autowired
   private TagRepository tagRepository;
   @Autowired
   private MediaFileSpecification mediaFileSpecification;

   public MediaFile getMediaFile(String hash) {
      return fileRepository.findByHash(hash).orElseThrow(() -> new EntityNotFoundException());
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

      MediaFile file = getMediaFile(hash);

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

      MediaFile file = getMediaFile(hash);

      try {
         File target;
         if (file.getName().toLowerCase().endsWith(".avi")) {
            target = transcodeFile2(file);
         } else {
            target = new File(file.getPath());
         }

         Resource resource = new UrlResource(target.toURI());
         if (resource.exists()) {
            return resource;
         } else {
            throw new FileNotFoundException("File not found: " + file);
         }
      } catch (IllegalArgumentException ex) {
         Logger.getLogger(MediaFileService.class.getName()).log(Level.SEVERE, null, ex);
         throw new FileNotFoundException("Malformed URL Exception: " + file);
      } catch (EncoderException ex) {
         Logger.getLogger(MediaFileService.class.getName()).log(Level.SEVERE, null, ex);
         throw new FileNotFoundException("Malformed URL Exception: " + file);
      } catch (IOException ex) {
         Logger.getLogger(MediaFileService.class.getName()).log(Level.SEVERE, null, ex);
         throw new FileNotFoundException("Malformed URL Exception: " + file);
      }
   }

   private File transcodeFile(MediaFile file) throws IOException, EncoderException {
      File folder = new File("C:/temp/");
      if (!folder.exists()) {
         folder.mkdirs();
      }
      File source = new File(file.getPath());
      File target = Paths.get(folder.getAbsolutePath(), file.getName()).toFile();
      AudioAttributes audio = new AudioAttributes();
      audio.setCodec("eac3");
      audio.setBitRate(97000);
      audio.setSamplingRate(48000);
      audio.setChannels(2);
      VideoAttributes video = new VideoAttributes();
      video.setCodec("mpeg4");
      video.setBitRate(1500000);
      video.setFrameRate(30);
      video.setSize(new VideoSize(320, 240));
      EncodingAttributes attrs = new EncodingAttributes();
      attrs.setFormat("mp4");
      attrs.setVideoAttributes(video);
      attrs.setAudioAttributes(audio);
      Encoder encoder = new Encoder();
      encoder.encode(new MultimediaObject(source), target, attrs);

      return target;
   }

   private File transcodeFile2(MediaFile file) throws IOException, EncoderException {
      File folder = new File("C:/temp/");
      if (!folder.exists()) {
         folder.mkdirs();
      }
      File source = new File(file.getPath());
      File target = Paths.get(folder.getAbsolutePath(), file.getName()).toFile();
      FFmpeg ffmpeg = new FFmpeg(getFileFromResources("ffmpeg.exe").getPath());
      FFprobe ffprobe = new FFprobe(getFileFromResources("ffprobe.exe").getPath());

      FFmpegBuilder builder = new FFmpegBuilder()
          .setInput(source.getAbsolutePath()) // Filename, or a FFmpegProbeResult
          .overrideOutputFiles(true) // Override the output if it exists

          .addOutput(target.getAbsolutePath()) // Filename for the destination
          .setFormat("mp4") // Format is inferred from filename, or can be set
          //.setTargetSize(250_000) // Aim for a 250KB file

          .disableSubtitle() // No subtiles

//          .setAudioChannels(1) // Mono audio
//          .setAudioCodec("aac") // using the aac codec
//          .setAudioSampleRate(48_000) // at 48KHz
//          .setAudioBitRate(32768) // at 32 kbit/s

          .setVideoCodec("libx264") // Video using x264

          .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
          .done();

      FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

// Run a one-pass encode
      executor.createJob(builder).run();

// Or run a two-pass encode (which is better quality at the cost of being slower)
      //executor.createTwoPassJob(builder).run();

      return target;
   }
   
   // get file from classpath, resources folder
    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

   public MediaFile addTagToMediaFile(long mediaId, long tagId) {
      if (mediaId < 0 || tagId < 0) {
         throw new IllegalArgumentException("Ids cannot be negative");
      }

      // Find the media file
      MediaFile file = fileRepository.findById(mediaId).orElseThrow();

      // Find the Tag and add it.
      Tag tag = tagRepository.findById(tagId).orElseThrow();
      file.addTag(tag);

      return fileRepository.save(file);
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
      MediaFile file = fileRepository.findById(mediaId).orElseThrow();

      file.getTags().clear();

      for (Long tagId : tags) {
         // Find the Tag and add it to the file.
         Tag tag = tagRepository.findById(tagId).orElseThrow();
         file.addTag(tag);
      }

      return fileRepository.save(file);
   }

   @Transactional
   public Page<MediaFile> getPageOfMediaFiles(Pageable page, MediaFileRequest mediaFileRequest) {
      return this.fileRepository.findAll(getDefaultSpecification(mediaFileRequest), page);
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
         for (Tag tag : params.getTags()) {
            if (specs == null) {
               specs = Specification.where(
                   mediaFileSpecification.getTagAttributeContains("name", "eq:" + tag.getName())
               );
            } else {
               specs = specs.and(
                   mediaFileSpecification.getTagAttributeContains("name", "eq:" + tag.getName())
               );
            }
         }
      }

      return specs;
   }
}
