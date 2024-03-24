package ru.ulstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
