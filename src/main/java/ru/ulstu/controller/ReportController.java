package ru.ulstu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ulstu.dto.CalculationDto;
import ru.ulstu.mapper.CalculationMapper;
import ru.ulstu.service.CalculationService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private CalculationService calculationService;
    @Autowired
    private CalculationMapper calculationMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public List<CalculationDto> makeCalculationReport(@RequestParam Long opopId,
                                                      @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd") Date date){
        return calculationService.makeCalculationReport(opopId, date).stream()
                .map(calculation -> calculationMapper.toCalculationDto(calculation))
                .toList();
    }
}
