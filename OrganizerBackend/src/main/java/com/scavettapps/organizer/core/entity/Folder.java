package com.scavettapps.organizer.core.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sun.istack.NotNull;
import java.util.stream.Collectors;

@Entity
@Table(name = "folders", uniqueConstraints = @UniqueConstraint(columnNames = {"path"}))
public class Folder extends AbstractPersistableEntity<Long> {

   @NotNull
   @Column(name = "path")
   private String path;

   @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
   private Set<Folder> folders;

   @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
   private Set<MediaFile> files;

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
   @JsonIgnore
   private Folder folder;

   public Folder(String path) {
      super();
      this.path = path;
      this.folders = new HashSet<Folder>();
      this.files = new HashSet<MediaFile>();
   }

   public Folder() {
      super();
      this.folders = new HashSet<Folder>();
      this.files = new HashSet<MediaFile>();
   }
   
   public boolean doesFileExist(MediaFile file) {
      return files.contains(file);
   }
   
   public boolean addFile(MediaFile file) {
      return files.add(file);
   }
   
   public MediaFile getFile(String hash) {
      return files.stream()
              .filter(mediaFile -> (mediaFile.getHash().equalsIgnoreCase(hash)))
              .collect(Collectors.toList()).get(0);
   }
   
   /**
    * This is meant for the JSON return to prevent stack overflow
    * @return the path of the parent.
    */
   public String getParentPath() {
      String parentPath = null;
      if (folder != null) {
         parentPath = folder.getPath();
      }
      return parentPath;
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
