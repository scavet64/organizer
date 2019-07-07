package com.scavettapps.organizer.core.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity<U, ID>
        extends AbstractPersistableEntity<ID>
        implements Serializable {

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
