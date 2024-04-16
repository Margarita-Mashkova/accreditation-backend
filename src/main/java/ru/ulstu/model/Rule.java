package ru.ulstu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = "indicator")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer min;
    private Integer max;
    @NonNull
    private int score;
    @NonNull
    @NotBlank(message = "Rule level can't be null or empty")
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
