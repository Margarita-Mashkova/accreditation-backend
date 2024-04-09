package ru.ulstu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.dto.RuleDto;
import ru.ulstu.mapper.RuleMapper;
import ru.ulstu.service.RuleService;

import java.util.List;

@RestController
@RequestMapping("/rule")
public class RuleController {
    @Autowired
    private RuleService ruleService;
    @Autowired
    private RuleMapper ruleMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public List<RuleDto> findAllRules(){
        return ruleService.findAllRules().stream()
                .map(rule -> ruleMapper.toRuleDto(rule))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/by-indicator")
    public List<RuleDto> findAllRulesByIndicatorKey(@RequestParam String indicatorKey){
        return ruleService.findAllByIndicatorKey(indicatorKey).stream()
                .map(rule -> ruleMapper.toRuleDto(rule))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public RuleDto findRule(@PathVariable Long id){
        return ruleMapper.toRuleDto(ruleService.findRuleById(id));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public RuleDto createRule(@RequestBody RuleDto ruleDto){
        return ruleMapper.toRuleDto(ruleService.addRule(ruleDto.getMin(), ruleDto.getMax(), ruleDto.getScore()));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public RuleDto updateRule(@PathVariable Long id, @RequestParam(required = false) Integer min,
                              @RequestParam(required = false) Integer max, @RequestParam int score){
        return ruleMapper.toRuleDto(ruleService.editRule(id, min, max, score));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public void deleteRule(@PathVariable Long id){
        ruleService.deleteRule(id);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/by-indicator")
    public void deleteAllRulesByIndicatorKey(String indicatorKey){
        ruleService.deleteAllByIndicatorKey(indicatorKey);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/all")
    public void deleteAllRules(){
        ruleService.deleteAllRules();
    }
}
