package com.example.studentmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import com.example.studentmanagement.dto.ErrorResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//Internal flow
// controller - service - repository - no student found -
//throw resource not found exception - global exception handler-
//return 404 json error


@RestControllerAdvice// this is a common error handler for all controllers
public class GlobalExceptionHandler {

// the below line means whenever this exception occurs call this method
    @ExceptionHandler(ResourceNotFoundException.class)

    //responseEntity provides body+status code+headers
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex){

        ErrorResponse error=new ErrorResponse(
           LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(), //it returns 404 error
            HttpStatus.NOT_FOUND.getReasonPhrase(),// it returns the reason for error "not found"
            ex.getMessage() //Student not found with id 20
        );
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }


    //this is to handle unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage()
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }
}
