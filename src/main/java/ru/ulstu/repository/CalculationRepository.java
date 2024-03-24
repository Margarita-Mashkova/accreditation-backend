package ru.ulstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.model.Calculation;

public interface CalculationRepository extends JpaRepository<Calculation, Long> {
}
