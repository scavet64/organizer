package com.scavettapps.organizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scavettapps.organizer.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, String> {

}
