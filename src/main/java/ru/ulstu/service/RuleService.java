package ru.ulstu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.Rule;
import ru.ulstu.repository.RuleRepository;
import ru.ulstu.service.exception.RuleNotFoundException;
import ru.ulstu.util.validation.ValidatorUtil;

import java.util.List;
import java.util.Optional;

@Service
public class RuleService {
    @Autowired
    private RuleRepository ruleRepository;
    @Autowired
    private ValidatorUtil validatorUtil;

    @Transactional(readOnly = true)
    public List<Rule> findAllRules(){
        return ruleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Rule findRuleById(Long id){
        final Optional<Rule> rule = ruleRepository.findById(id);
        return rule.orElseThrow(() -> new RuleNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Rule> findAllByIndicatorKey(String indicatorKey){
        return ruleRepository.findAllByIndicatorKey(indicatorKey);
    }

    @Transactional
    public Rule addRule(Integer min, Integer max, int score){
        final Rule rule = new Rule(min, max, score);
        validatorUtil.validate(rule);
        return ruleRepository.save(rule);
    }

    @Transactional
    public Rule editRule(Long id, Integer min, Integer max, int score){
        Rule ruleDB = findRuleById(id);
        ruleDB.setMin(min);
        ruleDB.setMax(max);
        ruleDB.setScore(score);
        validatorUtil.validate(ruleDB);
        return ruleRepository.save(ruleDB);
    }

    @Transactional
    public Rule deleteRule(Long id){
        Rule rule = findRuleById(id);
        ruleRepository.delete(rule);
        return rule;
    }

    @Transactional
    public List<Rule> deleteAllByIndicatorKey(String indicatorKey){
        List<Rule> rules = findAllByIndicatorKey(indicatorKey);
        rules.forEach(rule -> ruleRepository.delete(rule));
        return rules;
    }

    @Transactional
    public void deleteAllRules(){
        ruleRepository.deleteAll();
    }
}
