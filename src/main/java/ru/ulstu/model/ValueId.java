package ru.ulstu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ValueId implements Serializable {
    @NonNull
    @Column(name = "opop_id")
    private Long opopId;
    @NonNull
    @Column(name = "variable_key")
    private String variableKey;
    @NonNull
    @Column(columnDefinition = "DATE")
    private Date date;
}
