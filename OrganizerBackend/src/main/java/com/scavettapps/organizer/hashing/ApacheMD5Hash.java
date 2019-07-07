package com.scavettapps.organizer.hashing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service("apacheMD5")
public class ApacheMD5Hash implements IHashService {

   @Override
   public String getHash(File file) throws IOException {
      try (InputStream is = Files.newInputStream(Paths.get(file.getPath()))) {
         return org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
      }
   }

}
