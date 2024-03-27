package ru.ulstu.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class CalculationIdDto {
    @NonNull
    private Long opopId;
    @NonNull
    private String indicatorKey;
    @NonNull
    //@JsonFormat(pattern="yyyy-MM-dd")
    private Date date;
}
