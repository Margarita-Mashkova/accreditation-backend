package ru.ulstu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.ulstu.dto.CalculationDto;
import ru.ulstu.dto.CalculationIdDto;
import ru.ulstu.model.Calculation;
import ru.ulstu.model.CalculationId;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CalculationMapper {
    CalculationDto toCalculationDto(Calculation calculation);
    CalculationId fromCalculationIdDto(CalculationIdDto calculationIdDto);
}
