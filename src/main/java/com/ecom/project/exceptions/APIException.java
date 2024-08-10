package com.ecom.project.exceptions;

public class APIException extends RuntimeException{

//    private static final: These keywords indicate that serialVersionUID
//    is a constant value associated with the class, and it can't be changed.
//    static final is very similar to const in JS

    private static final long serialVersionUID = 1L;

    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }
}
