package ru.ulstu.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.dto.ReportCalculationOpopDto;
import ru.ulstu.mapper.CalculationMapper;
import ru.ulstu.model.Calculation;
import ru.ulstu.model.OPOP;
import ru.ulstu.util.excel.style.ExcelCellStyle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private CalculationService calculationService;
    @Autowired
    private OPOPService opopService;
    @Autowired
    private CalculationMapper calculationMapper;

    @Transactional(readOnly = true)
    public ReportCalculationOpopDto makeCalculationOpopReport(Long opopId, Date date) {
        ReportCalculationOpopDto reportData = new ReportCalculationOpopDto();
        OPOP opop = opopService.findOpopById(opopId);

        // Получение рассчитаных значений для каждого показателя
        List<Calculation> calculationsData = calculationService.makeCalculationData(opopId, date);

        int sum = 0;
        float ap1 = 0f;
        float ap11 = 0f;
        String accreditationStatus = "";

        // Расчет суммарного количества баллов
        for (Calculation calc : calculationsData) {
            if (calc.getId().getIndicatorKey().equals("АП1")) {
                ap1 = calc.getScore();
            } else if (calc.getId().getIndicatorKey().equals("АП1.1")) {
                ap11 = calc.getScore();
            } else {
                sum += calc.getScore();
            }
        }
        if (ap11 != 0) {
            sum += ap1 / ap11;
        }

        // Определение статуса аккредитации
        if (sum >= 70 && (opop.getLevel().equals("Бакалавриат") || opop.getLevel().equals("Специалитет"))) {
            accreditationStatus = "Аккредитована";
        } else if (sum >= 60 && (!opop.getLevel().equals("Бакалавриат") && !opop.getLevel().equals("Специалитет"))) {
            accreditationStatus = "Аккредитована";
        } else {
            accreditationStatus = "Не аккредитована";
        }

        reportData.setCalculations(calculationsData.stream()
                .map(calculation -> calculationMapper.toCalculationDto(calculation))
                .toList());
        reportData.setSum(sum);
        reportData.setAccreditationStatus(accreditationStatus);

        return reportData;
    }

    @Transactional(readOnly = true)
    public void saveCalculationOpopReportExcel(Long opopId, Date date) {
        ReportCalculationOpopDto reportData = makeCalculationOpopReport(opopId, date);
        OPOP opop = opopService.findOpopById(opopId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = sdf.format(date);

        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Расчет показателей");
        ExcelCellStyle styles = new ExcelCellStyle(book);

        // Заголовок
        Row rowTitle = sheet.createRow(0);
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue("Значения показателей и соответствующих баллов");
        cellTitle.setCellStyle(styles.StyleBold);

        // Дата мониторинга
        Row rowDate = sheet.createRow(1);
        String[] dateData = {"Дата мониторинга", dateString};
        for (int i = 0; i < 2; i++) {
            Cell dateCell = rowDate.createCell(i);
            dateCell.setCellValue(dateData[i]);
            dateCell.setCellStyle(styles.StyleSimple);
        }

        // ОПОП
        Row rowOpop = sheet.createRow(2);
        String[] opopData = {"ОПОП", opop.getName()};
        for (int i = 0; i < 2; i++) {
            Cell opopCell = rowOpop.createCell(i);
            opopCell.setCellValue(opopData[i]);
            opopCell.setCellStyle(styles.StyleSimple);
        }

        // Заголовки таблицы
        String[] tableTitles = {"Наименование показателя", "Обозначение показателя", "Значение", "Баллы"};
        Row rowTitlesTable = sheet.createRow(4);
        for (int i = 0; i < 4; i++) {
            Cell titleTableCell = rowTitlesTable.createCell(i);
            titleTableCell.setCellValue(tableTitles[i]);
            titleTableCell.setCellStyle(styles.StyleBorderAndBold);
        }

        // Значения показателей
        int rowIndex = 5;
        for (var reportLine : reportData.getCalculations()) {
            Row rowIndicator = sheet.createRow(rowIndex);

            Cell cellIndicatorName = rowIndicator.createCell(0);
            cellIndicatorName.setCellValue(reportLine.getIndicatorName());
            cellIndicatorName.setCellStyle(styles.StyleBorder);

            Cell cellIndicatorKey = rowIndicator.createCell(1);
            cellIndicatorKey.setCellValue(reportLine.getId().getIndicatorKey());
            cellIndicatorKey.setCellStyle(styles.StyleBorderCenter);

            Cell cellValue = rowIndicator.createCell(2);
            cellValue.setCellValue(reportLine.getValue());
            cellValue.setCellStyle(styles.StyleBorderCenter);

            Cell cellScore = rowIndicator.createCell(3);
            cellScore.setCellValue(reportLine.getScore());
            cellScore.setCellStyle(styles.StyleBorderCenter);

            rowIndex++;
        }

        // Итог
        Row rowSummary = sheet.createRow(rowIndex);

        Cell cellSummary = rowSummary.createCell(0);
        cellSummary.setCellValue("Итого");
        cellSummary.setCellStyle(styles.StyleBorderAndBold);

        Cell cellEmpty = rowSummary.createCell(1);
        cellEmpty.setCellValue("");
        cellEmpty.setCellStyle(styles.StyleBorderAndBold);

        Cell cellAccreditationStatus = rowSummary.createCell(2);
        cellAccreditationStatus.setCellValue(reportData.getAccreditationStatus());
        cellAccreditationStatus.setCellStyle(styles.StyleBorderAndBold);

        Cell cellSum = rowSummary.createCell(3);
        cellSum.setCellValue(reportData.getSum() + " баллов");
        cellSum.setCellStyle(styles.StyleBorderAndBold);

        sheet.setDefaultColumnWidth(75);
        for (int i = 1; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        //Запись в файл
        //TODO: подумать о записи новых ОПОП в существующий файл
        Path downloadsPath = Paths.get(System.getProperty("user.home"), "Downloads");
        Path parentPath = downloadsPath.getParent();
        String downloadsFolderPath = parentPath.toString() + "/Downloads/";
        String pathToSave = downloadsFolderPath.replace("\\", "/");

        //TODO: подумать как называть файл
        try (OutputStream fileOut = new FileOutputStream(pathToSave +
                String.format("%s_%s.xlsx", opop.getName(), dateString))) {
            book.write(fileOut);
            book.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error writing " + e);
        }
    }
}
