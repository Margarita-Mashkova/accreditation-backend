package ru.ulstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.Variable;
import ru.ulstu.repository.VariableRepository;
import ru.ulstu.service.exception.VariableNotFoundException;
import ru.ulstu.util.validation.ValidatorUtil;

import java.util.List;
import java.util.Optional;

@Service
public class VariableService {
    @Autowired
    private VariableRepository variableRepository;
    @Autowired
    private ValidatorUtil validatorUtil;

    @Transactional(readOnly = true)
    public Variable findVariableByKey(String key){
        final Optional<Variable> variable = variableRepository.findById(key);
        return variable.orElseThrow(() -> new VariableNotFoundException(key));
    }

    @Transactional(readOnly = true)
    public List<Variable> findAllVariables(){
        return variableRepository.findAll();
    }

    @Transactional
    public Variable addVariable(String key, String name){
        Variable variable = new Variable(key, name);
        validatorUtil.validate(variable);
        return variableRepository.save(variable);
    }

    @Transactional
    public Variable editVariable(String key, String name){
        Variable variableDB = findVariableByKey(key);
        variableDB.setName(name);
        validatorUtil.validate(variableDB);
        return variableRepository.save(variableDB);
    }

    @Transactional
    public Variable deleteVariable(String key){
        final Variable variable = findVariableByKey(key);
        variableRepository.delete(variable);
        return variable;
    }

    @Transactional
    public void deleteAllVariables(){
        variableRepository.deleteAll();
    }
}