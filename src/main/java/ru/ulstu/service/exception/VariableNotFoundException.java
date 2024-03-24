package ru.ulstu.service.exception;

public class VariableNotFoundException extends RuntimeException{
    public VariableNotFoundException(Long id) {
        super(String.format("Variable with id [%d] not found", id));
    }
}
