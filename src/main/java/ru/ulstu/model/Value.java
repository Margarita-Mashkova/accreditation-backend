package ru.ulstu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

// хранятся значения переменных для расчета в определенный год и для определенной ОПОП
@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "variable_opop")
public class Value {
    @EmbeddedId
    @NonNull
    private ValueId id;

    @ManyToOne
    @MapsId("opop_id")
    @NonNull
    private OPOP opop;

    @ManyToOne
    @MapsId("variable_key")
    @NonNull
    private Variable variable;
    @NonNull
    private float value;
}
