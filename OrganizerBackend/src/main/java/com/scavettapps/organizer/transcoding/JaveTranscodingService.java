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

import com.scavettapps.organizer.media.MediaFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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
 * @author Vincent Scavetta
 */
@Service("Jave")
public class JaveTranscodingService implements ITranscodingService {

   @Override
   public File transcodeMediaFile(MediaFile file) throws TranscodingException {
      try {
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
      } catch (EncoderException ex) {
         throw new TranscodingException(ex);
      }
   }

   @Override
   public File getDefaultThumbnail(MediaFile file) throws IOException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

}
