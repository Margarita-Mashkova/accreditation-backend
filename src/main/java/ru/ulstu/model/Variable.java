package ru.ulstu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Variable {
    @Id
    @NonNull
    @Column(unique = true)
    private String key;

    @NonNull
    @NotBlank(message = "Variable name can't be null or empty")
    @Column(length = 400)
    private String name;
}
