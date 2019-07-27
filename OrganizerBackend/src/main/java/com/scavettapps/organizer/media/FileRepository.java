package com.scavettapps.organizer.media;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scavettapps.organizer.media.MediaFile;

@Repository
public interface FileRepository extends JpaRepository<MediaFile, String> {

   Optional<MediaFile> findByNameAndSize(String name, long size);

   Optional<MediaFile> findByHash(String hash);

//   List<MediaFile> findAllByFolder_Path(String folderPath);
   
   List<MediaFile> findAllByDuplicatePathsNotEmpty();

}
