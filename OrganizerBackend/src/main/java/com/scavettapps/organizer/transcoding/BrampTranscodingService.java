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
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vincent Scavetta
 */
@Service("bramp")
public class BrampTranscodingService implements ITranscodingService {

   private final ApplicationResourceService applicationResourceService;

   @Autowired
   public BrampTranscodingService(
       ApplicationResourceService applicationResourceService
   ) {
      this.applicationResourceService = applicationResourceService;
   }

   @Override
   public File transcodeMediaFile(MediaFile file) throws TranscodingException {
      try {
         File folder = new File("C:/temp/");
         if (!folder.exists()) {
            folder.mkdirs();
         }
         File source = new File(file.getPath());
         File target = Paths.get(folder.getAbsolutePath(), file.getName()).toFile();
         FFmpeg ffmpeg = new FFmpeg(applicationResourceService.getFileFromResources("ffmpeg.exe").getPath());
         FFprobe ffprobe = new FFprobe(applicationResourceService.getFileFromResources("ffprobe.exe").getPath());

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
      } catch (IOException ex) {
         throw new TranscodingException(ex);
      }
   }

}
