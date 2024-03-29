package ru.ulstu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CalculationDto {
    private CalculationIdDto id;
    private float value;
    private int score;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String indicatorName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean planned;
}
