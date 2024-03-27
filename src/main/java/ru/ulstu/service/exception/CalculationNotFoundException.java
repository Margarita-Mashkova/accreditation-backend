package ru.ulstu.service.exception;

import ru.ulstu.model.CalculationId;

public class CalculationNotFoundException extends RuntimeException{
    public CalculationNotFoundException(CalculationId calculationId) {
        super(String.format("Calculation with opop_id [%d], indicator_key [%s] and date [%s] not found",
                calculationId.getOpopId(), calculationId.getIndicatorKey(), calculationId.getDate()));
    }
}
