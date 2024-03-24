package ru.ulstu.service.exception;

public class IndicatorNotFoundException extends RuntimeException{
    public IndicatorNotFoundException(Long id) {
        super(String.format("Indicator with id [%d] not found", id));
    }
}
