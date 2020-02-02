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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;


/**
 * @author Vincent Scavetta
 */
@Service
public class Converters {

   private Map<Class<?>, Function<String, ? extends Comparable<?>>> map = new HashMap<>();

   /**
    * Initializes the map of classes to functions
    */
   @PostConstruct
   public void init() {
      map.put(String.class, s -> s);
      map.put(Boolean.class, Boolean::valueOf);
      map.put(Long.class, Long::valueOf);
      map.put(Integer.class, Integer::valueOf);
      map.put(ChronoLocalDate.class, Converters::timestampParse);
      // Add more converters
   }

   /**
    * Get the Converter function for the passed in class
    * @param classObj the class object
    * @return Converter for that class
    */
   @SuppressWarnings("unchecked")
   public <T extends Comparable<T>> Function<String, T> getFunction(Class<?> classObj) {
      return (Function<String, T>) map.get(classObj);
   }

   /**
    * Parses the passed in date string as a {@link Timestamp}
    *
    * @param date The string in a format of "yyyy-MM-dd"
    * @return The Timestamp object parsed from the string
    */
   private static Timestamp timestampParse(String date) {
      try {
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
         Date parsedDate = format.parse(date);
         Timestamp stamp = new Timestamp(parsedDate.getTime());
         return stamp;
      } catch (Exception ex) {
         // Catch the exception and just move on.
         return null;
      }

   }

}