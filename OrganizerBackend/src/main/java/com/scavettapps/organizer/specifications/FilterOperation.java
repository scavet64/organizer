package com.scavettapps.organizer.specifications;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FilterOperation {

   EQUAL("eq"),
   NOT_EQUAL("neq"),
   GREATER_THAN("gt"),
   GREATER_THAN_OR_EQUAL_TO("gte"),
   LESS_THAN("lt"),
   LESSTHAN_OR_EQUAL_TO("lte"),
   IN("in"),
   NOT_IN("nin"),
   BETWEEN("btn"),
   CONTAINS("like");

   private String value;

   /**
    * Default constructor
    * @param value The value
    */
   FilterOperation(String value) {
      this.value = value;
   }

   @Override
   @JsonValue
   public String toString() {
      return String.valueOf(value);
   }

   /**
    * Get the filter operation from the string value
    * @param value The string value to get the FilterOperation ENUM from
    * @return The FilterOperation Enum corresponding to the string value
    */
   public static FilterOperation fromValue(String value) {
      for (FilterOperation op : FilterOperation.values()) {

         // Case insensitive operation name
         if (String.valueOf(op.value).equalsIgnoreCase(value)) {
            return op;
         }
      }
      return null;
   }

}