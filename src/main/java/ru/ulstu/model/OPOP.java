package ru.ulstu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class OPOP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @NotBlank(message = "OPOP name can't be null or empty")
    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;
}
