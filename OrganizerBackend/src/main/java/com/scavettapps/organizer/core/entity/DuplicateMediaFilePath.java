/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author vstro
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
}
