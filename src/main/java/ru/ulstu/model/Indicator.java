package ru.ulstu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Indicator {

    //TODO: String?
    @Id
    @Column(unique = true)
    @NonNull
    private String key;

    @NonNull
    @NotBlank(message = "Indicator name can't be null or empty")
    private String name;

    @NonNull
    @NotBlank(message = "Indicator formula can't be null or empty")
    private String formula;
}
