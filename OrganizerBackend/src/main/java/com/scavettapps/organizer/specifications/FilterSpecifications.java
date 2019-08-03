package com.scavettapps.organizer.specifications;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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