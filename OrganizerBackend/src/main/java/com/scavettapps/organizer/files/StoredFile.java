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

import com.scavettapps.organizer.core.entity.AbstractPersistableEntity;
import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Vincent Scavetta
 */
@Entity
@Table(name = "stored_files", uniqueConstraints = @UniqueConstraint(columnNames = {"hash"}))
public class StoredFile extends AbstractPersistableEntity<Long>{
   
   @NotNull
   @Column(name = "hash")
   private String hash;

   @NotNull
   @Column(name = "name")
   private String name;

   @NotNull
   @Column(name = "size")
   private long size;
}
