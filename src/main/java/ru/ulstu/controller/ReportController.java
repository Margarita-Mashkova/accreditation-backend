package ru.ulstu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ulstu.dto.CalculationDto;
import ru.ulstu.dto.ReportCalculationOpopDto;
import ru.ulstu.mapper.CalculationMapper;
import ru.ulstu.service.CalculationService;
import ru.ulstu.service.ReportService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/calculation-opop")
    public ReportCalculationOpopDto makeCalculationOpopReport(
            @RequestParam Long opopId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return reportService.makeCalculationOpopReport(opopId, date);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/save")
    public void saveCalculationOpopReportExcel(@RequestParam Long opopId,
                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        reportService.saveCalculationOpopReportExcel(opopId, date);
    }
}
