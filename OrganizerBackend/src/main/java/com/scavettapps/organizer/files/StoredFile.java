//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
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
 * @author Gnostech Inc.
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
