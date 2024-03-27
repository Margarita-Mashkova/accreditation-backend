package ru.ulstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.OPOP;
import ru.ulstu.model.User;
import ru.ulstu.repository.OPOPRepository;
import ru.ulstu.service.exception.OPOPNotFoundException;
import ru.ulstu.util.validation.ValidatorUtil;

import java.util.List;
import java.util.Optional;

@Service
public class OPOPService {
    @Autowired
    private OPOPRepository opopRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ValidatorUtil validatorUtil;

    @Transactional(readOnly = true)
    public OPOP findOpopById(Long id){
        final Optional<OPOP> opop = opopRepository.findById(id);
        return opop.orElseThrow(() -> new OPOPNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<OPOP> findAllOpops(){
        return opopRepository.findAll();
    }

    @Transactional
    public OPOP addOpop(String name, Long userId){
        final User user = userService.findUserById(userId);
        OPOP opop = new OPOP(name, user);
        validatorUtil.validate(opop);
        return opopRepository.save(opop);
    }

    @Transactional
    public OPOP editOPOP(Long id, String name, Long userId){
        OPOP opopDB = findOpopById(id);
        opopDB.setName(name);
        User user = userService.findUserById(userId);
        opopDB.setUser(user);
        validatorUtil.validate(opopDB);
        return opopRepository.save(opopDB);
    }

    @Transactional
    public OPOP deleteOpop(Long id){
        final OPOP opop = findOpopById(id);
        opopRepository.delete(opop);
        return opop;
    }

    @Transactional
    public void deleteAllOpops(){
        opopRepository.deleteAll();
    }
}
