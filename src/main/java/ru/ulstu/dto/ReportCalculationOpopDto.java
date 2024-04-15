package ru.ulstu.dto;

import lombok.Data;

import java.util.List;

// Модель расчета показателей для ОПОП за определенный год
@Data
public class ReportCalculationOpopDto {
    private String opopName;
    private List<CalculationDto> calculations;
    private int sum;
    private String accreditationStatus;
}
