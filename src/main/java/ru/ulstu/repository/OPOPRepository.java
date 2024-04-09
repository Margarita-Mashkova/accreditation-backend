package ru.ulstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ulstu.model.OPOP;

public interface OPOPRepository extends JpaRepository<OPOP, Long>,
        PagingAndSortingRepository<OPOP, Long> {
}
