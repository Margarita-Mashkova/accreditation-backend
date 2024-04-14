package ru.ulstu.util.excel.style;

import org.apache.poi.ss.usermodel.*;

public class ExcelCellStyle {
    public Workbook book;
    public Font FontTitle;
    public Font FontTitleTable;
    public Font FontSimple;
    public CellStyle StyleBold;
    public CellStyle StyleBorderAndBold;
    public CellStyle StyleBorder;
    public CellStyle StyleBorderCenter;
    public CellStyle StyleSimple;

    public ExcelCellStyle(Workbook book) {
        this.book = book;
        this.FontTitle = fontTitle();
        this.FontTitleTable = fontTitleTable();
        this.FontSimple = fontSimple();
        this.StyleBold = styleBold();
        this.StyleBorderAndBold = styleBorderAndBold();
        this.StyleBorder = styleBorder();
        this.StyleBorderCenter = styleBorderCenter();
        this.StyleSimple = styleSimple();
    }

    // Font title bold and 18pt
    public Font fontTitle() {
        Font fontTitle = book.createFont();
        fontTitle.setBold(true);
        fontTitle.setFontName("Times New Roman");
        fontTitle.setFontHeightInPoints((short) 16);
        return fontTitle;
    }

    // Font title of table bold and 12pt
    public Font fontTitleTable() {
        Font fontTitleTable = book.createFont();
        fontTitleTable.setBold(true);
        fontTitleTable.setFontName("Times New Roman");
        fontTitleTable.setFontHeightInPoints((short) 12);
        return fontTitleTable;
    }

    // Font simple 12pt
    public Font fontSimple() {
        Font fontSimple = book.createFont();
        fontSimple.setFontName("Times New Roman");
        fontSimple.setFontHeightInPoints((short) 12);
        return fontSimple;
    }

    // Cell bold font
    public CellStyle styleBold() {
        CellStyle styleBold = book.createCellStyle();
        styleBold.setFont(fontTitle());
        return styleBold;
    }


    // Cell bold font and borders and color background
    public CellStyle styleBorderAndBold() {
        CellStyle styleBorderAndBold = book.createCellStyle();
        styleBorderAndBold.setBorderBottom(BorderStyle.THIN);
        styleBorderAndBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderAndBold.setBorderLeft(BorderStyle.THIN);
        styleBorderAndBold.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderAndBold.setBorderRight(BorderStyle.THIN);
        styleBorderAndBold.setRightBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderAndBold.setBorderTop(BorderStyle.THIN);
        styleBorderAndBold.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderAndBold.setFont(fontTitleTable());
        styleBorderAndBold.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        styleBorderAndBold.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleBorderAndBold.setAlignment(HorizontalAlignment.CENTER);
        return styleBorderAndBold;
    }


    //Cell borders
    public CellStyle styleBorder() {
        CellStyle styleBorder = book.createCellStyle();
        styleBorder.setBorderBottom(BorderStyle.THIN);
        styleBorder.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styleBorder.setBorderLeft(BorderStyle.THIN);
        styleBorder.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        styleBorder.setBorderRight(BorderStyle.THIN);
        styleBorder.setRightBorderColor(IndexedColors.BLACK.getIndex());
        styleBorder.setBorderTop(BorderStyle.THIN);
        styleBorder.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styleBorder.setFont(fontSimple());
        styleBorder.setVerticalAlignment(VerticalAlignment.CENTER);
        styleBorder.setWrapText(true);
        return styleBorder;
    }

    //Cell borders and align center
    public CellStyle styleBorderCenter() {
        CellStyle styleBorderCenter = book.createCellStyle();
        styleBorderCenter.setBorderBottom(BorderStyle.THIN);
        styleBorderCenter.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderCenter.setBorderLeft(BorderStyle.THIN);
        styleBorderCenter.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderCenter.setBorderRight(BorderStyle.THIN);
        styleBorderCenter.setRightBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderCenter.setBorderTop(BorderStyle.THIN);
        styleBorderCenter.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderCenter.setFont(fontSimple());
        styleBorderCenter.setAlignment(HorizontalAlignment.CENTER);
        styleBorderCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        return styleBorderCenter;
    }

    //Cell simple
    public CellStyle styleSimple() {
        CellStyle styleSimple = book.createCellStyle();
        styleSimple.setFont(fontSimple());
        return styleSimple;
    }
}
