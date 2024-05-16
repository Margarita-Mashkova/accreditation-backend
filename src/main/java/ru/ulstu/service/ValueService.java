package ru.ulstu.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.ulstu.dto.ReportFileDto;
import ru.ulstu.dto.ValueDto;
import ru.ulstu.model.OPOP;
import ru.ulstu.model.Value;
import ru.ulstu.model.ValueId;
import ru.ulstu.model.Variable;
import ru.ulstu.repository.ValueRepository;
import ru.ulstu.service.exception.ValueNotFoundException;
import ru.ulstu.util.excel.ExcelHelper;
import ru.ulstu.util.excel.style.ExcelCellStyle;
import ru.ulstu.util.validation.ValidatorUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ValueService {
    @Autowired
    private ValueRepository valueRepository;
    @Autowired
    private ValidatorUtil validatorUtil;
    @Autowired
    private OPOPService opopService;
    @Autowired
    private VariableService variableService;

    @Transactional(readOnly = true)
    public List<Value> findAllValues() {
        return valueRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Value findValue(ValueId valueId) {
        final Optional<Value> value = valueRepository.findById(valueId);
        return value.orElseThrow(() -> new ValueNotFoundException(valueId));
    }

    @Transactional(readOnly = true)
    public List<Value> findValuesByOpopAndDate(Long opopId, Date date) {
        return valueRepository.findAllByOpopIdAndIdDateOrderByIdVariableKeyAsc(opopId, date);
    }

    @Transactional(readOnly = true)
    public List<Date> findDatesByOpop(Long opopId) {
        List<Value> values = valueRepository.findAllByOpopId(opopId);
        List<Date> datesWithValues = new ArrayList<>();
        for (var value : values) {
            if (!datesWithValues.contains(value.getId().getDate())) {
                datesWithValues.add(value.getId().getDate());
            }
        }
        datesWithValues.sort(Collections.reverseOrder());
        return datesWithValues;
    }

    @Transactional
    public List<Value> addValuesList(List<Value> values) {
        return values.stream()
                .map(value -> addValue(value.getId(), value.getValue()))
                .toList();
    }

    @Transactional
    public Value addValue(ValueId valueId, float val) {
        OPOP opop = opopService.findOpopById(valueId.getOpopId());
        Variable variable = variableService.findVariableByKey(valueId.getVariableKey());
        Value value = new Value(valueId, opop, variable, val);
        validatorUtil.validate(value);
        return valueRepository.save(value);
    }

    @Transactional
    public Value updateValue(ValueId valueId, float val) {
        Value valueDB = findValue(valueId);
        OPOP opop = opopService.findOpopById(valueId.getOpopId());
        Variable variable = variableService.findVariableByKey(valueId.getVariableKey());
        valueDB.setValue(val);
        valueDB.setOpop(opop);
        valueDB.setVariable(variable);
        validatorUtil.validate(valueDB);
        return valueRepository.save(valueDB);
    }

    @Transactional
    public Value deleteValue(ValueId valueId) {
        final Value value = findValue(valueId);
        valueRepository.delete(value);
        return value;
    }

    @Transactional
    public void deleteAllValues() {
        valueRepository.deleteAll();
    }

    @Transactional
    public ReportFileDto generatePatternFile(Long opopId, Date date) {
        List<Variable> variables = variableService.findAllVariables();
        OPOP opop = opopService.findOpopById(opopId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = sdf.format(date);

        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Данные для расчета");
        ExcelCellStyle styles = new ExcelCellStyle(book);

        String[] tableTitles = {"Наименование переменной", "Обозначение переменной", "Значение"};
        Row rowTitlesTable = sheet.createRow(0);
        for (int i = 0; i < 3; i++) {
            Cell titleTableCell = rowTitlesTable.createCell(i);
            titleTableCell.setCellValue(tableTitles[i]);
            titleTableCell.setCellStyle(styles.StyleBorderBoldFontColorAllCenter);
        }

        int rowIndex = 1;
        for (Variable variable : variables) {
            Row rowIndicator = sheet.createRow(rowIndex);

            Cell cellVariableName = rowIndicator.createCell(0);
            cellVariableName.setCellValue(variable.getName());
            cellVariableName.setCellStyle(styles.StyleBorderSimpleFontVerticalCenterWrap);

            Cell cellVariableKey = rowIndicator.createCell(1);
            cellVariableKey.setCellValue(variable.getKey());
            cellVariableKey.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            Cell cellValue = rowIndicator.createCell(2);
            cellValue.setCellType(CellType.NUMERIC);
            cellValue.setCellStyle(styles.StyleBorderSimpleFontAllCenter);

            rowIndex++;
        }

        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        //Запись в файл
        final String filename = String.format("Данные для расчета_%s_%s.xlsx", opop.getName(), dateString);
        final String filePath = "reports/" + filename;

        try (OutputStream fileOut = new FileOutputStream(filePath)) {
            book.write(fileOut);
            book.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Получение данных файла
        byte[] data;
        try (InputStream inputStream = new FileInputStream(filePath)) {
            data = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Удаление файла
        File file = new File(filePath);
        file.delete();

        // Передача данных в контроллер
        return ReportFileDto.builder()
                .filename(filename)
                .data(data)
                .build();
    }

    @Transactional
    public List<ValueDto> readValuesFromFile(MultipartFile file, Long opopId, Date date) {
        List<ValueDto> readValues;
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                readValues = ExcelHelper.excelToValues(file.getInputStream(), opopId, date);
            } catch (IOException e) {
                throw new RuntimeException("Fail to read excel data: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("File hasn't .xlsx format");
        }
        return readValues;
    }
}
