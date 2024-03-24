package ru.ulstu.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Calculation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "indicator_id")
    private Indicator indicator;

    @ManyToOne
    @JoinColumn(name = "opop_id")
    private OPOP opop;

    @NonNull
    private Date date;

    // значение показателя, рассчитанное по формуле
    private float value;

    // количество баллов (рассчитывается по правилам «если-то» в зависимости от значения показателя)
    private int score;

    // является ли данное значение планируемым (расчет показателей на будущие годы)
    private boolean isPlanned;
}
