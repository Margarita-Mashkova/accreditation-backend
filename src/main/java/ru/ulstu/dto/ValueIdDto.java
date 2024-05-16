package ru.ulstu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueIdDto {
    private Long opopId;
    private String variableKey;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
