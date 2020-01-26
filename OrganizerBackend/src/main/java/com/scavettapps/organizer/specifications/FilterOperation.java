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

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Vincent Scavetta
 */
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