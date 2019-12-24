//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.transcoding;

import com.scavettapps.organizer.media.MediaFile;
import java.io.File;
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
 * @author Gnostech Inc.
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

}
