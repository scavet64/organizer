/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scavettapps.organizer.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

// This configuration is needed to seperate Angular's HTML5 routing from routing to rest endpoints
@Configuration
public class WebConfig implements WebMvcConfigurer {
   @Override
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/**/*")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(new PathResourceResolver() {
               @Override
               protected Resource getResource(String resourcePath,
                     Resource location) throws IOException {
                  Resource requestedResource = location.createRelative(resourcePath);

                  // If it's a seperate resource that exists like an endpoint or an image, return
                  // that, otherwise return index.html
                  return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
                        : new ClassPathResource("/static/index.html");
               }
            });
   }
}
