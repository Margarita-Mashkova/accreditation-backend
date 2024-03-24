package ru.ulstu.util.handler;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.ulstu.service.exception.*;

@ControllerAdvice
public class AdviceController {
    @ExceptionHandler({
            ValidationException.class,
            UserNotFoundException.class,
            OPOPNotFoundException.class,
            IndicatorNotFoundException.class,
            VariableNotFoundException.class,
            WrongLoginOrPasswordException.class,
            UserNotFoundException.class

    })
    public ResponseEntity<Object> handleException(Throwable e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownException(Throwable e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
