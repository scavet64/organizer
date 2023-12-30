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
package com.scavettapps.organizer.scanner;

import com.scavettapps.organizer.core.entity.AbstractPersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;


/**
 * @author Vincent Scavetta
 */
@Entity
@Table(name = "scan_locations", uniqueConstraints = @UniqueConstraint(columnNames = {"path"}))
public class ScanLocation extends AbstractPersistableEntity<Long> {
   
   @NotNull
   @NotEmpty
   @Column(name = "path")
   private String path;
   
   @Column(name = "last_scan")
   private Instant lastScan;

   public ScanLocation() {
   }

   public ScanLocation(String path) {
      this.path = path;
   }

   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public Instant getLastScan() {
      return lastScan;
   }

   public void setLastScan(Instant lastScan) {
      this.lastScan = lastScan;
   }
   
   public void setLastScanNow() {
      this.lastScan = Instant.now();
   }
}
