package ru.ulstu.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

// хранятся значения переменных для расчета в определенный год и для определенной ОПОП
@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "variable_value")
public class Value {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // TODO: Подумать над типом данных
    private float value;

    @NonNull
    private Date date;

    @ManyToOne
    @JoinColumn(name = "OPOP_id")
    private OPOP opop;

    @ManyToOne
    @JoinColumn(name = "variable_key")
    private Variable variable;
}
