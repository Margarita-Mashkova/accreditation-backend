package ru.ulstu.service;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.*;
import ru.ulstu.repository.CalculationRepository;
import ru.ulstu.service.exception.CalculationNotFoundException;
import ru.ulstu.service.exception.ReportCalculationException;
import ru.ulstu.service.exception.ScoringRulesNotSetException;
import ru.ulstu.util.validation.ValidatorUtil;

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
    public Calculation addCalculation(CalculationId calculationId, float value, int score, String level){
        OPOP opop = opopService.findOpopById(calculationId.getOpopId());
        Indicator indicator = indicatorService.findIndicatorByKey(calculationId.getIndicatorKey());
        Calculation calculation = new Calculation(calculationId, indicator, opop, value, score, level);
        validatorUtil.validate(calculation);
        return calculationRepository.save(calculation);
    }

    @Transactional
    public Calculation calculateIndicator(CalculationId calculationId) {
        OPOP opop = opopService.findOpopById(calculationId.getOpopId());
        Indicator indicator = indicatorService.findIndicatorByKey(calculationId.getIndicatorKey());

        // Список значений переменных конкретной ОПОП в определенную дату
        List<Value> values = valueService.findValuesByOpopAndDate(calculationId.getOpopId(), calculationId.getDate());
        if(values.size() != 0) {
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

            // Выставления баллов полученному значению
            int score = 0;
            String level = "";
            List<Rule> scoreRules = indicator.getRules();
            if (scoreRules.size() > 1) {
                //TODO: подкорректировать условия на пограничные значения
                for (Rule rule : scoreRules) {
                    // менее min
                    if (rule.getMin() == null) {
                        if (value < rule.getMax()) {
                            score = rule.getScore();
                            level = rule.getLevel();
                        }
                    }
                    // max и более
                    else if (rule.getMax() == null) {
                        if (value >= rule.getMin()) {
                            score = rule.getScore();
                            level = rule.getLevel();
                        }
                    }
                    // от min и до max
                    else {
                        if ((value >= rule.getMin()) && (value < rule.getMax())) {
                            score = rule.getScore();
                            level = rule.getLevel();
                        }
                    }
                }
            } else {
                throw new ScoringRulesNotSetException(indicator.getKey());
            }

            Calculation calculation = new Calculation(calculationId, indicator, opop, value, score, level);
            validatorUtil.validate(calculation);
            return calculationRepository.save(calculation);
        }
        else {
            throw new ReportCalculationException();
        }
    }

    @Transactional
    public List<Calculation> makeCalculationReport(Long opopId, Date date) {
        List<Indicator> indicatorList = indicatorService.findAllIndicators();
        List<Calculation> calculations = new ArrayList<>();
        for (Indicator indicator : indicatorList) {
            CalculationId calculationId = new CalculationId(opopId, indicator.getKey(), date);
            Calculation calculatedIndicator = calculateIndicator(calculationId);
            calculations.add(calculatedIndicator);
        }
        return calculations;
    }

    @Transactional
    public Calculation updateCalculation(CalculationId calculationId, float value, int score, String level){
        Calculation calculationDB = findCalculation(calculationId);
        OPOP opop = opopService.findOpopById(calculationId.getOpopId());
        Indicator indicator = indicatorService.findIndicatorByKey(calculationId.getIndicatorKey());
        calculationDB.setOpop(opop);
        calculationDB.setIndicator(indicator);
        calculationDB.setValue(value);
        calculationDB.setScore(score);
        calculationDB.setLevel(level);
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
