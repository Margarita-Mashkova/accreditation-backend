package ru.ulstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.model.Calculation;
import ru.ulstu.model.CalculationId;

import java.util.Date;
import java.util.List;

public interface CalculationRepository extends JpaRepository<Calculation, CalculationId> {
    List<Calculation> findAllByOpopIdAndIdDate(Long opopId, Date date);
}
