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
package com.scavettapps.organizer.media.json;

/**
 *
 * @author Vincent Scavetta.
 */
public class SetMediaFavoriteRequest {
   private long mediaId;
   private boolean isFavorite;

   public SetMediaFavoriteRequest() {
   }

   public long getMediaId() {
      return mediaId;
   }

   public void setMediaId(long mediaId) {
      this.mediaId = mediaId;
   }

   public boolean getIsFavorite() {
      return isFavorite;
   }

   public void setIsFavorite(boolean isFavorite) {
      this.isFavorite = isFavorite;
   }
}
