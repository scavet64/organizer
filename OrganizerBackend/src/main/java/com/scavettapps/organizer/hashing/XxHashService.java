package com.scavettapps.organizer.hashing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.XXHashFactory;

@Service(value = "xxhash")
public class XxHashService implements IHashService {

   @Override
   public String getHash(File file) throws IOException {
      int hash;
      try (FileInputStream in = new FileInputStream(file)) {
         XXHashFactory factory = XXHashFactory.fastestInstance();
         int seed = 0x9747b28c; // used to initialize the hash value, use whatever value you want, but always the same
         StreamingXXHash32 hash32 = factory.newStreamingHash32(seed);
         byte[] buf = new byte[100000000]; // for real-world usage, use a larger buffer, like 8192 bytes
         for (;;) {
            int read = in.read(buf);
            if (read == -1) {
               break;
            }
            hash32.update(buf, 0, read);
         }
         hash = hash32.getValue();
      }
      return hash + "";
   }

}
