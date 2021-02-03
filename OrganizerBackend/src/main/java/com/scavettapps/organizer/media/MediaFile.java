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

import com.scavettapps.organizer.folder.Folder;
import com.scavettapps.organizer.tag.Tag;
import java.util.Set;

import javax.persistence.*;

import com.scavettapps.organizer.core.entity.AbstractPersistableEntity;
import com.scavettapps.organizer.files.StoredFile;
import com.sun.istack.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import javax.persistence.OrderBy;

/**
 * @author Vincent Scavetta
 */
@Entity
@Table(name = "files", uniqueConstraints = @UniqueConstraint(columnNames = {"hash"}))
public class MediaFile extends AbstractPersistableEntity<Long> {

   @NotNull
   @Column(name = "hash")
   private String hash;

   @NotNull
   @Column(name = "name")
   private String name;

   @NotNull
   @Column(name = "size")
   private long size;

   @NotNull
   @Lob
   @Column(name = "path")
   private String path;

   @NotNull
   @Column(name = "mimetype")
   private String mimetype;

   @Column(name = "views")
   private long views = 0;

   @NotNull
   @Column(name = "ignored")
   private boolean isIgnored;

   @NotNull
   @Column(name = "date_added")
   private Instant dateAdded;

   @NotNull
   @Column(name = "last_seen_date")
   private LocalDate lastSeenDate;

   @Column(name = "date_created")
   private Instant dateCreated;

   @Column(name = "lastModified")
   private Instant lastModified;

   @NotNull
   @Column(name = "is_favorite")
   private boolean isFavorite;

   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @OrderBy("UPPER(name) ASC")
   private Set<Tag> tags = new HashSet<>();

   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   private Set<DuplicateMediaFilePath> duplicatePaths = new HashSet<>();

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   private StoredFile thumbnail;

   public MediaFile(String hash, String name) {
      super();
      this.hash = hash;
      this.name = name;
      this.lastSeenDate = LocalDate.now();
      this.dateAdded = Instant.now();
      this.views = 0;
      this.isIgnored = false;
   }

   public MediaFile(
       String hash,
       String name,
       long size,
       String path,
       String mimetype,
       Instant dateCreated,
       Instant lastModified
   ) {
      this.hash = hash;
      this.name = name;
      this.size = size;
      this.path = path;
      this.mimetype = mimetype;
      this.dateCreated = dateCreated;
      this.lastModified = lastModified;

      this.isIgnored = false;
      this.isFavorite = false;
      this.lastSeenDate = LocalDate.now();
      this.dateAdded = Instant.now();
   }

   public MediaFile() {
      super();
      this.lastSeenDate = LocalDate.now();
      this.dateAdded = Instant.now();
      this.views = 0;
      this.isIgnored = false;
   }

   public long incrementViews() {
      return this.views++;
   }

   public boolean addTag(Tag tag) {
      return this.tags.add(tag);
   }

   public boolean addDuplicatePath(DuplicateMediaFilePath dupe) {
      return duplicatePaths.add(dupe);
   }

   public void updateLastSeen() {
      this.lastSeenDate = LocalDate.now();
   }

   //////////////////// Getters and Setters ////////////////////
   public String getHash() {
      return hash;
   }

   public void setHash(String hash) {
      this.hash = hash;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Set<Tag> getTags() {
      return tags;
   }

   public void setTags(Set<Tag> tags) {
      this.tags = tags;
   }

   public long getSize() {
      return size;
   }

   public void setSize(long size) {
      this.size = size;
   }

   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public LocalDate getLastSeenDate() {
      return lastSeenDate;
   }

   public void setLastSeenDate(LocalDate lastSeenDate) {
      this.lastSeenDate = lastSeenDate;
   }

   public Set<DuplicateMediaFilePath> getDuplicatePaths() {
      return duplicatePaths;
   }

   public void setDuplicatePaths(Set<DuplicateMediaFilePath> duplicatePaths) {
      this.duplicatePaths = duplicatePaths;
   }

   public String getMimetype() {
      return mimetype;
   }

   public void setMimetype(String mimetype) {
      this.mimetype = mimetype;
   }

   public long getViews() {
      return views;
   }

   public void setViews(long views) {
      this.views = views;
   }

   public Instant getDateAdded() {
      return dateAdded;
   }

   public void setDateAdded(Instant dateAdded) {
      this.dateAdded = dateAdded;
   }

   public boolean isIsIgnored() {
      return isIgnored;
   }

   public void setIsIgnored(boolean isIgnored) {
      this.isIgnored = isIgnored;
   }

   public StoredFile getThumbnail() {
      return thumbnail;
   }

   public void setThumbnail(StoredFile thumbnail) {
      this.thumbnail = thumbnail;
   }

   public Instant getDateCreated() {
      return dateCreated;
   }

   public void setDateCreated(Instant dateCreated) {
      this.dateCreated = dateCreated;
   }

   public Instant getLastModified() {
      return lastModified;
   }

   public void setLastModified(Instant lastModified) {
      this.lastModified = lastModified;
   }

   public boolean isIsFavorite() {
      return isFavorite;
   }

   public void setIsFavorite(boolean isFavorite) {
      this.isFavorite = isFavorite;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((hash == null) ? 0 : hash.hashCode());
      return result;
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
      MediaFile other = (MediaFile) obj;
      if (hash == null) {
         if (other.hash != null) {
            return false;
         }
      } else if (!hash.equals(other.hash)) {
         return false;
      }
      return true;
   }

}
