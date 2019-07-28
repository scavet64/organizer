package com.scavettapps.organizer.tag;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long> {

   Optional<Tag> findByName(String name);
   
   @Modifying
   @Query("delete from Tag t where t.id=?1")
   int deleteById(long id);
   
}
