package ru.ulstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.Indicator;
import ru.ulstu.model.Rule;
import ru.ulstu.repository.IndicatorRepository;
import ru.ulstu.service.exception.IndicatorNotFoundException;
import ru.ulstu.util.validation.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class IndicatorService {
    @Autowired
    private IndicatorRepository indicatorRepository;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private ValidatorUtil validatorUtil;
    private int pageAmount;

    @Transactional(readOnly = true)
    public Indicator findIndicatorByKey(String key){
        final Optional<Indicator> indicator = indicatorRepository.findById(key);
        return indicator.orElseThrow(() -> new IndicatorNotFoundException(key));
    }

    @Transactional(readOnly = true)
    public List<Indicator> findAllIndicators(){
        return indicatorRepository.findAll(Sort.by(Sort.Direction.ASC, "key"));
    }

    @Transactional(readOnly = true)
    public List<Indicator> findAllIndicatorsByPage(int page){
        Pageable pageWithThreeElements = PageRequest.of(page-1, 3, Sort.by(Sort.Direction.ASC, "key"));
        Page<Indicator> indicators = indicatorRepository.findAll(pageWithThreeElements);
        pageAmount = indicators.getTotalPages();
        List<Indicator> indicatorsList = new ArrayList<>();
        if(indicators.hasContent()) {
            indicatorsList = indicators.getContent();
        }
        return indicatorsList;
    }

    public int getAmountPages(){
        return pageAmount;
    }

    @Transactional
    public Indicator addIndicator(String key, String name, String formula, List<Rule> rules) {
        List<Rule> addedRules = new ArrayList<>();
        for (Rule rule : rules) {
            addedRules.add(ruleService.addRule(rule.getMin(), rule.getMax(), rule.getScore()));
        }
        Indicator indicator = new Indicator(key, name, formula, addedRules);
        validatorUtil.validate(indicator);
        return indicatorRepository.save(indicator);
    }

    @Transactional
    public Indicator editIndicator(String key, String name, String formula, List<Rule> rules){
        Indicator indicatorDB = findIndicatorByKey(key);
        indicatorDB.setName(name);
        indicatorDB.setFormula(formula);
        ruleService.deleteAllByIndicatorKey(key);
        List<Rule> addedRules = new ArrayList<>();
        for (Rule rule : rules) {
            addedRules.add(ruleService.addRule(rule.getMin(), rule.getMax(), rule.getScore()));
        }
        indicatorDB.setRules(addedRules);
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
