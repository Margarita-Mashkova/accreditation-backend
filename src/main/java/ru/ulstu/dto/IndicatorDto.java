package ru.ulstu.dto;

import lombok.Data;

import java.util.List;

@Data
public class IndicatorDto {
    private String key;
    private String name;
    private String formula;
    private boolean boolType;
    private boolean boolValue;
    private List<RuleDto> rules;
}
