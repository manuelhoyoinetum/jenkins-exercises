package com.inetum.course.prueba.jenkins.infrastructure.rest.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.inetum.course.prueba.jenkins.infrastructure.rest.spring.HttpError;
import com.inetum.course.prueba.jenkins.infrastructure.rest.spring.controller.dto.ErrorDto;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * REST Exception Handler.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    private static final Logger handlerLogger = LoggerFactory.getLogger(RestExceptionHandler.class);

    private static ErrorDto getErrorDto(HttpError saidbHttpError, String description, Object data)
    {
        return new ErrorDto()
                .status(saidbHttpError.getStatus())
                .number(saidbHttpError.getNumber())
                .code(saidbHttpError.getCode())
                .title(saidbHttpError.getTitle())
                .description(description)
                .data(data);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException mediaTypeNotSupportedException,
            HttpHeaders headers, HttpStatus status,
            WebRequest request
    )
    {
        handlerLogger.warn("mediaTypeNotSupportedException: " + mediaTypeNotSupportedException.getMessage());

        return toResponseEntity(
                HttpError.INVALID_MEDIA_TYPE,
                mediaTypeNotSupportedException,
                mediaTypeNotSupportedException.getMessage(),
                null
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException messageNotReadableException,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    )
    {
        handlerLogger.warn("HttpMessageNotReadableException: " + messageNotReadableException.getMessage());

        if (messageNotReadableException.getCause() == null)
        {
            return toResponseEntity(
                    HttpError.MISSING_JSON_BODY,
                    messageNotReadableException,
                    messageNotReadableException.getMessage(),
                    null
            );
        }

        if (messageNotReadableException.getCause() instanceof ValueInstantiationException)
        {
            ValueInstantiationException valueInstantiationException
                    = (ValueInstantiationException) messageNotReadableException.getCause();
            if (valueInstantiationException.getCause() instanceof IllegalArgumentException)
            {
                IllegalArgumentException illegalArgumentException =
                        (IllegalArgumentException) valueInstantiationException.getCause();

                return toResponseEntity(
                        HttpError.ILLEGAL_ARGUMENT,
                        illegalArgumentException,
                        null,
                        null
                );
            }

            return toResponseEntity(
                    HttpError.VALUE_INSTANTIATION,
                    valueInstantiationException,
                    null,
                    null
            );
        }

        if (messageNotReadableException.getCause() instanceof InvalidFormatException)
        {
            InvalidFormatException invalidFormatException =
                    (InvalidFormatException) messageNotReadableException.getCause();
            return toResponseEntity(
                    HttpError.INVALID_FORMAT,
                    invalidFormatException,
                    null,
                    invalidFormatException.getValue()
            );
        }

        if (messageNotReadableException.getCause() instanceof JsonProcessingException)
        {
            JsonProcessingException jsonProcessingException =
                    (JsonProcessingException) messageNotReadableException.getCause();
            return toResponseEntity(
                    HttpError.INVALID_JSON_BODY,
                    jsonProcessingException,
                    jsonProcessingException.getMessage(),
                    null
            );
        }

        return super.handleHttpMessageNotReadable(messageNotReadableException, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    )
    {
        handlerLogger.error("Unhandled exception: ", exception);

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status))
        {
            request.setAttribute("javax.servlet.error.exception", exception, 0);
        }

        return toResponseEntity(HttpError.INTERNAL_SERVER_ERROR, exception, null, null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    )
    {
        handlerLogger.warn("MethodArgumentNotValidException: " + exception.getMessage());

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<String> errors = new ArrayList<>();

        for (FieldError fieldError : fieldErrors)
        {
            if (fieldError.getRejectedValue() == null)
            {
                return toResponseEntity(
                        HttpError.NULL_FIELD_VALUE,
                        exception,
                        "Al menos uno de los campos obligatorios está ausente o es nulo",
                        fieldError.getField()
                );
            } else
            {
                return toResponseEntity(
                        HttpError.INVALID_FIELD_VALUE,
                        exception,
                        "Al menos uno de los campos obligatorios está vacío o tiene un formato incorrecto",
                        fieldError.getField()
                );
            }
        }

        for (ObjectError objectError : globalErrors)
        {
            errors.add(objectError.getObjectName() + ", " + objectError.getDefaultMessage());
        }

        return toResponseEntity(
                HttpError.GENERIC_INVALID_FIELDS_VALUES,
                exception,
                null,
                errors
        );
    }

    /**
     * Handles RuntimeApiException.
     * Estas excepciones ya contienen todos los datos necesarios en la respuesta HTTP.
     */
    @ExceptionHandler(value = RuntimeApiException.class)
    public ResponseEntity<Object> handleApiException(RuntimeApiException runtimeApiException, WebRequest request)
    {
        handlerLogger.info("RuntimeApiException: " + runtimeApiException.getMessage());

        return toResponseEntity(
                runtimeApiException.httpError,
                runtimeApiException,
                runtimeApiException.description,
                runtimeApiException.data
        );
    }

    /**
     * Handles unexpected errors.
     */
    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Object> handleUnexpectedException(Throwable throwable, WebRequest request)
    {
        handlerLogger.error("Unhandled throwable", throwable);
        return toResponseEntity(HttpError.INTERNAL_SERVER_ERROR, throwable, null, null);
    }

    private ResponseEntity<Object> toResponseEntity(
            HttpError saidbHttpError,
            Throwable throwable,
            String description,
            Object data)
    {

        ErrorDto errorDto = throwable == null
                ? getErrorDto(
                saidbHttpError,
                description,
                data
        ) : getErrorDto(
                saidbHttpError,
                description != null ? description : throwable.getMessage(),
                data
        );

        return new ResponseEntity<>(errorDto, saidbHttpError.getHttpStatus());
    }
}
