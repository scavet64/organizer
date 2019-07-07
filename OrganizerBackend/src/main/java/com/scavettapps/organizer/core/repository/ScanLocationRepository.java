package com.scavettapps.organizer.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scavettapps.organizer.core.entity.ScanLocation;

public interface ScanLocationRepository extends JpaRepository<ScanLocation, Long> {

}
