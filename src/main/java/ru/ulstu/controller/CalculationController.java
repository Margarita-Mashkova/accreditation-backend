package ru.ulstu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.dto.CalculationDto;
import ru.ulstu.dto.CalculationIdDto;
import ru.ulstu.mapper.CalculationMapper;
import ru.ulstu.service.CalculationService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/calculation")
public class CalculationController {
    @Autowired
    private CalculationService calculationService;
    @Autowired
    private CalculationMapper calculationMapper;

    @GetMapping("/all")
    public List<CalculationDto> findAllCalculations(){
        return calculationService.findAllCalculations().stream()
                .map(calculation -> calculationMapper.toCalculationDto(calculation))
                .toList();
    }

    @GetMapping("/filtered")
    public List<CalculationDto> findCalculationsByOpopAndDate(
            @RequestParam Long opopId, @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd") Date date){
        return calculationService.findCalculationsByOpopAndDate(opopId, date).stream()
                .map(calculation -> calculationMapper.toCalculationDto(calculation))
                .toList();
    }

    @GetMapping
    public CalculationDto findCalculation(@RequestParam Long opopId, @RequestParam String indicatorKey,
                                          @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd") Date date){
        CalculationIdDto calculationIdDto = new CalculationIdDto(opopId, indicatorKey, date);
        return calculationMapper.toCalculationDto(calculationService
                .findCalculation(calculationMapper.fromCalculationIdDto(calculationIdDto)));
    }

    @PostMapping
    public CalculationDto createCalculation(@RequestBody CalculationDto calculationDto){
        return calculationMapper.toCalculationDto(calculationService.addCalculation(
                calculationMapper.fromCalculationIdDto(calculationDto.getId()),
                calculationDto.getValue(), calculationDto.getScore()));
    }

    @PutMapping
    public CalculationDto editCalculation(@RequestBody CalculationDto calculationDto){
        return calculationMapper.toCalculationDto(calculationService.addCalculation(
                calculationMapper.fromCalculationIdDto(calculationDto.getId()),
                calculationDto.getValue(), calculationDto.getScore()));
    }

    @DeleteMapping
    public void deleteCalculation(@RequestBody CalculationIdDto calculationIdDto){
        calculationService.deleteCalculation(calculationMapper.fromCalculationIdDto(calculationIdDto));
    }

    @DeleteMapping("/all")
    public void deleteAllCalculations(){
        calculationService.deleteAllCalculations();
    }
}
