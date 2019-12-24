package com.scavettapps.organizer.scanner;

import com.scavettapps.organizer.core.entity.AbstractPersistableEntity;
import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "scan_locations", uniqueConstraints = @UniqueConstraint(columnNames = {"path"}))
public class ScanLocation extends AbstractPersistableEntity<Long> {
   
   @NotNull
   @NotEmpty
   @Column(name = "path")
   private String path;

   public ScanLocation(String path) {
      this.path = path;
   }

   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }

}
