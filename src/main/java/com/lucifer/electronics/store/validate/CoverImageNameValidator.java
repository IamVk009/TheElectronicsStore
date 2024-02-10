package com.lucifer.electronics.store.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoverImageNameValidator implements ConstraintValidator<CoverImageNameValid, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Executing CoverImageNameValidator..");
        return !s.isBlank();
    }
}
