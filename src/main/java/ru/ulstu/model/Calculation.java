package ru.ulstu.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
//@ToString(exclude = "opop")
public class Calculation {
    @EmbeddedId
    @NonNull
    private CalculationId id;

    @ManyToOne
    @MapsId("indicator_id")
    @NonNull
    private Indicator indicator;

    @ManyToOne
    @MapsId("opop_id")
    @NonNull
    private OPOP opop;

    // значение показателя, рассчитанное по формуле
    @NonNull
    private float value;

    // количество баллов (рассчитывается по правилам «если-то» в зависимости от значения показателя)
    @NonNull
    private int score;

    @NonNull
    private String level;

    // является ли данное значение планируемым (расчет показателей на будущие годы)
    private boolean planned;

    @PostLoad
    private void onLoad() {
        Date dateNow = new Date();
        this.planned = dateNow.before(id.getDate());
    }
}
