/**
 * Copyright 2019 Vincent Scavetta
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scavettapps.organizer.folder;

import java.util.Optional;

import com.scavettapps.organizer.media.MediaFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.scavettapps.organizer.folder.Folder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Vincent Scavetta
 */
public interface FolderRepository extends JpaRepository<Folder, Long> {

   Optional<Folder> findByPath(String name);
   
   List<Folder> findAllByFolderNull();
   
   List<Folder> findAllByFolder_Id(long id);

   @Query("select mediafiles from Folder f inner join f.files mediafiles where f.id = :id ORDER BY mediafiles.name")
   Page<MediaFile> findAllMediaForFolder(@Param("id") long id, Pageable page);

   Optional<Folder> findByFilesContaining(MediaFile file);
}
