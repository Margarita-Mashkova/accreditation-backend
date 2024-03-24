package ru.ulstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.model.Indicator;

public interface IndicatorRepository extends JpaRepository<Indicator, String> {
}
