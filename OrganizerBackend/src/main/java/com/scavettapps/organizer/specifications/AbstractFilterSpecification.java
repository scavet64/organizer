package com.scavettapps.organizer.specifications;

import java.time.chrono.ChronoLocalDate;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;


public class AbstractFilterSpecification<TYPE>{

   /**
    * Converter Functions
    */
   @Autowired
   private Converters converters;

   private final String wildcard = "%";

   /**
    * {@link FilterSpecifications} for Entity {@link TYPE} and Field type
    * {@link ChronoLocalDate} (LocalDate)
    */
   @Autowired
   private FilterSpecifications<TYPE, ChronoLocalDate> dateTypeSpecifications;

   /**
    * {@link FilterSpecifications} for Entity {@link TYPE} and Field type
    * {@link String}
    */
   @Autowired
   private FilterSpecifications<TYPE, String> stringTypeSpecifications;

   /**
    * {@link FilterSpecifications} for Entity {@link TYPE} and Field type
    * {@link Integer}
    *
    */
   @Autowired
   private FilterSpecifications<TYPE, Integer> integerTypeSpecifications;

   /**
    * {@link FilterSpecifications} for Entity {@link TYPE} and Field type
    * {@link Long}
    */
   @Autowired
   private FilterSpecifications<TYPE, Long> longTypeSpecifications;

   protected String containsLowerCase(String searchField) {
      return wildcard + searchField.toLowerCase() + wildcard;
   }

   /**
    * Returns the Specification for Entity {@link TYPE} for the given
    * fieldName and filterValue for the field type Date
    *
    * @param fieldName   The name of the field that will be filtered on
    * @param filterValue the value that should be used in the filter. This value
    *                    must have a function attached to it
    * @return the Specification for Entity {@link TYPE} for the given
    *         fieldName and filterValue for the field type Date
    */
   public Specification<TYPE> getDateTypeSpecification(String fieldName, String filterValue) {
      return getSpecification(fieldName, filterValue, converters.getFunction(ChronoLocalDate.class),
            dateTypeSpecifications);
   }

   /**
    * Returns the Specification for Entity {@link TYPE} for the given
    * fieldName and filterValue for the field type String
    *
    * @param fieldName   The name of the field that will be filtered on
    * @param filterValue the value that should be used in the filter. This value
    *                    must have a function attached to it
    * @return the Specification for Entity {@link TYPE} for the given
    *         fieldName and filterValue for the field type String
    */
   public Specification<TYPE> getStringTypeSpecification(String fieldName, String filterValue) {
      return getSpecification(fieldName, filterValue, converters.getFunction(String.class), stringTypeSpecifications);
   }

   /**
    * Returns the Specification for Entity {@link TYPE} for the given
    * fieldName and filterValue for the field type Long
    *
    * @param fieldName   The name of the field that will be filtered on
    * @param filterValue the value that should be used in the filter. This value
    *                    must have a function attached to it
    * @return Returns the Specification for Entity {@link TYPE} for the
    *         given fieldName and filterValue for the field type Long
    */
   public Specification<TYPE> getLongTypeSpecification(String fieldName, String filterValue) {
      return getSpecification(fieldName, filterValue, converters.getFunction(Long.class), longTypeSpecifications);
   }

   /**
    * Returns the Specification for Entity {@link TYPE} for the given
    * fieldName and filterValue for the field type Integer
    *
    * @param fieldName   The name of the field that will be filtered on
    * @param filterValue the value that should be used in the filter. This value
    *                    must have a function attached to it
    * @return the Specification for Entity {@link TYPE} for the given
    *         fieldName and filterValue for the field type Integer
    */
   public Specification<TYPE> getIntegerTypeSpecification(String fieldName, String filterValue) {
      return getSpecification(fieldName, filterValue, converters.getFunction(Integer.class), integerTypeSpecifications);
   }

   /**
    * Generic method to return {@link Specification} for Entity
    * {@link TYPE}
    *
    * @param fieldName      The name of the field that will be filtered on
    * @param filterValue    the value that should be used in the filter. This value
    *                       must have a function attached to it
    * @param converter      The converter function that will be used to convert the
    *                       string filter value into the specified type
    * @param specifications Specifications
    * @return {@link Specification} for Entity {@link TYPE}
    */
   private <T extends Comparable<T>> Specification<TYPE> getSpecification(String fieldName,
         String filterValue, Function<String, T> converter, FilterSpecifications<TYPE, T> specifications) {

      if (StringUtils.isNotBlank(filterValue)) {

         // Form the filter Criteria
         FilterCriteria<T> criteria = new FilterCriteria<>(fieldName, filterValue, converter);
         return specifications.getSpecification(criteria.getOperation()).apply(criteria);
      }

      return null;
   }

}