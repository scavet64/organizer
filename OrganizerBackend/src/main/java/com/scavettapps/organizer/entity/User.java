package com.scavettapps.organizer.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = { "email", "username" }))
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
