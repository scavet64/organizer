package com.scavettapps.organizer.media;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {

   Optional<MediaFile> findByNameAndSize(String name, long size);

   Optional<MediaFile> findByHash(String hash);

//   List<MediaFile> findAllByFolder_Path(String folderPath);
   
   List<MediaFile> findAllByDuplicatePathsNotEmpty();
   
   Page<MediaFile> findAll(Specification<MediaFile> specs, Pageable page);

}
