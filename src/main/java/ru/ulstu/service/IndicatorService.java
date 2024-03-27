package ru.ulstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.Indicator;
import ru.ulstu.repository.IndicatorRepository;
import ru.ulstu.service.exception.IndicatorNotFoundException;
import ru.ulstu.util.validation.ValidatorUtil;

import java.util.List;
import java.util.Optional;

@Service
public class IndicatorService {
    @Autowired
    private IndicatorRepository indicatorRepository;
    @Autowired
    private ValidatorUtil validatorUtil;

    @Transactional(readOnly = true)
    public Indicator findIndicatorByKey(String key){
        final Optional<Indicator> indicator = indicatorRepository.findById(key);
        return indicator.orElseThrow(() -> new IndicatorNotFoundException(key));
    }

    @Transactional(readOnly = true)
    public List<Indicator> findAllIndicators(){
        return indicatorRepository.findAll();
    }

    @Transactional
    public Indicator addIndicator(String key, String name, String formula){
        Indicator indicator = new Indicator(key, name, formula);
        validatorUtil.validate(indicator);
        return indicatorRepository.save(indicator);
    }

    @Transactional
    public Indicator editIndicator(String key, String name, String formula){
        Indicator indicatorDB = findIndicatorByKey(key);
        indicatorDB.setName(name);
        indicatorDB.setFormula(formula);
        validatorUtil.validate(indicatorDB);
        return indicatorRepository.save(indicatorDB);
    }

    @Transactional
    public Indicator deleteIndicator(String key){
        final Indicator indicator = findIndicatorByKey(key);
        indicatorRepository.delete(indicator);
        return indicator;
    }

    @Transactional
    public void deleteAllIndicators(){
        indicatorRepository.deleteAll();
    }
}
