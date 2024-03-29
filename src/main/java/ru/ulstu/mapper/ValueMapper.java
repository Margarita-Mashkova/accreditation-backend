package ru.ulstu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.ulstu.dto.ValueDto;
import ru.ulstu.dto.ValueIdDto;
import ru.ulstu.model.Value;
import ru.ulstu.model.ValueId;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ValueMapper {
    ValueDto toValueDto(Value value);
    Value fromValueDto(ValueDto valueDto);
    ValueId fromValueIdDto(ValueIdDto valueIdDto);
}
