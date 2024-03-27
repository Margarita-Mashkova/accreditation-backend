package ru.ulstu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.ulstu.model.enums.Role;

import java.util.List;

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String login;
    private String password;
    private Role role;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<OPOPDto> opops;
}
