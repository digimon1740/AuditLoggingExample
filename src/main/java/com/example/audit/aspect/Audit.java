package com.example.audit.aspect;

import com.example.audit.log.AuditAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

    // targetClass가 default값이라면 target의 값이 우선임
    String target() default "";

    Class targetClass() default Object.class;

    AuditAction action() default AuditAction.NONE;
}
