package com.ecom.project.exceptions;

import com.ecom.project.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    //Any non-valid exception will be handled here globally
    //response will be an object with {fieldName, msg} of both type String

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){

        Map<String,String> response = new HashMap<>();
        //Created a hashmap to store response object

        e.getBindingResult().getAllErrors().forEach(err->{
            //This will (typeCast)convert the error to the field error so that we can get the field name of the error
            String fieldName = ((FieldError)err).getField();
            String msg = err.getDefaultMessage();
            response.put(fieldName,msg);
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
        String msg = e.getMessage();
        APIResponse apiResponse = new APIResponse(msg,false);
        return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e){
        String msg = e.getMessage();
        APIResponse apiResponse = new APIResponse(msg,false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }
}
