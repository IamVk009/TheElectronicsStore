package com.lucifer.electronics.store.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {

    private final Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);

    @Override
    public boolean isValid(String imageName, ConstraintValidatorContext constraintValidatorContext) {
        logger.info("Executing ImageNameValidator...");
        return (imageName.isBlank())? false : true;
    }
}
