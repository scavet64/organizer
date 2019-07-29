/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.media;

import com.sun.istack.NotNull;
import java.util.Collection;
import java.util.Objects;

/**
 *
 * @author vstro
 */
public class AddTagRequest {
   
   @NotNull
   private Long mediaId;
   @NotNull
   private Collection<Long> tagIds;

   public AddTagRequest(long mediaId, Collection<Long> tagId) {
      this.mediaId = mediaId;
      this.tagIds = tagId;
   }

   public AddTagRequest() {
   }

   public long getMediaId() {
      return mediaId;
   }

   public void setMediaId(long mediaId) {
      this.mediaId = mediaId;
   }

   public Collection<Long> getTagIds() {
      return tagIds;
   }

   public void setTagIds(Collection<Long> tagIds) {
      this.tagIds = tagIds;
   }

   @Override
   public int hashCode() {
      int hash = 5;
      hash = 11 * hash + (int) (this.mediaId ^ (this.mediaId >>> 32));
      hash = 11 * hash + Objects.hashCode(this.tagIds);
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final AddTagRequest other = (AddTagRequest) obj;
      if (this.mediaId != other.mediaId) {
         return false;
      }
      if (!Objects.equals(this.tagIds, other.tagIds)) {
         return false;
      }
      return true;
   }
}
