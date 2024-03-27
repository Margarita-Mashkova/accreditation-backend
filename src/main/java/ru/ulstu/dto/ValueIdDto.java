package ru.ulstu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class ValueIdDto {
    @NonNull
    private Long opopId;
    @NonNull
    private String variableKey;
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
