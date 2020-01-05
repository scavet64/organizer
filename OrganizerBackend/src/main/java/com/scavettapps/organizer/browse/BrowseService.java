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
package com.scavettapps.organizer.browse;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vincent Scavetta
 */
@Service
public class BrowseService {
   
   public List<String> getDirectories() {
      File[] roots = File.listRoots();
      return filesToPathArray(roots);
   }
   
   public List<String> getDirectories(String directory, boolean showHidden) throws IOException {
      
      File directoryToSearch = new File(directory);
      if (!directoryToSearch.exists()) {
         throw new FileNotFoundException("Path does not exist:" + directory);
      }
      if (!directoryToSearch.isDirectory()) {
         throw new NotDirectoryException("Path must point to a directory");
      }
      
      FileFilter directoryFilter = (File file) 
          ->  file.isDirectory() && (!file.isHidden() || showHidden) && file.canRead();
      
      File[] childDirectories = directoryToSearch.listFiles(directoryFilter);
      return filesToPathArray(childDirectories);
   }
   
   private List<String> filesToPathArray(File[] files) {
      List<String> browsePaths = new ArrayList<>();
      for(File root: files) {
         browsePaths.add(root.getAbsolutePath());
      }
      return browsePaths;
   }
   
}
