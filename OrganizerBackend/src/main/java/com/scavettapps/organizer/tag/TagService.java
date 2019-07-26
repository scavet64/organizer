package com.scavettapps.organizer.tag;

import com.scavettapps.organizer.core.entity.Tag;
import com.scavettapps.organizer.core.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Collection<Tag> findAllTags() {
        return tagRepository.findAll();
    }

}
