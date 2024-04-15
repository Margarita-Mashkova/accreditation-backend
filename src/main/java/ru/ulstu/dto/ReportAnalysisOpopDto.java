package ru.ulstu.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

// Модель сравнения показателей для ОПОП по годам
@Data
public class ReportAnalysisOpopDto {
    private String opopName;
    private String indicatorName;
    private String indicatorKey;
    private List<Date> dates;
    private List<Float> values;
    private List<Integer> scores;
    private List<Boolean> planned;
}
