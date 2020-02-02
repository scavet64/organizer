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
package com.scavettapps.organizer.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scavettapps.organizer.core.entity.AbstractAuditableEntity;
import com.scavettapps.organizer.core.entity.User;
import com.scavettapps.organizer.media.MediaFile;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Tags")

/**
 * @author Vincent Scavetta
 */
public class Tag extends AbstractAuditableEntity<User, Long> {

   @NotNull
   @NotEmpty
   @Column(unique=true)
   private String name;

   private String description;

   @NotNull
   @NotEmpty
   private String backgroundColor;

   @NotNull
   @NotEmpty
   private String textColor;
   
   @JsonIgnore
   @ManyToMany(mappedBy = "tags")
   private Collection<MediaFile> mediaFiles;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getBackgroundColor() {
      return backgroundColor;
   }

   public void setBackgroundColor(String backgroundColor) {
      this.backgroundColor = backgroundColor;
   }

   public String getTextColor() {
      return textColor;
   }

   public void setTextColor(String textColor) {
      this.textColor = textColor;
   }
   
   public int getNumberOfTaggedMedia() {
      return mediaFiles != null ? mediaFiles.size() : 0;
   }
}
