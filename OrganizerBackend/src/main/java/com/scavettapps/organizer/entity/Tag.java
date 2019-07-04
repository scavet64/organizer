package com.scavettapps.organizer.entity;

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
	
	private Date dateModified;
	
}
