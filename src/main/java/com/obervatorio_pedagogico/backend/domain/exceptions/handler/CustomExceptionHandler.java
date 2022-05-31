package com.obervatorio_pedagogico.backend.domain.exceptions.handler;


import java.nio.file.AccessDeniedException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.obervatorio_pedagogico.backend.infrastructure.exceptions.annotation.BusinessException;
import com.obervatorio_pedagogico.backend.infrastructure.utils.httpResponse.MessageService;
import com.obervatorio_pedagogico.backend.presentation.shared.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.ConstraintViolationException;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    protected final MessageService mensagemService;

    public CustomExceptionHandler(MessageService mensagemService) {
        this.mensagemService = mensagemService;
    }

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        BusinessException businessExAnnotation = AnnotationUtils.findAnnotation(ex.getClass(), BusinessException.class);

        if (Objects.nonNull(businessExAnnotation))
            return handleException(ex, request, businessExAnnotation);

        log.error(ex.getMessage(), ex);
        return handleException(ex, HttpStatus.BAD_REQUEST, request, "recurso.operacao-invalida");
    }

    @ExceptionHandler({ TransactionSystemException.class })
    public ResponseEntity<Object> handleRollbackException(TransactionSystemException ex, WebRequest request) {

        Exception exception = (Exception) ex.getCause().getCause();

        if (exception.getClass().equals(ConstraintViolationException.class)) {
            return this.handleConstraintViolationException((ConstraintViolationException) exception, request);
        } else {
            return this.handleRuntimeException((RuntimeException) ex, request);
        }
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return handleException(ex, HttpStatus.UNAUTHORIZED, request, "seguranca.permisao-negada");
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
            WebRequest request) {
        Response<String> responseTOTO = new Response<>();
        ex.getConstraintViolations()
                .forEach(e -> responseTOTO.getErrors().add(MessageFormat.format(e.getMessage(), e.getPropertyPath())));

        return handleExceptionInternal(ex, responseTOTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response<List<String>> response = new Response<>();
        List<String> erros = obterListaErros(ex.getBindingResult());
        response.setErrors(erros);
        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
            WebRequest request) {
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            return handleHibernateConstraintViolationException((org.hibernate.exception.ConstraintViolationException) ex.getCause(),
                    request);
        }

        return handleException(ex, HttpStatus.BAD_REQUEST, request, "recurso.operacao-invalida");
    }

    @ExceptionHandler({ org.hibernate.exception.ConstraintViolationException.class })
    public ResponseEntity<Object> handleHibernateConstraintViolationException(org.hibernate.exception.ConstraintViolationException ex,
            WebRequest request) {
        try {
            return handleException(ex, HttpStatus.BAD_REQUEST, request, "constraint." + ex.getConstraintName());
        } catch (NoSuchMessageException noSuchMessageException) {
            return handleException(ex, HttpStatus.BAD_REQUEST, request, "constraint.unique-key");
        }
    }

    protected ResponseEntity<Object> handleException(Exception ex, HttpStatus status, WebRequest req, String chave) {
        Response<List<String>> response = new Response<>();
        response.setErrors(Arrays.asList((mensagemService.getMessage(chave))));
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, req);
    }

    protected ResponseEntity<Object> handleException(Exception ex, WebRequest req, BusinessException businessException) {
        Response<List<String>> response = new Response<>();
        String message = mensagemService.getMessage(businessException.key());
        String exceptionMessage = Objects.nonNull(ex.getMessage()) ? " " + ex.getMessage() : "";

        if (businessException.returnMessageException())
            message += exceptionMessage;

        response.setErrors(Arrays.asList(message));
        return handleExceptionInternal(ex, response, new HttpHeaders(), businessException.status(), req);
    }

    protected List<String> obterListaErros(BindingResult bindingResult) {
        List<String> erros = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(e -> erros.add(mensagemService.getMessage(e)));
        return erros;
    }

}