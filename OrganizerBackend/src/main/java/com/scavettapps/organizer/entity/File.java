package com.scavettapps.organizer.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sun.istack.NotNull;

@Entity
@Table(name = "files", uniqueConstraints = @UniqueConstraint(columnNames = {"hash"}))
public class File extends AbstractPersistableEntity<Long>{
	
	@NotNull
	@Column(name = "hash")
	private String hash;
	
	@NotNull
	@Column(name = "name")
	private String name;
	
	@ManyToMany
	private Set<Tag> tags;

	public File(String hash, String name) {
		super();
		this.hash = hash;
		this.name = name;
	}

	public File() {
		super();
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	
}
