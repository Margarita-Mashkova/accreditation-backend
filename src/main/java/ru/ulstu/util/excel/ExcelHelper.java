package ru.ulstu.util.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import ru.ulstu.dto.ValueDto;
import ru.ulstu.dto.ValueIdDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<ValueDto> excelToValues(InputStream inputStream, Long opopId, Date date) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<ValueDto> values = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                ValueDto valueDto = new ValueDto();
                ValueIdDto valueIdDto = new ValueIdDto();
                valueIdDto.setOpopId(opopId);
                valueIdDto.setDate(date);

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIndex) {
                        case 1 -> valueIdDto.setVariableKey(currentCell.getStringCellValue());
                        case 2 -> valueDto.setValue((float) currentCell.getNumericCellValue());
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                valueDto.setId(valueIdDto);
                values.add(valueDto);
            }
            workbook.close();
            return values;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse Excel file: " + e.getMessage());
        }
    }
}
