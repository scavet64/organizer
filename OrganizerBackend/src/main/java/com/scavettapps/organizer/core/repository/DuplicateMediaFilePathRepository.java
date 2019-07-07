/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.core.repository;

import com.scavettapps.organizer.core.entity.DuplicateMediaFilePath;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author vstro
 */
public interface DuplicateMediaFilePathRepository 
        extends JpaRepository<DuplicateMediaFilePath, String> {
   
}
