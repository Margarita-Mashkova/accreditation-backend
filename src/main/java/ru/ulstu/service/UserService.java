package ru.ulstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.User;
import ru.ulstu.model.enums.Role;
import ru.ulstu.repository.UserRepository;
import ru.ulstu.service.exception.UserLoginAlreadyExistsException;
import ru.ulstu.service.exception.UserNotFoundException;
import ru.ulstu.service.exception.WrongLoginOrPasswordException;
import ru.ulstu.util.jwt.JwtProvider;
import ru.ulstu.util.validation.ValidatorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ValidatorUtil validatorUtil;
    @Autowired
    private JwtProvider jwtProvider;
    private int pageAmount;

    @Transactional(readOnly = true)
    public List<Role> findAllRoles(){
        return new ArrayList<Role>(Arrays.asList(Role.values()));
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers(){
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "surname"));
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsersByPage(int page) {
        Pageable pageWithFiveElements = PageRequest.of(page-1, 5, Sort.by(Sort.Direction.ASC, "id"));
        Page<User> users = userRepository.findAll(pageWithFiveElements);
        pageAmount = users.getTotalPages();
        List<User> userList = new ArrayList<>();
        if(users.hasContent()) {
            userList = users.getContent();
        }
        return userList;
    }

    public int getAmountPages(){
        return pageAmount;
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        final Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public User findUserByLogin(String login) {
        User user = userRepository.findByLogin(login);
        return user;
    }

    @Transactional
    public String authorization(User user) {
        User userDB = findUserByLogin(user.getLogin());
        if (userDB != null && passwordEncoder.matches(user.getPassword(), userDB.getPassword())) {
            return jwtProvider.generateAccessToken(user);
        } else {
            throw new WrongLoginOrPasswordException();
        }
    }

    @Transactional
    public User addUser(String name, String surname, String patronymic, String login, String password,
                        Role role) {
        if (login != null && findUserByLogin(login) == null) {
            User user = new User(name, surname, patronymic, login, passwordEncoder.encode(password), role);
            /*if (opops != null){
                user.setOpops(opops.stream()
                        .map(opop_id -> opopService.findOpopById(opop_id))
                        .toList());
            }*/
            validatorUtil.validate(user);
            return userRepository.save(user);
        } else {
            throw new UserLoginAlreadyExistsException(login);
        }
    }

    @Transactional
    public User updateUser(Long id, String name, String surname, String patronymic, String login, String password,
                           Role role) {
        User userDB = findUserById(id);
        userDB.setName(name);
        userDB.setSurname(surname);
        userDB.setPatronymic(patronymic);
        if (login != null) {
            if (findUserByLogin(login) == null || userDB.getLogin().equals(login)) {
                userDB.setLogin(login);
            } else {
                throw new UserLoginAlreadyExistsException(login);
            }
        }
        if(password != null && !password.equals("")) {
            userDB.setPassword(passwordEncoder.encode(password));
        }
        if (role != null)
            userDB.setRole(role);
        /*if (opops != null){
            userDB.setOpops(opops.stream()
                    .map(opop_id -> opopService.findOpopById(opop_id))
                    .toList());
        }*/
        validatorUtil.validate(userDB);
        return userRepository.save(userDB);
    }

    @Transactional
    public User deleteUser(Long id){
        final User user = findUserById(id);
        userRepository.delete(user);
        return user;
    }

    @Transactional
    public void deleteAllUsers(){
        userRepository.deleteAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }
}