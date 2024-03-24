package ru.ulstu.service.exception;

public class OPOPNotFoundException extends RuntimeException{
    public OPOPNotFoundException(Long id) {
        super(String.format("OPOP with id [%d] not found", id));
    }
}
