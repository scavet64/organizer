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
package com.scavettapps.organizer.specifications;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author Vincent Scavetta
 */
@Service
public class FilterSpecifications<E, T extends Comparable<T>> {

   private EnumMap<FilterOperation, Function<FilterCriteria<T>, Specification<E>>> map;

   public FilterSpecifications() {
      initSpecifications();
   }

   /**
    * Get the Generically specified specification for the passed in FilterOperation
    *
    * @param operation the operation that should be used
    * @return the Generically specified specification for the passed in
    *         FilterOperation
    */
   public Function<FilterCriteria<T>, Specification<E>> getSpecification(FilterOperation operation) {
      return map.get(operation);
   }

   /**
    * Forms the generic filter specifications for the operations
    * {@link FilterOperation}
    *
    * @return the generic filter specifications for the operations
    */
   private Map<FilterOperation, Function<FilterCriteria<T>, Specification<E>>> initSpecifications() {

      map = new EnumMap<>(FilterOperation.class);

      // Equal
      map.put(FilterOperation.EQUAL, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .equal(root.get(filterCriteria.getFieldName()), filterCriteria.getConvertedSingleValue()));

      map.put(FilterOperation.NOT_EQUAL, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .notEqual(root.get(filterCriteria.getFieldName()), filterCriteria.getConvertedSingleValue()));

      map.put(FilterOperation.GREATER_THAN,
          filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(
                  root.get(filterCriteria.getFieldName()), filterCriteria.getConvertedSingleValue()));

      map.put(FilterOperation.GREATER_THAN_OR_EQUAL_TO,
          filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                  root.get(filterCriteria.getFieldName()), filterCriteria.getConvertedSingleValue()));

      map.put(FilterOperation.LESS_THAN, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .lessThan(root.get(filterCriteria.getFieldName()), filterCriteria.getConvertedSingleValue()));

      map.put(FilterOperation.LESSTHAN_OR_EQUAL_TO,
          filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                  root.get(filterCriteria.getFieldName()), filterCriteria.getConvertedSingleValue()));

      map.put(FilterOperation.IN, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> root
            .get(filterCriteria.getFieldName()).in(filterCriteria.getConvertedValues()));

      map.put(FilterOperation.NOT_IN, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .not(root.get(filterCriteria.getFieldName()).in(filterCriteria.getConvertedSingleValue())));

      map.put(FilterOperation.BETWEEN,
          filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(
                  root.get(filterCriteria.getFieldName()), filterCriteria.getMinValue(),
                  filterCriteria.getMaxValue()));

      map.put(FilterOperation.CONTAINS, filterCriteria -> (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .like(root.get(filterCriteria.getFieldName()), "%" + filterCriteria.getConvertedSingleValue() + "%"));

      return map;
   }
}