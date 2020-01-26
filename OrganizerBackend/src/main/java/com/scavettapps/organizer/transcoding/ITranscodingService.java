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

import com.scavettapps.organizer.files.StoredFile;
import com.scavettapps.organizer.media.MediaFile;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Vincent Scavetta
 */
public interface ITranscodingService {
   public File transcodeMediaFile(MediaFile file) throws TranscodingException;
   public File getDefaultThumbnail(MediaFile file) throws IOException;
}
