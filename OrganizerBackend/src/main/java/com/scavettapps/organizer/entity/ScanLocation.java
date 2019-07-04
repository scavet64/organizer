package com.scavettapps.organizer.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scan_locations")
public class ScanLocation extends AbstractPersistableEntity<Long> {

	private String path;
	
}
