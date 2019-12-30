/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.media;

import com.scavettapps.organizer.tag.Tag;
import java.util.List;

/**
 *
 * @author vstro
 */
public class MediaFileRequest {
   private String name;
   private List<Long> tags;

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
   
   
}
