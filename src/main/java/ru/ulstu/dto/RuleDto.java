package ru.ulstu.dto;

import lombok.Data;

@Data
public class RuleDto {
    private Integer min;
    private Integer max;
    private int score;
    private String indicatorKey;
}
