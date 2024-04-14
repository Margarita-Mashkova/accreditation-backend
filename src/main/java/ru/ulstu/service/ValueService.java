package ru.ulstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.OPOP;
import ru.ulstu.model.Value;
import ru.ulstu.model.ValueId;
import ru.ulstu.model.Variable;
import ru.ulstu.repository.ValueRepository;
import ru.ulstu.service.exception.ValueNotFoundException;
import ru.ulstu.util.validation.ValidatorUtil;

import java.util.*;

@Service
public class ValueService {
    @Autowired
    private ValueRepository valueRepository;
    @Autowired
    private ValidatorUtil validatorUtil;
    @Autowired
    private OPOPService opopService;
    @Autowired
    private VariableService variableService;

    @Transactional(readOnly = true)
    public List<Value> findAllValues(){
        return valueRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Value findValue(ValueId valueId){
        final Optional<Value> value = valueRepository.findById(valueId);
        return value.orElseThrow(() -> new ValueNotFoundException(valueId));
    }

    @Transactional(readOnly = true)
    public List<Value> findValuesByOpopAndDate(Long opopId, Date date){
        return valueRepository.findAllByOpopIdAndIdDate(opopId, date);
    }

    @Transactional(readOnly = true)
    public List<Date> findDatesByOpop(Long opopId){
        List<Value> values = valueRepository.findAllByOpopId(opopId);
        List<Date> datesWithValues = new ArrayList<>();
        for (var value : values) {
            if (!datesWithValues.contains(value.getId().getDate())) {
                datesWithValues.add(value.getId().getDate());
            }
        }
        datesWithValues.sort(Collections.reverseOrder());
        return datesWithValues;
    }

    @Transactional
    public List<Value> addValuesList(List<Value> values){
        return values.stream()
                .map(value -> addValue(value.getId(), value.getValue()))
                .toList();
    }

    @Transactional
    public Value addValue(ValueId valueId, float val){
        OPOP opop = opopService.findOpopById(valueId.getOpopId());
        Variable variable = variableService.findVariableByKey(valueId.getVariableKey());
        Value value = new Value(valueId, opop, variable, val);
        validatorUtil.validate(value);
        return valueRepository.save(value);
    }

    @Transactional
    public Value updateValue(ValueId valueId, float val){
        Value valueDB = findValue(valueId);
        OPOP opop = opopService.findOpopById(valueId.getOpopId());
        Variable variable = variableService.findVariableByKey(valueId.getVariableKey());
        valueDB.setValue(val);
        valueDB.setOpop(opop);
        valueDB.setVariable(variable);
        validatorUtil.validate(valueDB);
        return valueRepository.save(valueDB);
    }

    @Transactional
    public Value deleteValue(ValueId valueId){
        final Value value = findValue(valueId);
        valueRepository.delete(value);
        return value;
    }

    @Transactional
    public void deleteAllValues(){
        valueRepository.deleteAll();
    }
}
