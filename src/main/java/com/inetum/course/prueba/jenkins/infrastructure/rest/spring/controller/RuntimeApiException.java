package com.inetum.course.prueba.jenkins.infrastructure.rest.spring.controller;

import com.inetum.course.prueba.jenkins.infrastructure.rest.spring.HttpError;

/**
 * RuntimeApiException.
 * Es la forma de trasladar excepciones de negocio a excepciones de API.
 */
public class RuntimeApiException extends RuntimeException
{
    /**
     * SaidbHttpError object.
     */
    public final HttpError httpError;

    /**
     * Description.
     */
    public final String description;

    /**
     * Data object.
     */
    public final Object data;

    /**
     * Constructor.
     *
     * @param httpError   Error HTTP relacionado con la excepción de negocio
     * @param cause       Throwable Excepción de negocio
     * @param description Description adicional del error
     * @param data        Datos adicionales del error
     */
    public RuntimeApiException(
            HttpError httpError,
            Throwable cause,
            String description,
            Object data
    )
    {
        super(description, cause);
        this.httpError = httpError;
        this.description = description;
        this.data = data;
    }
}
