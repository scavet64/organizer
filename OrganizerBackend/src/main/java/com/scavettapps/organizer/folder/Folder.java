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
package com.scavettapps.organizer.folder;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.scavettapps.organizer.core.entity.AbstractPersistableEntity;
import com.scavettapps.organizer.media.MediaFile;
import com.sun.istack.NotNull;

/**
 * @author Vincent Scavetta
 */
@Entity
@Table(name = "folders", uniqueConstraints = @UniqueConstraint(columnNames = {"path"}))
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@UUID")
public class Folder extends AbstractPersistableEntity<Long> {

   @NotNull
   @Column(name = "path")
   private String path;

   @NotNull
   @Column(name = "folderName")
   private String folderName;

   @NotNull
   @Column(name = "ignored")
   private boolean isIgnored;

   @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
   @OrderBy("UPPER(folderName) ASC")
   private Set<Folder> folders;

   @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
   private Set<MediaFile> files;

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
   private Folder folder;

   public Folder(String path) {
      super();
      this.path = path;
      this.folderName = new File(path).getName();
      this.folders = new HashSet<>();
      this.files = new HashSet<>();
      this.isIgnored = false;
   }

   public Folder() {
      super();
      this.folders = new HashSet<>();
      this.files = new HashSet<>();
      this.isIgnored = false;
   }

   /**
    * Adds a folder to this list of folders. This was done because of the way that the scanning
    * services was implemented. Eventually I would like to go back and fix it so that maybe this
    * code would be unneeded I can just use the built in functions of the set. Since the folder
    * object that is passed in to this
    *
    * @param folder
    * @return
    */
   public boolean addFolder(Folder folder) {

      if (this.folders.isEmpty()) {
         return this.folders.add(folder);
      } else {
         //Search for the folder using the path.
         Folder existingFolder = folders.stream()
             .filter(mediaFile -> (mediaFile.getPath().equalsIgnoreCase(folder.getPath())))
             .collect(Collectors.toList())
             .stream()
             .findFirst()
             .orElse(null);

         if (existingFolder == null) {
            return this.folders.add(folder);
         } else {
            // Need to replace this object with the new one
            this.folders.remove(existingFolder);
            return this.folders.add(folder);
         }
      }
   }

   public Folder getFolder(String path) {
      if (this.folders.isEmpty()) {
         return null;
      }

      return folders.stream()
          .filter(mediaFile -> (mediaFile.getPath().equalsIgnoreCase(path)))
          .collect(Collectors.toList()).get(0);
   }

   public boolean doesFileExist(MediaFile file) {
      return files.contains(file);
   }

   public boolean addFile(MediaFile file) {
      return files.add(file);
   }

   public MediaFile getFile(String hash) {
      if (this.files.isEmpty()) {
         return null;
      }

      return files.stream()
          .filter(mediaFile -> (mediaFile.getHash().equalsIgnoreCase(hash)))
          .collect(Collectors.toList()).get(0);
   }

   /**
    * This is meant for the JSON return to prevent stack overflow
    *
    * @return the path of the parent.
    */
   public String getParentPath() {
      String parentPath = null;
      if (folder != null) {
         parentPath = folder.getPath();
      }
      return parentPath;
   }


   public String getFolderName() {
      return folderName;
   }

   public void setFolderName(String folderName) {
      this.folderName = folderName;
   }

   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public Set<Folder> getFolders() {
      return folders;
   }

   public void setFolders(Set<Folder> folders) {
      this.folders = folders;
   }

   public Set<MediaFile> getFiles() {
      return files;
   }

   public void setFiles(Set<MediaFile> files) {
      this.files = files;
   }

   public Folder getFolder() {
      return folder;
   }

   public void setFolder(Folder folder) {
      this.folder = folder;
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
      result = prime * result + ((path == null) ? 0 : path.hashCode());
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
      Folder other = (Folder) obj;
      if (path == null) {
         if (other.path != null) {
            return false;
         }
      } else if (!path.equals(other.path)) {
         return false;
      }
      return true;
   }

}
