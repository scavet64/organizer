//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.core;

import java.io.File;
import java.net.URL;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gnostech Inc.
 */
@Service
public class ApplicationResourceService {
   public File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }
}
