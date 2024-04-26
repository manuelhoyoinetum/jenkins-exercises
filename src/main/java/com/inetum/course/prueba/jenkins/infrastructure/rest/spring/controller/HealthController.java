package com.inetum.course.prueba.jenkins.infrastructure.rest.spring.controller;

import com.inetum.course.prueba.jenkins.infrastructure.rest.spring.controller.api.HealthApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * HealthController.
 */
@RestController
public class HealthController implements HealthApi
{
    @Override
    public ResponseEntity<Void> health()
    {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
