package ru.ulstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.model.Value;

public interface ValueRepository extends JpaRepository<Value, Long> {
}
