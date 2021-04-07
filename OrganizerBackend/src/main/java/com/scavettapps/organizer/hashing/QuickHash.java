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
package com.scavettapps.organizer.hashing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.XXHashFactory;
import org.springframework.stereotype.Service;

/**
 * QuickHash is just an extension on the XXHash algorithm with a slight twist. Rather than hashing the entire file,
 * this algorithm will read up to 5 100mb chunks of the file. This drastically speeds up the hashing of very large files
 * and is relatively safe for media. This algorithm should not be used to verify a files integrity, but for quickly
 * checking if two files are the same, it does the job.
 *
 * @author Vincent Scavetta
 */
@Service("QuickHash")
public class QuickHash implements IHashService {

   private static final int NUMBER_OF_CHUNKS = 5;
   private static final int BYTE_BUFFER_SIZE = 100000000;

   @Override
   public String getHash(File file) throws IOException {
      int hash;
      try (FileInputStream in = new FileInputStream(file)) {
         XXHashFactory factory = XXHashFactory.fastestInstance();
         int seed = 0x9747b28c; // used to initialize the hash value, use whatever value you want, but always the same
         StreamingXXHash32 hash32 = factory.newStreamingHash32(seed);
         // Use 100 MB buffer
         byte[] buf = new byte[BYTE_BUFFER_SIZE];
         for (;;) {
            in.skip(file.length() / NUMBER_OF_CHUNKS);
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
