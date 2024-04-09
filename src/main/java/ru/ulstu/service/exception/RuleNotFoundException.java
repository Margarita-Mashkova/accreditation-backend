package ru.ulstu.service.exception;

public class RuleNotFoundException extends RuntimeException{
    public RuleNotFoundException(Long id) {
        super(String.format("Rule with id [%d] not found", id));
    }
}
