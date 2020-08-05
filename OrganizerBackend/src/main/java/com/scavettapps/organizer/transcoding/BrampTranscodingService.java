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
package com.scavettapps.organizer.transcoding;

import com.scavettapps.organizer.core.ApplicationResourceService;
import com.scavettapps.organizer.media.MediaFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vincent Scavetta
 */
@Service("bramp")
public class BrampTranscodingService implements ITranscodingService {

   private static final String TEMP_LOCATION = "C:/temp/organizer";
   private static final String FFPROBE_EXE = "ffprobe.exe";
   private static final String FFMPEG_EXE = "ffmpeg.exe";
   private static final String THUMBNAIL_FORMAT = ".png";
   private static final String VIDEO_FORMAT = "mp4";
   private static final String VIDEO_CODEC = "libx264";
   private static final String VIDEO_MP4 = "video/mp4";
   private static final String VIDEO_WEBM = "video/webm";

   private final ApplicationResourceService applicationResourceService;

   @Autowired
   public BrampTranscodingService(
       ApplicationResourceService applicationResourceService
   ) {
      this.applicationResourceService = applicationResourceService;
   }

   /**
    *
    * @param file
    * @return
    * @throws TranscodingException
    */
   public File transcodeStream(MediaFile file) throws TranscodingException {
      try {
         FFmpeg ffmpeg = new FFmpeg(applicationResourceService.getFileFromResources(FFMPEG_EXE).getPath());
         FFprobe ffprobe = new FFprobe(applicationResourceService.getFileFromResources(FFPROBE_EXE).getPath());

         File baseTempFolder = new File(TEMP_LOCATION);
         if (!baseTempFolder.exists()) {
            baseTempFolder.mkdirs();
         }

         // Make a folder for this file to store all the transcoded files
         // Create the file handle for the playlist file
         // Kick off an async task to being transcoding the file
         // Return the file
         File transcodedFolder = Paths.get(baseTempFolder.getAbsolutePath(), file.getHash()).toFile();
         if (!transcodedFolder.exists()) {
            transcodedFolder.mkdir();
         }

         File source = new File(file.getPath());
         File outputFormat = Paths.get(transcodedFolder.getAbsolutePath(), "out_%6d.ts").toFile();
         File targetPlaylistFile = Paths.get(transcodedFolder.getAbsolutePath(), "play_file.m3u8").toFile();

         if (targetPlaylistFile.exists()) {
            return targetPlaylistFile;
         }

         FFmpegProbeResult mediaProbeResult = ffprobe.probe(source.getAbsolutePath());
         String segmentTimes = getSegmentTimes(mediaProbeResult.getFormat().duration);

         // Build the ffmpeg command
         FFmpegBuilder builder = new FFmpegBuilder()
             .setInput(source.getAbsolutePath()) // Filename, or a FFmpegProbeResult
             .overrideOutputFiles(true) // Override the output if it exists
             .addOutput(outputFormat.getAbsolutePath()) // Filename for the destination

             // Set Options
             .setVideoCodec("h264") // Video using x264
             .setAudioCodec("aac") // using the aac codec
             .setPreset("ultrafast")
             .setFormat("ssegment")
             .addExtraArgs("-hls_flags", "delete_segments")
             .addExtraArgs("-segment_list", targetPlaylistFile.getAbsolutePath())
             .addExtraArgs("-segment_list_type", "hls")
             .addExtraArgs("-segment_times", segmentTimes)
            .addExtraArgs("-g", "30")
             .addExtraArgs("-map", "0")
             .addExtraArgs("-pix_fmt", "yuv420p") // To support iPhones, this needed to be set.

             .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
             .done();

         FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
         FFmpegJob job = executor.createJob(builder, new ProgressListener() {
            // Using the FFmpegProbeResult determine the duration of the input
            final double duration_ns = mediaProbeResult.getFormat().duration * TimeUnit.SECONDS.toNanos(1);

            @Override
            public void progress(Progress progress) {
               double percentage = progress.out_time_ns / duration_ns;

               // Print out interesting information about the progress
               System.out.println(String.format(
                   "[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx",
                   percentage * 100,
                   progress.status,
                   progress.frame,
                   FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
                   progress.fps.doubleValue(),
                   progress.speed
               ));
            }
         });

         Thread t = new Thread(() -> {
            job.run();
         });

         t.start();

         // Give a few seconds for the file to be generated. TODO: Look into doing this better
         try {
            Thread.sleep(5000L);
         } catch (InterruptedException ex) {
            Logger.getLogger(BrampTranscodingService.class.getName()).log(Level.SEVERE, null, ex);
         }

         return targetPlaylistFile;
      } catch (IOException ex) {
         throw new TranscodingException(ex);
      }
   }

   private String getSegmentTimes(double duration) {
      int segmentSize = 2;
      StringBuilder b = new StringBuilder("0");
      for (double i = segmentSize; i < duration; i += segmentSize) {
         b.append(",").append(i);
      }
      return b.toString();
   }

   @Override
   public synchronized File transcodeMediaFile(MediaFile file) throws TranscodingException {
      try {
         File folder = new File(TEMP_LOCATION);
         if (!folder.exists()) {
            folder.mkdirs();
         }
         File source = new File(file.getPath());
         File target = Paths.get(folder.getAbsolutePath(), file.getName()).toFile();
         FFmpeg ffmpeg = new FFmpeg(applicationResourceService.getFileFromResources(FFMPEG_EXE).getPath());
         FFprobe ffprobe = new FFprobe(applicationResourceService.getFileFromResources(FFPROBE_EXE).getPath());

         FFmpegProbeResult in = ffprobe.probe(source.getAbsolutePath());

         FFmpegBuilder builder = new FFmpegBuilder()
             .setInput(source.getAbsolutePath()) // Filename, or a FFmpegProbeResult
             .overrideOutputFiles(true) // Override the output if it exists

             .addOutput(target.getAbsolutePath()) // Filename for the destination
             .setFormat("mp4") // Format is inferred from filename, or can be set
             //.setTargetSize(250_000) // Aim for a 250KB file

             .disableSubtitle() // No subtiles

             //.setAudioChannels(1) // Mono audio
             //.setAudioCodec("aac") // using the aac codec
             //.setAudioSampleRate(48_000) // at 48KHz
             //.setAudioBitRate(32768) // at 32 kbit/s

             .setVideoCodec(VIDEO_CODEC) // Video using x264
             .setPreset("ultrafast")
             //.setVideoBitRate(in.getFormat().bit_rate > 3000 ? 3000 : in.getFormat().bit_rate)
             .setVideoQuality(5)
             .setVideoResolution(1920, 1080)
             .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
             .done();

         FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

//         // Run a one-pass encode
//         executor.createJob(builder).run();
         FFmpegJob job = executor.createJob(builder, new ProgressListener() {

            // Using the FFmpegProbeResult determine the duration of the input
            final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);

            @Override
            public void progress(Progress progress) {
               double percentage = progress.out_time_ns / duration_ns;

               // Print out interesting information about the progress
               System.out.println(String.format(
                   "[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx",
                   percentage * 100,
                   progress.status,
                   progress.frame,
                   FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
                   progress.fps.doubleValue(),
                   progress.speed
               ));
            }
         });

         job.run();

         // Or run a two-pass encode (which is better quality at the cost of being slower)
         //executor.createTwoPassJob(builder).run();
         return target;
      } catch (IOException ex) {
         throw new TranscodingException(ex);
      }
   }

   /**
    * Generates a default thumbnail from the passed in file.
    *
    * @param multimediaFile the video file that should be used to generate the thumbnail
    * @return The generated thumbnail file.
    * @throws IOException If there was a problem getting the FFMPEG or FFPROBE
    */
   @Override
   public File getDefaultThumbnail(MediaFile multimediaFile) throws IOException {
      File folder = new File(TEMP_LOCATION);
      if (!folder.exists()) {
         folder.mkdirs();
      }

      File mediaFileFile = new File(multimediaFile.getPath());

      File target = Paths.get(
          folder.getAbsolutePath(),
          multimediaFile.getName() + THUMBNAIL_FORMAT
      ).toFile();

      FFmpeg ffmpeg = new FFmpeg(applicationResourceService.getFileFromResources(FFMPEG_EXE).getPath());
      FFprobe ffprobe = new FFprobe(applicationResourceService.getFileFromResources(FFPROBE_EXE).getPath());

//      FFmpegProbeResult probeResult = ffprobe.probe("input.mp4");
//
//      FFmpegFormat format = probeResult.getFormat();
//      System.out.format("%nFile: '%s' ; Format: '%s' ; Duration: %.3fs", 
//         format.filename, 
//         format.format_long_name,
//         format.duration
//      );
//
//      FFmpegStream stream = probeResult.getStreams().get(0);
//      System.out.format("%nCodec: '%s' ; Width: %dpx ; Height: %dpx",
//         stream.codec_long_name,
//         stream.width,
//         stream.height
//      ); 
      FFmpegBuilder builder = new FFmpegBuilder()
          .setInput(mediaFileFile.getAbsolutePath())
          .addOutput(target.getAbsolutePath())
          .setFrames(1)
          .setVideoFilter("select='gte(n\\,10)',scale=200:-1")
          .done();

      FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

      try {
         // Run a one-pass encode
         executor.createJob(builder).run();
      } catch (Throwable ex) {
         // Catch anyting this can throw and return our own exception
         throw new IOException("Failed generating thumbnail", ex);
      }

      return target;
   }

   public MediaDetails getMediaDetails(MediaFile mediaFile) {

      try {
         File source = new File(mediaFile.getPath());
         FFprobe ffprobe = new FFprobe(applicationResourceService.getFileFromResources(FFPROBE_EXE).getPath());

         FFmpegProbeResult probeResult = ffprobe.probe(source.getAbsolutePath());

         var mediaFormat = probeResult.getFormat();
         var mediaDetails = MediaDetails.builder()
             .format(mediaFormat.format_long_name)
             .duration(mediaFormat.duration)
             .build();
         System.out.println(mediaDetails.getFormat());
         return mediaDetails;
      } catch (Exception ex) {
         return null;
      }
   }
}
