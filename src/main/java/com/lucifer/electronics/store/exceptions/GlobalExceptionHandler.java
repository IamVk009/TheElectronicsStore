package com.lucifer.electronics.store.exceptions;

import com.lucifer.electronics.store.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException e){
        logger.info("Executing ResourceNotFoundExceptionHandler..");
        return new ResponseEntity<>(ApiResponseMessage.builder()
                .message(e.getMessage())
                .success(true)
                .build(), HttpStatus.NOT_FOUND);
    }
}
