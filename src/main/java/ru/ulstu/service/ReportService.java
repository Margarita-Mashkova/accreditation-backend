package ru.ulstu.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.dto.AnalysisValuesDto;
import ru.ulstu.dto.ReportAnalysisOpopDto;
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
import java.util.*;

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

        reportData.setOpopName(opop.getName());
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = sdf.format(date);

        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Расчет показателей");
        ExcelCellStyle styles = new ExcelCellStyle(book);

        // Заголовок
        Row rowTitle = sheet.createRow(0);
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue("Значения показателей и соответствующих баллов");
        cellTitle.setCellStyle(styles.StyleBoldFont);

        // Дата мониторинга
        Row rowDate = sheet.createRow(1);
        String[] dateData = {"Дата мониторинга", dateString};
        for (int i = 0; i < 2; i++) {
            Cell dateCell = rowDate.createCell(i);
            dateCell.setCellValue(dateData[i]);
            dateCell.setCellStyle(styles.StyleFontSimple);
        }

        // ОПОП
        Row rowOpop = sheet.createRow(2);
        String[] opopData = {"ОПОП", reportData.getOpopName()};
        for (int i = 0; i < 2; i++) {
            Cell opopCell = rowOpop.createCell(i);
            opopCell.setCellValue(opopData[i]);
            opopCell.setCellStyle(styles.StyleFontSimple);
        }

        // Заголовки таблицы
        String[] tableTitles = {"Наименование показателя", "Обозначение показателя", "Значение", "Баллы"};
        Row rowTitlesTable = sheet.createRow(4);
        for (int i = 0; i < 4; i++) {
            Cell titleTableCell = rowTitlesTable.createCell(i);
            titleTableCell.setCellValue(tableTitles[i]);
            titleTableCell.setCellStyle(styles.StyleBorderBoldFontColorAllCenter);
        }

        // Значения показателей
        int rowIndex = 5;
        for (var reportLine : reportData.getCalculations()) {
            Row rowIndicator = sheet.createRow(rowIndex);

            Cell cellIndicatorName = rowIndicator.createCell(0);
            cellIndicatorName.setCellValue(reportLine.getIndicatorName());
            cellIndicatorName.setCellStyle(styles.StyleBorderSimpleFontVerticalCenterWrap);

            Cell cellIndicatorKey = rowIndicator.createCell(1);
            cellIndicatorKey.setCellValue(reportLine.getId().getIndicatorKey());
            cellIndicatorKey.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            Cell cellValue = rowIndicator.createCell(2);
            cellValue.setCellValue(reportLine.getValue());
            cellValue.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            Cell cellScore = rowIndicator.createCell(3);
            cellScore.setCellValue(reportLine.getScore());
            cellScore.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            rowIndex++;
        }

        // Итог
        Row rowSummary = sheet.createRow(rowIndex);

        Cell cellSummary = rowSummary.createCell(0);
        cellSummary.setCellValue("Итого");
        cellSummary.setCellStyle(styles.StyleBorderBoldFontColorAllCenter);

        Cell cellEmpty = rowSummary.createCell(1);
        cellEmpty.setCellValue("");
        cellEmpty.setCellStyle(styles.StyleBorderBoldFontColorAllCenter);

        Cell cellAccreditationStatus = rowSummary.createCell(2);
        cellAccreditationStatus.setCellValue(reportData.getAccreditationStatus());
        cellAccreditationStatus.setCellStyle(styles.StyleBorderBoldFontColorAllCenter);

        Cell cellSum = rowSummary.createCell(3);
        cellSum.setCellValue(reportData.getSum() + " баллов");
        cellSum.setCellStyle(styles.StyleBorderBoldFontColorAllCenter);

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

        try (OutputStream fileOut = new FileOutputStream(pathToSave +
                String.format("Расчет_%s_%s.xlsx", reportData.getOpopName(), dateString))) {
            book.write(fileOut);
            book.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error writing " + e);
        }
    }

    @Transactional(readOnly = true)
    public List<ReportAnalysisOpopDto> makeAnalysisReport(Long opopId, Date dateStart, Date dateEnd) {
        List<ReportAnalysisOpopDto> reportData = new ArrayList<>();
        OPOP opop = opopService.findOpopById(opopId);

        List<Calculation> calculations = calculationService.findCalculationsByPeriod(opopId, dateStart, dateEnd);
        //<indicatorKey, indicatorName, (dates, values, scores, planned)>
        Map<String, AnalysisValuesDto> indicatorDict = new HashMap<>();
        for (Calculation calc : calculations) {
            AnalysisValuesDto analysisValues;
            String indicatorKey = calc.getId().getIndicatorKey();
            if (!indicatorDict.containsKey(indicatorKey)) {
                analysisValues = new AnalysisValuesDto();
                analysisValues.setIndicatorName(calc.getIndicator().getName());

                List<Date> dates = new ArrayList<>();
                dates.add(calc.getId().getDate());
                analysisValues.setDates(dates);

                List<Float> values = new ArrayList<>();
                values.add(calc.getValue());
                analysisValues.setValues(values);

                List<Integer> scores = new ArrayList<>();
                scores.add(calc.getScore());
                analysisValues.setScores(scores);

                List<Boolean> planned = new ArrayList<>();
                planned.add(calc.isPlanned());
                analysisValues.setPlanned(planned);

                indicatorDict.put(indicatorKey, analysisValues);
            } else {
                analysisValues = indicatorDict.get(indicatorKey);
                analysisValues.getDates().add(calc.getId().getDate());
                analysisValues.getValues().add(calc.getValue());
                analysisValues.getScores().add(calc.getScore());
                analysisValues.getPlanned().add(calc.isPlanned());
            }
        }

        for (String indicatorKey : indicatorDict.keySet()) {
            AnalysisValuesDto analysisValues = indicatorDict.get(indicatorKey);
            ReportAnalysisOpopDto reportItem = new ReportAnalysisOpopDto();

            reportItem.setOpopName(opop.getName());
            reportItem.setIndicatorKey(indicatorKey);
            reportItem.setIndicatorName(analysisValues.getIndicatorName());
            reportItem.setDates(analysisValues.getDates());
            reportItem.setValues(analysisValues.getValues());
            reportItem.setScores(analysisValues.getScores());
            reportItem.setPlanned(analysisValues.getPlanned());

            reportData.add(reportItem);
        }

        reportData.sort(Comparator.comparing(ReportAnalysisOpopDto::getIndicatorKey));
        return reportData;
    }

    @Transactional(readOnly = true)
    public void saveAnalysisReportExcel(Long opopId, Date dateStart, Date dateEnd) {
        List<ReportAnalysisOpopDto> reportData = makeAnalysisReport(opopId, dateStart, dateEnd);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String dateStartString = sdf.format(dateStart);
        String dateEndString = sdf.format(dateEnd);

        Workbook book = new XSSFWorkbook();
        ExcelCellStyle styles = new ExcelCellStyle(book);

        // Сводная таблица
        makeTotalTableAnalysis(book, styles, opopId, dateStart, dateEnd, reportData.get(0).getOpopName());

        // Лист со всеми показателями
        Sheet sheetAllIndicators = book.createSheet("Все показатели");
        makeTitleForTableAnalysis(sheetAllIndicators, styles, reportData.get(0).getOpopName(), dateStartString, dateEndString);

        int rowIndex = 4;
        for (ReportAnalysisOpopDto reportItem : reportData) {
            // На общем листе данные показателя
            int currentRowIndex = addDataToTableAnalysis(sheetAllIndicators, styles, reportItem, rowIndex);
            rowIndex = currentRowIndex + 2;

            // На отдельном листе
            Sheet sheetIndicator = book.createSheet(reportItem.getIndicatorKey());
            // Заголовок
            Row rowTitleIndicator = sheetIndicator.createRow(0);
            Cell cellTitleIndicator = rowTitleIndicator.createCell(0);
            cellTitleIndicator.setCellValue(String.format("Сравнение значений показателя %s по годам",
                    reportItem.getIndicatorKey()));
            cellTitleIndicator.setCellStyle(styles.StyleBoldFont);
            // Данные показателя
            addDataToTableAnalysis(sheetIndicator, styles, reportItem, 4);
            sheetIndicator.setDefaultColumnWidth(50);
            for (int i = 2; i < 4; i++) {
                sheetIndicator.autoSizeColumn(i);
            }
        }

        sheetAllIndicators.setDefaultColumnWidth(50);
        for (int i = 2; i < 4; i++) {
            sheetAllIndicators.autoSizeColumn(i);
        }
        
        //Запись в файл
        Path downloadsPath = Paths.get(System.getProperty("user.home"), "Downloads");
        Path parentPath = downloadsPath.getParent();
        String downloadsFolderPath = parentPath.toString() + "/Downloads/";
        String pathToSave = downloadsFolderPath.replace("\\", "/");

        try (OutputStream fileOut = new FileOutputStream(pathToSave +
                String.format("Анализ_%s_%s_%s.xlsx", reportData.get(0).getOpopName(), dateStartString, dateEndString))) {
            book.write(fileOut);
            book.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error writing " + e);
        }
    }


    private void makeTitleForTableAnalysis(Sheet sheet, ExcelCellStyle styles,
                                           String opopName, String dateStart, String dateEnd){
        // Заголовок
        Row rowTitle = sheet.createRow(0);
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue("Сравнение значений показателей по годам");
        cellTitle.setCellStyle(styles.StyleBoldFont);

        // ОПОП
        Row rowOpop = sheet.createRow(1);
        String[] opopData = {"ОПОП", opopName};
        for (int i = 0; i < 2; i++) {
            Cell opopCell = rowOpop.createCell(i);
            opopCell.setCellValue(opopData[i]);
            opopCell.setCellStyle(styles.StyleFontSimple);
        }

        // Период
        Row rowPeriod = sheet.createRow(2);
        String[] periodData = {"Период", String.format("с %s по %s", dateStart, dateEnd)};
        for (int i = 0; i < 2; i++) {
            Cell periodCell = rowPeriod.createCell(i);
            periodCell.setCellValue(periodData[i]);
            periodCell.setCellStyle(styles.StyleFontSimple);
        }
    }

    private int addDataToTableAnalysis(Sheet sheet, ExcelCellStyle styles,
                                        ReportAnalysisOpopDto reportItem, int rowIndex){
        // Наименование показателя
        Row rowIndicatorName = sheet.createRow(rowIndex);
        String[] indicatorNameData = {"Наименование показателя", reportItem.getIndicatorName()};
        for (int i = 0; i < 2; i++) {
            Cell indicatorNameCell = rowIndicatorName.createCell(i);
            indicatorNameCell.setCellValue(indicatorNameData[i]);
            if (i == 0) indicatorNameCell.setCellStyle(styles.StyleBorderBoldFontColorAllCenter);
            else indicatorNameCell.setCellStyle(styles.StyleBorderSimpleFontVerticalCenterWrap);
        }

        // Обозначение показателя
        rowIndex++; // 5
        Row rowIndicatorKey = sheet.createRow(rowIndex);
        String[] indicatorKeyData = {"Обозначение показателя", reportItem.getIndicatorKey()};
        for (int i = 0; i < 2; i++) {
            Cell indicatorKeyCell = rowIndicatorKey.createCell(i);
            indicatorKeyCell.setCellValue(indicatorKeyData[i]);
            indicatorKeyCell.setCellStyle(styles.StyleBorderBoldFontColorAllCenter);
        }

        // Заголовки таблицы
        rowIndex += 2; // 7
        String[] tableTitles = {"Дата мониторинга", "Значение", "Баллы", "Планирование"};
        Row rowTitlesTable = sheet.createRow(rowIndex);
        for (int i = 0; i < 4; i++) {
            Cell titleTableCell = rowTitlesTable.createCell(i);
            titleTableCell.setCellValue(tableTitles[i]);
            titleTableCell.setCellStyle(styles.StyleBorderBoldFontColorAllCenter);
        }

        // Данные показателя
        rowIndex++; // 8
        for (int i = 0; i < reportItem.getDates().size(); i++) {
            Row rowIndicator = sheet.createRow(rowIndex);

            Cell cellDate = rowIndicator.createCell(0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
            String dateString = simpleDateFormat.format(reportItem.getDates().get(i));
            cellDate.setCellValue(dateString);
            cellDate.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            Cell cellValue = rowIndicator.createCell(1);
            cellValue.setCellValue(reportItem.getValues().get(i));
            cellValue.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            Cell cellScore = rowIndicator.createCell(2);
            cellScore.setCellValue(reportItem.getScores().get(i));
            cellScore.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            Cell cellPlanned = rowIndicator.createCell(3);
            cellPlanned.setCellValue(reportItem.getPlanned().get(i) ? "Да" : "Нет");
            cellPlanned.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            rowIndex++;
        }
        return rowIndex;
    }

    private void makeTotalTableAnalysis(Workbook book, ExcelCellStyle styles,
                                        Long opopId, Date dateStart, Date dateEnd, String opopName){
        List<Calculation> reportData = calculationService.findCalculationsByPeriod(opopId, dateStart, dateEnd);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String dateStartString = sdf.format(dateStart);
        String dateEndString = sdf.format(dateEnd);

        Sheet sheet = book.createSheet("Сводная таблица");
        makeTitleForTableAnalysis(sheet, styles, opopName, dateStartString, dateEndString);

        // Заголовки таблицы
        String[] tableTitles = { "Наименование показателя", "Обозначение показателя", "Дата мониторинга", "Значение",
                "Баллы", "Планирование"};
        Row rowTitlesTable = sheet.createRow(5);
        for (int i = 0; i < 6; i++) {
            Cell titleTableCell = rowTitlesTable.createCell(i);
            titleTableCell.setCellValue(tableTitles[i]);
            titleTableCell.setCellStyle(styles.StyleBorderBoldFontColorAllCenter);
        }

        int rowIndex = 6;
        for (Calculation calc : reportData) {
            Row rowIndicator = sheet.createRow(rowIndex);

            Cell cellIndicatorName = rowIndicator.createCell(0);
            cellIndicatorName.setCellValue(calc.getIndicator().getName());
            cellIndicatorName.setCellStyle(styles.StyleBorderSimpleFontVerticalCenterWrap);

            Cell cellIndicatorKey = rowIndicator.createCell(1);
            cellIndicatorKey.setCellValue(calc.getIndicator().getKey());
            cellIndicatorKey.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            Cell cellDate = rowIndicator.createCell(2);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
            String dateString = simpleDateFormat.format(calc.getId().getDate());
            cellDate.setCellValue(dateString);
            cellDate.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            Cell cellValue = rowIndicator.createCell(3);
            cellValue.setCellValue(calc.getValue());
            cellValue.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            Cell cellScore = rowIndicator.createCell(4);
            cellScore.setCellValue(calc.getScore());
            cellScore.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            Cell cellPlanned = rowIndicator.createCell(5);
            cellPlanned.setCellValue(calc.isPlanned() ? "Да" : "Нет");
            cellPlanned.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            rowIndex++;
        }

        sheet.setDefaultColumnWidth(50);
        for (int i = 1; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}