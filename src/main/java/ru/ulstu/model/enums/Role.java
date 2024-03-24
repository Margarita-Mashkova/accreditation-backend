package ru.ulstu.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    // Создает пользователей
    ADMIN,
    // Представитель учебного управления
    DEAN,
    // Руководитель ОПОП
    MANAGER;

    private static final String PREFIX = "ROLE_";
    @Override
    public String getAuthority() {
        return PREFIX + this.name();
    }

}
