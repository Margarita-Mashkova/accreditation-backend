package ru.ulstu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.dto.AuthDto;
import ru.ulstu.dto.OPOPDto;
import ru.ulstu.dto.UserDto;
import ru.ulstu.mapper.OPOPMapper;
import ru.ulstu.mapper.UserMapper;
import ru.ulstu.model.User;
import ru.ulstu.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OPOPMapper opopMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal User user) {
        return userMapper.toUserDto(user);
    }

    @PostMapping("/auth")
    public String auth(@RequestBody AuthDto authDto) {
        return userService.authorization(userMapper.fromAuthDto(authDto));
    }

    @PostMapping("/add-user")
    public UserDto createUser(@RequestBody UserDto userDto){
        return userMapper.toUserDto(userService.addUser(userDto.getName(), userDto.getSurname(),
                userDto.getPatronymic(), userDto.getLogin(), userDto.getPassword(), userDto.getRole(),
                userDto.getOpops()
                        .stream()
                        .map(OPOPDto::getId)
                        .toList()));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/edit-user")
    public UserDto editUser(@AuthenticationPrincipal User user, @RequestBody UserDto userDto){
        return userMapper.toUserDto(userService.updateUser(user, userDto.getName(), userDto.getSurname(),
                userDto.getPatronymic(), userDto.getLogin(), userDto.getPassword(), userDto.getRole(),
                userDto.getOpops()
                        .stream()
                        .map(OPOPDto::getId)
                        .toList()));
    }

    @GetMapping("/users")
    public List<UserDto> findAllUsers(){
        return userService.findAllUsers().stream()
                .map(user -> userMapper.toUserDto(user))
                .toList();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @DeleteMapping
    public void deleteAllUsers(){
        userService.deleteAllUsers();
    }
}
