package com.scavettapps.organizer.core;

import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
public @interface OrganizerRestController {
}
