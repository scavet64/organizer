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
package com.scavettapps.organizer.media;

import java.util.List;

/**
 *
 * @author Vincent Scavetta
 */
public class MediaFileRequest {
   private String name;
   private List<Long> tags;
   private String mediaType;
   private boolean isFavorite;
   private boolean showIgnored;

   public MediaFileRequest() {
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List<Long> getTags() {
      return tags;
   }

   public void setTags(List<Long> tags) {
      this.tags = tags;
   }

   public String getMediaType() {
      return mediaType;
   }

   public void setMediaType(String typefilter) {
      this.mediaType = typefilter;
   }

   public boolean getIsFavorite() {
      return isFavorite;
   }

   public void setIsFavorite(boolean isFavorite) {
      this.isFavorite = isFavorite;
   }

   public boolean getShowIgnored() {
      return showIgnored;
   }

   public void setShowIgnored(boolean showIgnored) {
      this.showIgnored = showIgnored;
   }
}
