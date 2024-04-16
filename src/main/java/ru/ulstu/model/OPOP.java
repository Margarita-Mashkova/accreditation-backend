package ru.ulstu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = {"values", "calculations", "user"})
public class OPOP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @NotBlank(message = "OPOP name can't be null or empty")
    @Column(unique = true)
    private String name;

    @NonNull
    private String level;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "opop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Value> values;

    @OneToMany(mappedBy = "opop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calculation> calculations;

    public OPOP(@NonNull String name, @NonNull String level, User user) {
        this.name = name;
        this.level = level;
        this.user = user;
    }
}
