package ru.ulstu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.model.Value;
import ru.ulstu.model.ValueId;

import java.util.Date;
import java.util.List;

public interface ValueRepository extends JpaRepository<Value, ValueId> {
    List<Value> findAllByOpopIdAndIdDate(Long opopId, Date date);
}
