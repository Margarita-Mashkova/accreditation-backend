package ru.ulstu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.ulstu.dto.AuthDto;
import ru.ulstu.dto.UserDto;
import ru.ulstu.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User fromAuthDto(AuthDto authDto);
    UserDto toUserDto(User user);
}
