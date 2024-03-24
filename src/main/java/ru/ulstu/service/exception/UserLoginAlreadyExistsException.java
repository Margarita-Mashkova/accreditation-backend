package ru.ulstu.service.exception;

public class UserLoginAlreadyExistsException extends RuntimeException{
    public UserLoginAlreadyExistsException(String login) {
        super(String.format("User with login [%s] already exists", login));
    }
}
