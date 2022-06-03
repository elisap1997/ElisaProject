package com.project.elisa.annotation;

import com.project.elisa.models.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Authorized {
    public Role[] allowedRoles() default {};
}
