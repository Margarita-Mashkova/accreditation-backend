package ru.ulstu.service.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super(String.format("User with id [%d] not found", id));
    }
}
