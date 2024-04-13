package ru.ulstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.OPOP;
import ru.ulstu.model.User;
import ru.ulstu.repository.OPOPRepository;
import ru.ulstu.service.exception.OPOPNotFoundException;
import ru.ulstu.util.validation.ValidatorUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
    private int pageAmount;

    @Transactional(readOnly = true)
    public OPOP findOpopById(Long id){
        final Optional<OPOP> opop = opopRepository.findById(id);
        return opop.orElseThrow(() -> new OPOPNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<OPOP> findAllOpops(){
        return opopRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Transactional(readOnly = true)
    public List<OPOP> findAllOpopsByPage(int page){
        Pageable pageWithFiveElements = PageRequest.of(page-1, 5, Sort.by(Sort.Direction.ASC, "id"));
        Page<OPOP> opops = opopRepository.findAll(pageWithFiveElements);
        pageAmount = opops.getTotalPages();
        List<OPOP> opopList = new ArrayList<>();
        if(opops.hasContent()) {
            opopList = opops.getContent();
        }
        return opopList;
    }

    public int getAmountPages(){
        return pageAmount;
    }

    public List<String> findAllOpopLevels(){
        return Arrays.asList("Бакалавриат", "Специалитет", "Магистратура", "Ординатура",
                "Ассистентура-стажировка");
    }

    @Transactional
    public OPOP addOpop(String name, String level, Long userId){
        final User user = userService.findUserById(userId);
        OPOP opop = new OPOP(name, level, user);
        validatorUtil.validate(opop);
        return opopRepository.save(opop);
    }

    @Transactional
    public OPOP editOPOP(Long id, String name, String level, Long userId){
        OPOP opopDB = findOpopById(id);
        opopDB.setName(name);
        opopDB.setLevel(level);
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
