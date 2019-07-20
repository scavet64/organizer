package com.scavettapps.organizer.core.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import java.time.LocalDate;
import java.util.HashSet;

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
   @Column(name = "path")
   private String path;
   
   @NotNull
   @Column(name = "ignored")
   private boolean isIgnored;
   
   @NotNull
   @Column(name = "last_seen_date")
   private LocalDate lastSeenDate;

//   @JsonIgnore
//   @NotNull
//   @ManyToOne()
//   private Folder folder;

   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   private Set<Tag> tags = new HashSet<>();
   
   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   private Set<DuplicateMediaFilePath> duplicatePaths = new HashSet<>();

   public MediaFile(String hash, String name) {
      super();
      this.hash = hash;
      this.name = name;
      this.lastSeenDate = LocalDate.now();
      isIgnored = false;
   }

   public MediaFile(String hash, String name, long size, String path) {
      super();
      this.hash = hash;
      this.name = name;
      this.size = size;
      this.path = path;
      this.lastSeenDate = LocalDate.now();
      isIgnored = false;
   }

   public MediaFile() {
      super();
      this.lastSeenDate = LocalDate.now();
      isIgnored = false;
   }
   
   public boolean addDuplicatePath(DuplicateMediaFilePath dupe) {
      return duplicatePaths.add(dupe);
   }
   
   public void updateLastSeen() {
      this.lastSeenDate = LocalDate.now();
   }

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

//   public Folder getFolder() {
//      return folder;
//   }
//
//   public void setFolder(Folder folder) {
//      this.folder = folder;
//   }

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

   public boolean isIsIgnored() {
      return isIgnored;
   }

   public void setIsIgnored(boolean isIgnored) {
      this.isIgnored = isIgnored;
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
