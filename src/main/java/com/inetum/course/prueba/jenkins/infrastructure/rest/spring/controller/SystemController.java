package com.inetum.course.prueba.jenkins.infrastructure.rest.spring.controller;

import com.inetum.course.prueba.jenkins.infrastructure.rest.spring.SystemProperties;
import com.inetum.course.prueba.jenkins.infrastructure.rest.spring.controller.api.SystemApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemController.
 */
@RestController
public class SystemController implements SystemApi
{
    @Override
    public ResponseEntity<Object> getSystemProperties()
    {
        return new ResponseEntity<>(SystemProperties.getProperties(), HttpStatus.OK);
    }
}
