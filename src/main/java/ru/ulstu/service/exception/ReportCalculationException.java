package ru.ulstu.service.exception;

public class ReportCalculationException extends RuntimeException{
    public ReportCalculationException() {
        super("Not all the data for the calculation has been entered");
    }
}
