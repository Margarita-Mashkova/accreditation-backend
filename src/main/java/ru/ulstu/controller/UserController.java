package ru.ulstu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.dto.UserDto;
import ru.ulstu.mapper.OPOPMapper;
import ru.ulstu.mapper.UserMapper;
import ru.ulstu.model.enums.Role;
import ru.ulstu.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OPOPMapper opopMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto){
        return userMapper.toUserDto(userService.addUser(userDto.getName(), userDto.getSurname(),
                userDto.getPatronymic(), userDto.getLogin(), userDto.getPassword(), userDto.getRole()));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public UserDto editUser(@PathVariable Long id, @RequestBody UserDto userDto){
        return userMapper.toUserDto(userService.updateUser(id, userDto.getName(), userDto.getSurname(),
                userDto.getPatronymic(), userDto.getLogin(), userDto.getPassword(), userDto.getRole()));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public List<UserDto> findAllUsers(){
        return userService.findAllUsers().stream()
                .map(user -> userMapper.toUserDto(user))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/page")
    public List<UserDto> findAllUsersByPage(@RequestParam int pageNumber){
        return userService.findAllUsersByPage(pageNumber).stream()
                .map(user -> userMapper.toUserDto(user))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/pages")
    public int getAmountPages(){
        return userService.getAmountPages();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public UserDto findUser(@PathVariable Long id){
        return userMapper.toUserDto(userService.findUserById(id));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/roles")
    public List<Role> findAllRoles(){
        return userService.findAllRoles();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/find")
    public UserDto findUserByLogin(@RequestParam String login){
        return userMapper.toUserDto(userService.findUserByLogin(login));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping
    public void deleteAllUsers(){
        userService.deleteAllUsers();
    }
}
