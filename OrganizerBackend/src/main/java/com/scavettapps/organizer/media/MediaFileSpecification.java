/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.media;

import com.scavettapps.organizer.specifications.AbstractFilterSpecification;
import com.scavettapps.organizer.tag.Tag;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author vstro
 */
@Service
public class MediaFileSpecification extends AbstractFilterSpecification<MediaFile> {
   public static final String NULL = "Null";
   
   public Specification<MediaFile> getTagAttributeContains(String attribute, String value) {
      return (root, query, cb) ->
         {
            if(value == null) {
                return null;
            }
 
            Join<MediaFile, Tag> tags = root.join("tags", JoinType.INNER);
 
            return cb.like(
                cb.lower(tags.get(attribute)),
                containsLowerCase(value)
            );
         };
   }
   
   public Specification<MediaFile> getTagAttributeEquals(String attribute, Object value) {
      return (root, query, cb) ->
         {
            if(value == null) {
                return null;
            }
 
            Join<MediaFile, Tag> tags = root.join("tags", JoinType.INNER);
 
            return cb.equal(
                tags.get(attribute),
                value
            );
         };
   }
}
