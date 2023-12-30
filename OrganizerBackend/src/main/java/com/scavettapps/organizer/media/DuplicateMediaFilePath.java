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

import com.scavettapps.organizer.core.entity.AbstractPersistableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 *
 * @author Vincent Scavetta
 */
@Entity
@Table(name = "duplicate_media_file_path")
public class DuplicateMediaFilePath extends AbstractPersistableEntity<Long> {
   private String duplicatePath;

   public DuplicateMediaFilePath(String duplicatePath) {
      this.duplicatePath = duplicatePath;
   }

   public DuplicateMediaFilePath() {
   }

   public String getDuplicatePath() {
      return duplicatePath;
   }

   public void setDuplicatePath(String duplicatePath) {
      this.duplicatePath = duplicatePath;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      DuplicateMediaFilePath that = (DuplicateMediaFilePath) o;
      return duplicatePath.equals(that.duplicatePath);
   }

   @Override
   public int hashCode() {
      return Objects.hash(duplicatePath);
   }
}
