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
package com.scavettapps.organizer.files;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.scavettapps.organizer.core.entity.AbstractPersistableEntity;

/**
 *
 * @author Vincent Scavetta
 */
@Entity
@Table(name = "stored_files")
public class StoredFile extends AbstractPersistableEntity<Long>{
   
   @NotNull
   @Column(name = "hash")
   private String hash;
   
   @NotNull
   @Column(name = "path")
   private String path;

   @NotNull
   @Column(name = "name")
   private String name;

   @NotNull
   @Column(name = "size")
   private long size;

   public StoredFile() {
   }

   public StoredFile(String hash, String path, String name, long size) {
      this.hash = hash;
      this.path = path;
      this.name = name;
      this.size = size;
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

   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public void setName(String name) {
      this.name = name;
   }

   public long getSize() {
      return size;
   }

   public void setSize(long size) {
      this.size = size;
   }
}
