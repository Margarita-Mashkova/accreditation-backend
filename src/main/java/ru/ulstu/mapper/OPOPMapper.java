package ru.ulstu.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.ulstu.dto.OPOPDto;
import ru.ulstu.model.OPOP;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OPOPMapper {
    OPOPDto toOPOPDto (OPOP opop);
    OPOP fromOPOPDto (OPOPDto opopDto);

    @AfterMapping
    default void setUserLogin(OPOP opop, @MappingTarget OPOPDto opopDto){
        if(opop.getUser() != null){
            opopDto.setUserLogin(opop.getUser().getLogin());
        }
    }
}
