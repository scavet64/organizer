package com.scavettapps.organizer.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Tags")
public class Tag extends AbstractAuditableEntity<User, Long> {

   private String name;

   private String description;

   private String backgroundColor;

   private String textColor;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getBackgroundColor() {
      return backgroundColor;
   }

   public void setBackgroundColor(String backgroundColor) {
      this.backgroundColor = backgroundColor;
   }

   public String getTextColor() {
      return textColor;
   }

   public void setTextColor(String textColor) {
      this.textColor = textColor;
   }
}
