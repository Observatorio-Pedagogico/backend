package com.obervatorio_pedagogico.backend.infrastructure.exceptions.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessException {

    String key();

    HttpStatus status() default HttpStatus.BAD_REQUEST;

    boolean returnMessageException() default false;

}
