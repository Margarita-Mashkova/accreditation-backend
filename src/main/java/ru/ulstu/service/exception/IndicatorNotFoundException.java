package ru.ulstu.service.exception;

public class IndicatorNotFoundException extends RuntimeException{
    public IndicatorNotFoundException(String key) {
        super(String.format("Indicator with key [%s] not found", key));
    }
}
