package ru.ulstu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Indicator {
    @Id
    @Column(unique = true)
    @NonNull
    private String key;

    @NonNull
    @NotBlank(message = "Indicator name can't be null or empty")
    @Column(length = 600)
    private String name;

    @NotBlank(message = "Indicator formula can't be null or empty")
    private String formula;

    @Column(columnDefinition = "boolean default false")
    private boolean boolType;
    @Column(columnDefinition = "boolean default false")
    private boolean boolValue;

    @NonNull
    @OneToMany
    @JoinColumn(name = "indicator_key")
    private List<Rule> rules;

    public Indicator(@NonNull String key, @NonNull String name, String formula,
                     boolean boolType, boolean boolValue, @NonNull List<Rule> rules) {
        this.key = key;
        this.name = name;
        this.formula = formula;
        this.boolType = boolType;
        this.boolValue = boolValue;
        this.rules = rules;
    }
}
