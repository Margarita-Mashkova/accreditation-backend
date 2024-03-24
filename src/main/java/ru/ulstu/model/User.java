package ru.ulstu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ulstu.model.enums.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @NotBlank(message = "User name can't be null or empty")
    private String name;

    @NonNull
    @NotBlank(message = "User surname can't be null or empty")
    private String surname;

    @NonNull
    @NotBlank(message = "User patronymic can't be null or empty")
    private String patronymic;

    @NonNull
    @NotBlank(message = "User login can't be null or empty")
    private String login;

    @NonNull
    @NotBlank(message = "User password can't be null or empty")
    private String password;

    @NonNull
    private Role role;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<OPOP> opops;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
