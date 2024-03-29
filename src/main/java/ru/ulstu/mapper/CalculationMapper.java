package ru.ulstu.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.ulstu.dto.CalculationDto;
import ru.ulstu.dto.CalculationIdDto;
import ru.ulstu.dto.OPOPDto;
import ru.ulstu.model.Calculation;
import ru.ulstu.model.CalculationId;
import ru.ulstu.model.OPOP;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CalculationMapper {
    CalculationDto toCalculationDto(Calculation calculation);
    CalculationId fromCalculationIdDto(CalculationIdDto calculationIdDto);

    @AfterMapping
    default void setIndicatorName(Calculation calculation, @MappingTarget CalculationDto calculationDto){
        calculationDto.setIndicatorName(calculation.getIndicator().getName());
    }
}
