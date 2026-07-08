package com.example.studentmanagement.exception;

//why run time exception Because this is an unchecked exception.
//Spring automatically rolls back transactions when unchecked exceptions occur.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);//This passes the message to the parent (RuntimeException).
    }

}