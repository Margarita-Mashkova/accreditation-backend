package ru.ulstu.service.exception;

import ru.ulstu.model.ValueId;

public class ValueNotFoundException extends RuntimeException {

    public ValueNotFoundException(ValueId valueId) {
        super(String.format("Value with opop_id [%d], variable_key [%s] and date [%s] not found",
                valueId.getOpopId(), valueId.getVariableKey(), valueId.getDate()));
    }
}
