package ru.ulstu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer min;
    private Integer max;
    @NonNull
    private int score;
    @NonNull
    private String level;

    @ManyToOne
    @JoinColumn(name = "indicator_key")
    @NonNull
    private Indicator indicator;

    public Rule(Integer min, Integer max, @NonNull int score, String level) {
        this.min = min;
        this.max = max;
        this.score = score;
        this.level = level;
    }
}
