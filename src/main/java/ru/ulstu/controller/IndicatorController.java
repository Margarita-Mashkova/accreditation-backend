package ru.ulstu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.dto.IndicatorDto;
import ru.ulstu.mapper.IndicatorMapper;
import ru.ulstu.service.IndicatorService;

import java.util.List;

@RestController
@RequestMapping("/indicator")
public class IndicatorController {
    @Autowired
    private IndicatorService indicatorService;
    @Autowired
    private IndicatorMapper indicatorMapper;

    @GetMapping
    public List<IndicatorDto> findAllIndicators(){
        return indicatorService.findAllIndicators().stream()
                .map(indicator -> indicatorMapper.toIndicatorDto(indicator))
                .toList();
    }

    @GetMapping("/{key}")
    public IndicatorDto findIndicator(@PathVariable String key){
        return indicatorMapper.toIndicatorDto(indicatorService.findIndicatorByKey(key));
    }

    @PostMapping
    public IndicatorDto createIndicator(@RequestBody IndicatorDto indicatorDto){
        return indicatorMapper.toIndicatorDto(indicatorService.addIndicator(indicatorDto.getKey(),
                indicatorDto.getName(), indicatorDto.getFormula()));
    }

    @PutMapping
    public IndicatorDto updateIndicator(@RequestBody IndicatorDto indicatorDto){
        return indicatorMapper.toIndicatorDto(indicatorService.editIndicator(indicatorDto.getKey(),
                indicatorDto.getName(), indicatorDto.getFormula()));
    }

    @DeleteMapping("/{key}")
    public void deleteIndicator(@PathVariable String key){
        indicatorService.deleteIndicator(key);
    }

    @DeleteMapping
    public void deleteAllIndicators(){
        indicatorService.deleteAllIndicators();
    }
}
