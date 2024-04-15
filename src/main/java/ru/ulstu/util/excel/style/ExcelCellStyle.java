package ru.ulstu.util.excel.style;

import org.apache.poi.ss.usermodel.*;

public class ExcelCellStyle {
    public Workbook book;

    /**
     * Font title bold and 18pt
     */
    public Font FontTitle;

    /**
     * Font title of table bold and 12pt
     */
    public Font FontTitleTable;

    /**
     * Font simple 12pt
     */
    public Font FontSimple;

    /**
     * Cell title bold font
     */
    public CellStyle StyleBoldFont;

    /**
     * Cell borders, bold font, color background, horizontal and vertical align center
     */
    public CellStyle StyleBorderBoldFontColorAllCenter;

    /**
     * Cell borders, simple font, vertical align center, wrap text
     */
    public CellStyle StyleBorderSimpleFontVerticalCenterWrap;

    /**
     * Cell borders, simple font, horizontal and vertical align center
     */
    public CellStyle StyleBorderSimpleFontAllCenter;

    /**
     * Cell simple font
     */
    public CellStyle StyleFontSimple;

    public ExcelCellStyle(Workbook book) {
        this.book = book;
        this.FontTitle = fontTitle();
        this.FontTitleTable = fontTitleTable();
        this.FontSimple = fontSimple();
        this.StyleBoldFont = styleBoldFont();
        this.StyleBorderBoldFontColorAllCenter = styleBorderBoldFontColorAllCenter();
        this.StyleBorderSimpleFontVerticalCenterWrap = styleBorderSimpleFontVerticalCenterWrap();
        this.StyleBorderSimpleFontAllCenter = styleBorderSimpleFontAllCenter();
        this.StyleFontSimple = styleFontSimple();
    }

    private Font fontTitle() {
        Font fontTitle = book.createFont();
        fontTitle.setBold(true);
        fontTitle.setFontName("Times New Roman");
        fontTitle.setFontHeightInPoints((short) 16);
        return fontTitle;
    }
    private Font fontTitleTable() {
        Font fontTitleTable = book.createFont();
        fontTitleTable.setBold(true);
        fontTitleTable.setFontName("Times New Roman");
        fontTitleTable.setFontHeightInPoints((short) 12);
        return fontTitleTable;
    }
    private Font fontSimple() {
        Font fontSimple = book.createFont();
        fontSimple.setFontName("Times New Roman");
        fontSimple.setFontHeightInPoints((short) 12);
        return fontSimple;
    }

    private CellStyle styleBoldFont() {
        CellStyle styleBoldFont = book.createCellStyle();
        styleBoldFont.setFont(fontTitle());
        return styleBoldFont;
    }
    private CellStyle styleBorderBoldFontColorAllCenter() {
        CellStyle styleBorderBoldFontColorAllCenter = book.createCellStyle();

        styleBorderBoldFontColorAllCenter.setBorderBottom(BorderStyle.THIN);
        styleBorderBoldFontColorAllCenter.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderBoldFontColorAllCenter.setBorderLeft(BorderStyle.THIN);
        styleBorderBoldFontColorAllCenter.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderBoldFontColorAllCenter.setBorderRight(BorderStyle.THIN);
        styleBorderBoldFontColorAllCenter.setRightBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderBoldFontColorAllCenter.setBorderTop(BorderStyle.THIN);
        styleBorderBoldFontColorAllCenter.setTopBorderColor(IndexedColors.BLACK.getIndex());

        styleBorderBoldFontColorAllCenter.setFont(fontTitleTable());

        styleBorderBoldFontColorAllCenter.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        styleBorderBoldFontColorAllCenter.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        styleBorderBoldFontColorAllCenter.setAlignment(HorizontalAlignment.CENTER);
        styleBorderBoldFontColorAllCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        return styleBorderBoldFontColorAllCenter;
    }
    private CellStyle styleBorderSimpleFontVerticalCenterWrap() {
        CellStyle styleBorderSimpleFontVerticalCenterWrap = book.createCellStyle();

        styleBorderSimpleFontVerticalCenterWrap.setBorderBottom(BorderStyle.THIN);
        styleBorderSimpleFontVerticalCenterWrap.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderSimpleFontVerticalCenterWrap.setBorderLeft(BorderStyle.THIN);
        styleBorderSimpleFontVerticalCenterWrap.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderSimpleFontVerticalCenterWrap.setBorderRight(BorderStyle.THIN);
        styleBorderSimpleFontVerticalCenterWrap.setRightBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderSimpleFontVerticalCenterWrap.setBorderTop(BorderStyle.THIN);
        styleBorderSimpleFontVerticalCenterWrap.setTopBorderColor(IndexedColors.BLACK.getIndex());

        styleBorderSimpleFontVerticalCenterWrap.setFont(fontSimple());

        styleBorderSimpleFontVerticalCenterWrap.setVerticalAlignment(VerticalAlignment.CENTER);
        styleBorderSimpleFontVerticalCenterWrap.setWrapText(true);
        return styleBorderSimpleFontVerticalCenterWrap;
    }
    private CellStyle styleBorderSimpleFontAllCenter() {
        CellStyle styleBorderSimpleFontAllCenter = book.createCellStyle();

        styleBorderSimpleFontAllCenter.setBorderBottom(BorderStyle.THIN);
        styleBorderSimpleFontAllCenter.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderSimpleFontAllCenter.setBorderLeft(BorderStyle.THIN);
        styleBorderSimpleFontAllCenter.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderSimpleFontAllCenter.setBorderRight(BorderStyle.THIN);
        styleBorderSimpleFontAllCenter.setRightBorderColor(IndexedColors.BLACK.getIndex());
        styleBorderSimpleFontAllCenter.setBorderTop(BorderStyle.THIN);
        styleBorderSimpleFontAllCenter.setTopBorderColor(IndexedColors.BLACK.getIndex());

        styleBorderSimpleFontAllCenter.setFont(fontSimple());

        styleBorderSimpleFontAllCenter.setAlignment(HorizontalAlignment.CENTER);
        styleBorderSimpleFontAllCenter.setVerticalAlignment(VerticalAlignment.CENTER);

        return styleBorderSimpleFontAllCenter;
    }
    private CellStyle styleFontSimple() {
        CellStyle styleFontSimple = book.createCellStyle();
        styleFontSimple.setFont(fontSimple());
        return styleFontSimple;
    }
}
