package com.springbootLearning.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.springbootLearning.utils.ResultResponse;



import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Configuration
@ControllerAdvice
public class GlobalParamsValidExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("code", ResultResponse.PARAMS_ERROR);
        errors.put("data", null);
        ArrayList<String> errorMsg = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errorMsg.add(errorMessage);
        });
        errors.put("msg", errorMsg.stream().collect(Collectors.joining(",")));
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }
}
