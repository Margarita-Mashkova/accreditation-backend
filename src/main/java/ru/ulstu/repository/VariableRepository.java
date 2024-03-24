package ru.ulstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.model.Variable;

public interface VariableRepository extends JpaRepository<Variable, String> {
}
