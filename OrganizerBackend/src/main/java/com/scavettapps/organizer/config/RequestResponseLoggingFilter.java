//Copyright (C) 2019 Gnostech, Inc - All Rights Reserved.
package com.scavettapps.organizer.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gnostech Inc.
 */
@Component
@Order(2)
public class RequestResponseLoggingFilter implements Filter {

   private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
         throws IOException, ServletException {

      HttpServletRequest req = (HttpServletRequest) request;
      LOGGER.debug("Logging Request  {} : {}", req.getMethod(), req.getRequestURI());
      chain.doFilter(request, response);
   }

   // other methods
}