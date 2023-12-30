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

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Vincent Scavetta
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "username"}))
public class User extends AbstractAuditableEntity<User, Long> {

   @NotEmpty
   @Column(name = "username")
   private String username;

   @JsonIgnore
   @NotEmpty
   @Column(name = "password")
   private String password;

   @Column(name = "email", length = 50)
   @NotNull
   @Size(min = 4, max = 50)
   @Email
   private String email;

   @Column(name = "enabled")
   private boolean enabled;

   @Column(name = "token_expired")
   private boolean tokenExpired;

   @Column(name = "last_password_reset")
   @NotNull
   private Timestamp lastPasswordResetDate;

   public User(@NotEmpty String username, @NotEmpty String password,
           @NotNull @Size(min = 4, max = 50) @Email String email, boolean enabled, boolean tokenExpired,
           @NotNull Timestamp lastPasswordResetDate) {
      super();
      this.username = username;
      this.password = password;
      this.email = email;
      this.enabled = enabled;
      this.tokenExpired = tokenExpired;
      this.lastPasswordResetDate = lastPasswordResetDate;
   }

   public User() {
      super();
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public boolean isEnabled() {
      return enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public boolean isTokenExpired() {
      return tokenExpired;
   }

   public void setTokenExpired(boolean tokenExpired) {
      this.tokenExpired = tokenExpired;
   }

   public Timestamp getLastPasswordResetDate() {
      return lastPasswordResetDate;
   }

   public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
      this.lastPasswordResetDate = lastPasswordResetDate;
   }
}
