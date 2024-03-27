package ru.ulstu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CalculationId {
    @NonNull
    @Column(name = "opop_id")
    private Long opopId;
    @NonNull
    @Column(name = "indicator_key")
    private String indicatorKey;
    @NonNull
    @Column(columnDefinition = "DATE")
    private Date date;
}
