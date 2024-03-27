package ru.ulstu.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.ulstu.dto.AuthDto;
import ru.ulstu.dto.OPOPDto;
import ru.ulstu.dto.UserDto;
import ru.ulstu.model.OPOP;
import ru.ulstu.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User fromAuthDto(AuthDto authDto);
    UserDto toUserDto(User user);

    @AfterMapping
    default void setUserLogin(OPOP opop, @MappingTarget OPOPDto opopDto){
        opopDto.setUserLogin(opop.getUser().getLogin());
    }
}
