package ru.ulstu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = "values")
public class Variable {
    @Id
    @NonNull
    @Column(unique = true)
    private String key;

    @NonNull
    @NotBlank(message = "Variable name can't be null or empty")
    @Column(length = 400)
    private String name;

    @OneToMany(mappedBy = "variable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Value> values;
}
