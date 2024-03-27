package ru.ulstu.dto;

import lombok.Data;

@Data
public class ProfileDto {
    private String name;
    private String surname;
    private String patronymic;
    private String login;
    private String password;
}
