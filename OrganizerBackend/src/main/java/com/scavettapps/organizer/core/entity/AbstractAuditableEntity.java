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
package com.scavettapps.organizer.core.entity;

import java.time.LocalDate;

import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Vincent Scavetta
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity<U, ID>
        extends AbstractPersistableEntity<ID> {

   @CreatedDate
   LocalDate createdDate = LocalDate.now();

   @LastModifiedDate
   LocalDate lastModifiedDate = LocalDate.now();

   @CreatedBy
   @ManyToOne
   @JoinColumn(name = "created_by")
   U createdBy;

   @LastModifiedBy
   @ManyToOne
   @JoinColumn(name = "last_modified_by")
   U lastModifiedBy;

   /**
    * @return the createdDate
    */
   public LocalDate getCreatedDate() {
      return createdDate;
   }

   /**
    * @param createdDate the createdDate to set
    */
   public void setCreatedDate(LocalDate createdDate) {
      this.createdDate = createdDate;
   }

   /**
    * @return the lastModifiedDate
    */
   public LocalDate getLastModifiedDate() {
      return lastModifiedDate;
   }

   /**
    * @param lastModifiedDate the lastModifiedDate to set
    */
   public void setLastModifiedDate(LocalDate lastModifiedDate) {
      this.lastModifiedDate = lastModifiedDate;
   }

   /**
    * @return the createdBy
    */
   public U getCreatedBy() {
      return createdBy;
   }

   /**
    * @param createdBy the createdBy to set
    */
   public void setCreatedBy(U createdBy) {
      this.createdBy = createdBy;
   }

   /**
    * @return the lastModifiedBy
    */
   public U getLastModifiedBy() {
      return lastModifiedBy;
   }

   /**
    * @param lastModifiedBy the lastModifiedBy to set
    */
   public void setLastModifiedBy(U lastModifiedBy) {
      this.lastModifiedBy = lastModifiedBy;
   }

}
