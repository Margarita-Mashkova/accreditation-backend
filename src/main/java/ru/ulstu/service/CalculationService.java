package ru.ulstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.Calculation;
import ru.ulstu.model.CalculationId;
import ru.ulstu.model.Indicator;
import ru.ulstu.model.OPOP;
import ru.ulstu.repository.CalculationRepository;
import ru.ulstu.service.exception.CalculationNotFoundException;
import ru.ulstu.util.validation.ValidatorUtil;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CalculationService {
    @Autowired
    private CalculationRepository calculationRepository;
    @Autowired
    private ValidatorUtil validatorUtil;
    @Autowired
    private OPOPService opopService;
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
        return calculationRepository.findAllByOpopIdAndIdDate(opopId, date);
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
