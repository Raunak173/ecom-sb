package com.ecom.project.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    //We need to extend the RunTimeException Class

    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    //Nothing fancy, just created one no args constructor and 2 types of parameterised
    //constructor that we need for showing this exception

    public ResourceNotFoundException(){
    }

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s",resourceName,field,fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s: %d",resourceName,field,fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
