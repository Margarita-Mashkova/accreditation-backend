package ru.ulstu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.ulstu.dto.VariableDto;
import ru.ulstu.model.Variable;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VariableMapper {
    VariableDto toVariableDto (Variable variable);
}
