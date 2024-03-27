package ru.ulstu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.dto.OPOPDto;
import ru.ulstu.dto.UserDto;
import ru.ulstu.mapper.OPOPMapper;
import ru.ulstu.mapper.UserMapper;
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

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto){
        return userMapper.toUserDto(userService.addUser(userDto.getName(), userDto.getSurname(),
                userDto.getPatronymic(), userDto.getLogin(), userDto.getPassword(), userDto.getRole()));
    }

    @PutMapping("/{id}")
    public UserDto editUser(@PathVariable Long id, @RequestBody UserDto userDto){
        return userMapper.toUserDto(userService.updateUser(id, userDto.getName(), userDto.getSurname(),
                userDto.getPatronymic(), userDto.getLogin(), userDto.getPassword(), userDto.getRole()));
    }

    @GetMapping
    public List<UserDto> findAllUsers(){
        return userService.findAllUsers().stream()
                .map(user -> userMapper.toUserDto(user))
                .toList();
    }

    @GetMapping("/{id}")
    public UserDto findUser(@PathVariable Long id){
        return userMapper.toUserDto(userService.findUserById(id));
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
