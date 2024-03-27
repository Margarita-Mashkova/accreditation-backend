package ru.ulstu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.ulstu.dto.IndicatorDto;
import ru.ulstu.model.Indicator;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndicatorMapper {
    IndicatorDto toIndicatorDto(Indicator indicator);
}
