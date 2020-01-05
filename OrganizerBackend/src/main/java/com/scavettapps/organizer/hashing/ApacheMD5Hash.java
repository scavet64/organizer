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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

/**
 * @author Vincent Scavetta
 */
@Service("apacheMD5")
public class ApacheMD5Hash implements IHashService {

   @Override
   public String getHash(File file) throws IOException {
      try (InputStream is = Files.newInputStream(Paths.get(file.getPath()))) {
         return org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
      }
   }

}
