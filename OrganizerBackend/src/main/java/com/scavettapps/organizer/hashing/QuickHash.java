/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.hashing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.XXHashFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author vstro
 */
@Service("QuickHash")
public class QuickHash implements IHashService {

   @Override
   public String getHash(File file) throws IOException {
      int hash;
      try (FileInputStream in = new FileInputStream(file)) {
         XXHashFactory factory = XXHashFactory.fastestInstance();
         int seed = 0x9747b28c; // used to initialize the hash value, use whatever value you want, but always the same
         StreamingXXHash32 hash32 = factory.newStreamingHash32(seed);
         byte[] buf = new byte[100000000]; // for real-world usage, use a larger buffer, like 8192 bytes
         for (;;) {
            in.skip(file.length() / 5);
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
