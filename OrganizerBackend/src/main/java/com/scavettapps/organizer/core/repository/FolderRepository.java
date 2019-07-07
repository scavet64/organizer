package com.scavettapps.organizer.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scavettapps.organizer.core.entity.Folder;
import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

   Optional<Folder> findByPath(String name);
   
   List<Folder> findAllByFolderNull();
   
   List<Folder> findAllByFolder_Id(long id);

}
