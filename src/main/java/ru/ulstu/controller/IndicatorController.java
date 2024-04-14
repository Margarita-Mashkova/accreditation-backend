package ru.ulstu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.dto.IndicatorDto;
import ru.ulstu.mapper.IndicatorMapper;
import ru.ulstu.mapper.RuleMapper;
import ru.ulstu.service.IndicatorService;

import java.util.List;

@RestController
@RequestMapping("/indicator")
public class IndicatorController {
    @Autowired
    private IndicatorService indicatorService;
    @Autowired
    private IndicatorMapper indicatorMapper;
    @Autowired
    private RuleMapper ruleMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public List<IndicatorDto> findAllIndicators(){
        return indicatorService.findAllIndicators().stream()
                .map(indicator -> indicatorMapper.toIndicatorDto(indicator))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/page")
    public List<IndicatorDto> findAllIndicatorsByPage(@RequestParam int pageNumber){
        return indicatorService.findAllIndicatorsByPage(pageNumber).stream()
                .map(indicator -> indicatorMapper.toIndicatorDto(indicator))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/pages")
    public int getAmountPages(){
        return indicatorService.getAmountPages();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{key}")
    public IndicatorDto findIndicator(@PathVariable String key){
        return indicatorMapper.toIndicatorDto(indicatorService.findIndicatorByKey(key));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public IndicatorDto createIndicator(@RequestBody IndicatorDto indicatorDto){
        return indicatorMapper.toIndicatorDto(indicatorService.addIndicator(indicatorDto.getKey(),
                indicatorDto.getName(), indicatorDto.getFormula(),
                indicatorDto.isBoolType(), indicatorDto.isBoolValue(),
                indicatorDto.getRules().stream()
                        .map(ruleDto -> ruleMapper.fromRuleDto(ruleDto))
                        .toList()));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping
    public IndicatorDto updateIndicator(@RequestBody IndicatorDto indicatorDto){
        return indicatorMapper.toIndicatorDto(indicatorService.editIndicator(indicatorDto.getKey(),
                indicatorDto.getName(), indicatorDto.getFormula(),
                indicatorDto.isBoolType(), indicatorDto.isBoolValue(),
                indicatorDto.getRules().stream()
                        .map(ruleDto -> ruleMapper.fromRuleDto(ruleDto))
                        .toList()));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{key}")
    public void deleteIndicator(@PathVariable String key){
        indicatorService.deleteIndicator(key);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping
    public void deleteAllIndicators(){
        indicatorService.deleteAllIndicators();
    }
}
