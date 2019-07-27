package com.scavettapps.organizer.tag;

import com.scavettapps.organizer.core.entity.AbstractAuditableEntity;
import com.scavettapps.organizer.core.entity.User;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Tags", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))

public class Tag extends AbstractAuditableEntity<User, Long> {

   @NotNull
   @NotEmpty
   private String name;

   private String description;

   @NotNull
   @NotEmpty
   private String backgroundColor;

   @NotNull
   @NotEmpty
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
