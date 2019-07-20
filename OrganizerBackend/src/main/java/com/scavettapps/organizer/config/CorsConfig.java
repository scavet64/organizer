/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *
 * @author vstro
 */
@Configuration
public class CorsConfig {
   
   /** 
    * This Bean will filter requests to allow for requests outside of the origin. This is useful for 
    * running the angular test server
    * @return The FilterRegistrationBean of type cores filter
    */
   @Bean
   public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8080", "*"));
      config.setAllowedMethods(Collections.singletonList("*"));
      config.setAllowedHeaders(Collections.singletonList("*"));
      source.registerCorsConfiguration("/**", config);
      FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
      bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
      return bean;
   }
}
