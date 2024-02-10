package com.lucifer.electronics.store.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
public class CoverImageNameValidator implements ConstraintValidator<CoverImageNameValid, String> {
    @Override
    public boolean isValid(String coverImageName, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Executing CoverImageNameValidator..");
        String imageFileExtension = "";
        if (coverImageName == null) return false;
        if (!coverImageName.isBlank()) {
            imageFileExtension = coverImageName.substring(coverImageName.lastIndexOf("."));
            return imageFileExtension.equalsIgnoreCase(".png") ||
                    imageFileExtension.equalsIgnoreCase(".jpg") ||
                    imageFileExtension.equalsIgnoreCase(".jpeg");
        }
        return false;
    }
}
