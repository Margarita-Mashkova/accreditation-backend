package ru.ulstu.service;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.*;
import ru.ulstu.repository.CalculationRepository;
import ru.ulstu.service.exception.CalculationNotFoundException;
import ru.ulstu.util.validation.ValidatorUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.*;

@Service
public class CalculationService {
    @Autowired
    private CalculationRepository calculationRepository;
    @Autowired
    private ValidatorUtil validatorUtil;
    @Autowired
    private OPOPService opopService;
    @Autowired
    private ValueService valueService;
    @Autowired
    private IndicatorService indicatorService;

    @Transactional(readOnly = true)
    public List<Calculation> findAllCalculations(){
        return calculationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Calculation findCalculation(CalculationId calculationId){
        final Optional<Calculation> calculation = calculationRepository.findById(calculationId);
        return calculation.orElseThrow(() -> new CalculationNotFoundException(calculationId));
    }

    @Transactional(readOnly = true)
    public List<Calculation> findCalculationsByOpopAndDate(Long opopId, Date date){
        return calculationRepository.findAllByOpopIdAndIdDateOrderByIdIndicatorKey(opopId, date);
    }

    @Transactional(readOnly = true)
    public List<Calculation> findCalculationsByPeriod(Long opopId, Date dateStart, Date dateEnd){
        return calculationRepository.findAllByOpopIdAndIdDateBetweenOrderByIdIndicatorKeyAscIdDateAsc(opopId, dateStart, dateEnd);
    }

    @Transactional
    public Calculation addCalculation(CalculationId calculationId, float value, int score){
        OPOP opop = opopService.findOpopById(calculationId.getOpopId());
        Indicator indicator = indicatorService.findIndicatorByKey(calculationId.getIndicatorKey());
        Calculation calculation = new Calculation(calculationId, indicator, opop, value, score);
        validatorUtil.validate(calculation);
        return calculationRepository.save(calculation);
    }

    @Transactional
    public List<Value> makeCalculation(CalculationId calculationId) {
        OPOP opop = opopService.findOpopById(calculationId.getOpopId());
        Indicator indicator = indicatorService.findIndicatorByKey(calculationId.getIndicatorKey());

        // Список значений переменных конкретной ОПОП в определенную дату
        List<Value> values = valueService.findValuesByOpopAndDate(calculationId.getOpopId(), calculationId.getDate());
        // Список наименований переменных
        Set<String> vars = new HashSet<>();
        for (Value val : values) {
            vars.add(val.getId().getVariableKey());
        }
        // Формула вычисления значения показателя
        String formula = indicator.getFormula();
        // Создание выражения
        Expression expression = new ExpressionBuilder(formula)
                .variables(vars)
                .build();
        // Присваивание значений переменным в выражении
        for (Value val : values) {
            expression.setVariable(val.getId().getVariableKey(), val.getValue());
        }
        // Вычисление значения показателя
        float value = ((Double) expression.evaluate()).floatValue();

        // 2 способ парсить формулу
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");
//        Map<String, Object> vars = new HashMap<String, Object>();
//        for (Value val : values) {
//            vars.put(val.getId().getVariableKey(), val.getValue());
//        }
//        float value = ((Double) engine.eval(formula, new SimpleBindings(vars))).floatValue();

        //TODO: правила выставления балов
        int score = 60;
        Calculation calculation = new Calculation(calculationId, indicator, opop, value, score);
        //validatorUtil.validate(calculation);
        //return calculationRepository.save(calculation);
        return values;
    }

    @Transactional
    public Calculation updateCalculation(CalculationId calculationId, float value, int score){
        Calculation calculationDB = findCalculation(calculationId);
        OPOP opop = opopService.findOpopById(calculationId.getOpopId());
        Indicator indicator = indicatorService.findIndicatorByKey(calculationId.getIndicatorKey());
        calculationDB.setOpop(opop);
        calculationDB.setIndicator(indicator);
        calculationDB.setValue(value);
        calculationDB.setScore(score);
        validatorUtil.validate(calculationDB);
        return calculationRepository.save(calculationDB);
    }

    @Transactional
    public Calculation deleteCalculation(CalculationId calculationId){
        Calculation calculation = findCalculation(calculationId);
        calculationRepository.delete(calculation);
        return calculation;
    }

    @Transactional
    public void deleteAllCalculations(){
        calculationRepository.deleteAll();
    }
}
