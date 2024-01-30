package com.lucifer.electronics.store.exceptions;

import com.lucifer.electronics.store.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        logger.info("Executing ResourceNotFoundExceptionHandler..");
        return new ResponseEntity<>(ApiResponseMessage.builder()
                .message(e.getMessage())
                .success(true)
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Map<String, Object> response = new HashMap<>();
        allErrors.forEach(objectError ->{
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field, message);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
