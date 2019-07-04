package com.scavettapps.organizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scavettapps.organizer.entity.ScanLocation;

public interface ScanLocationRepository extends JpaRepository<ScanLocation, Long> {

}
