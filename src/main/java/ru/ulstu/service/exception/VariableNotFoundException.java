package ru.ulstu.service.exception;

public class VariableNotFoundException extends RuntimeException{
    public VariableNotFoundException(String key) {
        super(String.format("Variable with key [%s] not found", key));
    }
}
