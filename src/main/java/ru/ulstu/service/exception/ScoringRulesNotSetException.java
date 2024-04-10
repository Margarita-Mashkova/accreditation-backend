package ru.ulstu.service.exception;

public class ScoringRulesNotSetException extends RuntimeException{
    public ScoringRulesNotSetException(String indicatorKey) {
        super(String.format("The scoring rules for the indicator [%s] are not set", indicatorKey));
    }
}
