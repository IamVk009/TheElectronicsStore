package com.lucifer.electronics.store.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CoverImageNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CoverImageNameValid {

    String message() default "Category cover image name is not valid..";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
