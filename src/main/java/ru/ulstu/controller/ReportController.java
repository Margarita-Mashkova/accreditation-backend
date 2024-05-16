package ru.ulstu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ulstu.dto.ReportAnalysisOpopDto;
import ru.ulstu.dto.ReportCalculationOpopDto;
import ru.ulstu.dto.ReportFileDto;
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
    @GetMapping(value = "/calculation-opop/save")
    public ReportFileDto saveCalculationOpopReportExcel(@RequestParam Long opopId,
                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return reportService.saveCalculationOpopReportExcel(opopId, date);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/analysis")
    public List<ReportAnalysisOpopDto> makeAnalysisReport(@RequestParam Long opopId,
                                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
                                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd) {
        return reportService.makeAnalysisReport(opopId, dateStart, dateEnd);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/analysis/save")
    public ReportFileDto saveAnalysisReportExcel(@RequestParam Long opopId,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd) {
        return reportService.saveAnalysisReportExcel(opopId, dateStart, dateEnd);
    }
}
