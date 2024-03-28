package ru.ulstu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.dto.AuthDto;
import ru.ulstu.dto.ProfileDto;
import ru.ulstu.dto.UserDto;
import ru.ulstu.mapper.UserMapper;
import ru.ulstu.model.User;
import ru.ulstu.service.UserService;

@RestController
@RequestMapping("/")
public class ProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal User user) {
        System.out.println(user.getAuthorities());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return userMapper.toUserDto(user);
    }

    @PostMapping("/auth")
    public String auth(@RequestBody AuthDto authDto) {
        return userService.authorization(userMapper.fromAuthDto(authDto));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/edit-profile")
    public UserDto editProfile(@AuthenticationPrincipal User user, @RequestBody ProfileDto profileDto){
        return userMapper.toUserDto(userService.updateUser(user.getId(), profileDto.getName(), profileDto.getSurname(),
                profileDto.getPatronymic(), profileDto.getLogin(), profileDto.getPassword(), user.getRole()));
    }
}
