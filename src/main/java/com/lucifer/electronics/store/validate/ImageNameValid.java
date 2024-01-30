package com.lucifer.electronics.store.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageNameValid {

//  Default error message
    String message() default "ImageName is not valid..";

//  Represents group of constraints
    Class<?>[] groups() default {};

//  Represents additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
