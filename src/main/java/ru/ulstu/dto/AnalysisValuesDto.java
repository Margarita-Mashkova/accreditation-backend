package ru.ulstu.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AnalysisValuesDto {
    private String indicatorName;
    private List<Date> dates;
    private List<Float> values;
    private List<Integer> scores;
    private List<Boolean> planned;
}
