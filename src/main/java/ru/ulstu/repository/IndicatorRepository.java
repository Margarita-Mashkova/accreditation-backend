package ru.ulstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ulstu.model.Indicator;

public interface IndicatorRepository extends JpaRepository<Indicator, String>,
        PagingAndSortingRepository <Indicator, String> {

}
