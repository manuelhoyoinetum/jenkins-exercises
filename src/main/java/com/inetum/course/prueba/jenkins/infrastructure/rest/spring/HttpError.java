package com.inetum.course.prueba.jenkins.infrastructure.rest.spring;

import org.springframework.http.HttpStatus;

/**
 * HttpError.
 */
public enum HttpError
{
    INTERNAL_SERVER_ERROR(1000, 500, "Internal server error"),
    GENERAL_ERROR(2000, 400, "Error general"),
    MISSING_AUTH_HEADER(2001, 401, "Missing 'Authorization' header"),
    INVALID_AUTH_HEADER_PREFIX(2002, 401, "Invalid 'Authorization' header prefix"),
    INVALID_AUTH_TOKEN(2003, 401, "Invalid JWT token"),
    MALFORMED_AUTH_TOKEN(2004, 403, "Malformed JWT token"),
    GENERIC_INVALID_FIELDS_VALUES(2005, 400, "Invalid fields values"),
    NOT_FOUND_ENTITY(2006, 404, "Entity not found"),
    VALUE_INSTANTIATION(2007, 400, "Value instantiation error"),
    WRONG_DNI_FORMAT(2008, 400, "Wrong DNI format"),
    ILLEGAL_ARGUMENT(2009, 400, "Illegal argument"),
    INVALID_FORMAT(2010, 400, "Invalid format"),
    ENTITY_NOT_FOUND(2011, 404, "Entity not found"),
    ENTITY_ALREADY_CREATED(2012, 400, "Entity already created"),
    NULL_FIELD_VALUE(2013, 400, "Campo obligatorio nulo o ausente"),
    INVALID_FIELD_VALUE(2014, 400, "Campo obligatorio vacío o con formato incorrecto"),
    INVALID_WEBPOL_PASS_TOKEN(2015, 400, "Invalid WebpolPass Token"),
    WEBPOL_PASS_REQUEST_ERROR(2016, 500, "WebpolPass request error"),
    MISSING_WEBPOL_PASS_HEADER(2017, 401, "Missing 'Police-Token' header"),
    TOKEN_ALREADY_RETURNED(2018, 401, "Token already returned"),
    REQUEST_BLOCKED_BY_FIREWALL(2019, 401, "Request blocked by Firewall"),
    INVALID_JSON_BODY(2020, 400, "JSON de la petición inválido"),
    MISSING_JSON_BODY(2021, 400, "JSON de la petición ausente"),
    INVALID_MEDIA_TYPE(2022, 400, "Media type (Content-Type) no válido");

    private final int number;
    private final int status;
    private final String title;

    HttpError(int number, int status, String title)
    {
        this.number = number;
        this.status = status;
        this.title = title;
    }

    public Integer getNumber()
    {
        return number;
    }

    public Integer getStatus()
    {
        return status;
    }

    public String getTitle()
    {
        return title;
    }

    public String getCode()
    {
        return this.name();
    }

    public HttpStatus getHttpStatus()
    {
        return HttpStatus.resolve(this.status);
    }
}
