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
package com.scavettapps.organizer.media;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Vincent Scavetta
 */
@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {

   Optional<MediaFile> findByNameAndSize(String name, long size);

   Optional<MediaFile> findByHash(String hash);

//   List<MediaFile> findAllByFolder_Path(String folderPath);
   
   List<MediaFile> findAllByDuplicatePathsNotEmpty();
   
   Page<MediaFile> findAll(Specification<MediaFile> specs, Pageable page);

}
