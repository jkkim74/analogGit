package com.skplanet.bisportal.view.syrup;

/**
 * Created by lko on 2014-12-16.
 */

import com.google.common.collect.Maps;
import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.common.utils.NumberUtil;
import net.sf.reportengine.out.AbstractByteOutput;
import net.sf.reportengine.out.CellProps;
import net.sf.reportengine.out.ReportProps;
import net.sf.reportengine.out.RowProps;
import net.sf.reportengine.out.TitleProps;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;


/**
 * A simple implementation of the AbstractReportOut having as result an Excel file
 * based on jakarta's poi library for excel.
 *
 * <pre>
 * Usage:
 * 		ReportOutput reportOutput = new ExcelOutput("c:/temp/test.xls");
 * </pre>
 * @author dragos balan (dragos dot balan at gmail dot com)
 * @since 0.2
 */
@Deprecated
@SuppressWarnings({"unchecked", "rawtypes", "cast"})
public class NewCustomExcelOutput extends AbstractByteOutput {
    /**
     * the xls workbook
     */
    private HSSFWorkbook workBook;

    /**
     * the xls sheet
     */
    private HSSFSheet sheet;

    /**
     * the name of the sheet that holds the report
     */
    private String sheetName = "Report";

    /**
     * current row
     */
    private HSSFRow currentRow;

    /**
     * common odd row cell style
     */
    //private HSSFCellStyle DEFAULT_ODD_ROW_CELL_STYLE;

    /**
     * common even row cell style
     */
    //private HSSFCellStyle DEFAULT_EVEN_ROW_CELL_STYLE;

    private HSSFCellStyle DEFAULT_ROW_CELL_STYLE;

    /**
     * the heder style
     */
//    private HSSFCellStyle DEFAULT_HEADER_CELL_STYLE ;

    /**
     * column
     */
    private short currentCol = 0;

    /**
     *
     */
    private int titleRowSpan = 0;

    /**
     *
     */
    private int headerRowSpan = 0;

    //private int dataRowSpan = 0;

    /**
     *
     */
    //private HSSFFont columnHeaderFont;

    private CellStyle styleHVRight;

    //private CellStyle styleHVCenter;

    private Object preValue;

    private int startHeaderSpan = 0;

    //private int startDataRowSpan = 0;

    private int headerColCnt = 0;

    private Map map = Maps.newHashMap();

    /**
     * outputs in memory (if no other outputStream is set)
     */
    public NewCustomExcelOutput(){
        super();
    }

    /**
     * output into a file
     * @param fileName
     */
    public NewCustomExcelOutput(String fileName){
        super(fileName);
    }


    /**
     * output into an outputStream
     * @param out     the output stream
     */
    public NewCustomExcelOutput(OutputStream out){
        super(out);
    }

    /**
     * starts the report
     */
    public void open() {
        markAsOpen();

        preValue = 0;

        workBook = new HSSFWorkbook();
        sheet = workBook.createSheet(sheetName);

        //HSSFPalette customPalette = workBook.getCustomPalette();

        //settings for odd row style
        //DEFAULT_ODD_ROW_CELL_STYLE = workBook.createCellStyle();

        //settings for even row style
        //DEFAULT_EVEN_ROW_CELL_STYLE = workBook.createCellStyle();

        DEFAULT_ROW_CELL_STYLE = workBook.createCellStyle();

        //settings for header style
        //columnHeaderFont = workBook.createFont();

        try {
            styleHVRight = ExcelUtil.getRightStyle(workBook, sheet);
            //styleHVCenter = ExcelUtil.getCenterStyle(sheet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * empty implementation
     */
    public void startReport(ReportProps reportProps){

    }


    /*
     * (non-Javadoc)
     * @see net.sf.reportengine.out.ReportOutput#outputTitle(net.sf.reportengine.out.TitleProps)
     */
    public void outputTitle(TitleProps titleProps){
        currentRow = sheet.createRow(0);

        Font fontTitle = sheet.getWorkbook().createFont();
        fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
        fontTitle.setFontHeight((short) 280);

        HSSFCellStyle cellStyle = workBook.createCellStyle();
        cellStyle.setFont(fontTitle);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);

        CellStyle cellStyleDate = sheet.getWorkbook().createCellStyle();
        cellStyleDate.setAlignment(CellStyle.ALIGN_RIGHT);
        cellStyleDate.setWrapText(true);

        Cell cell = currentRow.createCell(0);
        currentRow.setHeight((short) 500);
        cell.setCellValue(titleProps.getTitle() + " 리포트");
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(cellStyle);

        Row dateTitle = sheet.createRow((short) 1);
        Cell cellDate = dateTitle.createCell(0);
        cellDate.setCellValue("This report was generated at " + new Date());
        cellDate.setCellStyle(cellStyleDate);

        sheet.addMergedRegion(
                new Region(0,
                        (short)0,
                        0,
                        (short)(titleProps.getColspan()-1)));
        sheet.addMergedRegion(
                new Region(1,
                        (short)0,
                        1,
                        (short)(titleProps.getColspan()-1)));
        //titleRowSpan++;
        titleRowSpan = 2;
    }

    public void startHeaderRow(RowProps rowProperties){
        currentRow = sheet.createRow((short)(rowProperties.getRowNumber() + titleRowSpan));
        currentCol = 0;

    }

    public void outputHeaderCell(CellProps cellProps){
        int rowCount = cellProps.getRowNumber() + titleRowSpan;

        if (currentCol == 0 || cellProps.getColspan() > 1) {
            preValue = cellProps.getValue();
        }

        HSSFCell cell = currentRow.createCell(currentCol);
        currentRow.setHeight((short) 500);
        HSSFCellStyle cellStyle = workBook.createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        //pattern 속성이 없으면 Color를 설정해도 아무것도 보이지 않음.
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillBackgroundColor(HSSFColor.LIGHT_GREEN.index);
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);

        cellStyle.setFillPattern(CellStyle.FINE_DOTS);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        if (!preValue.equals(cellProps.getValue())) {
            preValue = cellProps.getValue();
                sheet.addMergedRegion(
                        new Region(rowCount,
                                (short) startHeaderSpan,
                                rowCount,
                                (short) (currentCol - 1)
                        ));
            startHeaderSpan = currentCol;
        }
        cell.setCellStyle(cellStyle);

        String valueToWrite = cellProps.getValue() != null ? cellProps.getValue().toString() : WHITESPACE;
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(new HSSFRichTextString(valueToWrite));

        int colspan = cellProps.getColspan();
        if (colspan != 1) {
            sheet.addMergedRegion(
                    new Region(rowCount,
                            (short)currentCol,
                            rowCount,
                            (short) (currentCol + colspan - 1)));

        }
        currentCol += colspan;
        headerColCnt++;
    }

    public void endHeaderRow(){
        headerRowSpan++;
    }

    /**
     * ends the current line and creates a new one
     */
    public void startDataRow(RowProps rowProperties) {
        short currentRowNumber = (short)(rowProperties.getRowNumber() + titleRowSpan + headerRowSpan);
        currentRow = sheet.createRow(currentRowNumber);
        currentCol = 0;
    }


    /**
     * output the specified value
     */
    public void outputDataCell(CellProps cellProps) {
        int rowCount = cellProps.getRowNumber() + titleRowSpan + headerRowSpan ;

        HSSFCell cell = currentRow.createCell(currentCol);

        if (currentRow.getRowNum() == 3) {
            Map subMap = Maps.newHashMap();
            subMap.put("colVal", cellProps.getValue());
            subMap.put("rowNum", currentRow.getRowNum());
            map.put(currentCol, subMap);
        }

        HSSFCellStyle cellStyle;

        cellStyle = DEFAULT_ROW_CELL_STYLE;

        cellStyle.setAlignment(cellProps.getHorizAlign().getHssfCode());
        cellStyle.setVerticalAlignment(cellProps.getVertAlign().getHssfCode());
        cellStyle.setWrapText(true);
        cell.setCellStyle(cellStyle);

        String valueToWrite = cellProps.getValue() != null ? cellProps.getValue().toString() : WHITESPACE;
        String childrenValue = StringUtils.replace(valueToWrite, ",", StringUtils.EMPTY);
        if (NumberUtil.isNumber(childrenValue)) {
            DataFormat format = workBook.createDataFormat();
            cellStyle.setDataFormat(format.getFormat("#,##"));
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellStyle(styleHVRight);
            if (NumberUtil.isDigit(childrenValue)) {
                cell.setCellValue(Long.parseLong(childrenValue));
            } else {
                cell.setCellValue(Double.parseDouble(childrenValue));
            }
        }else{
            cell.setCellValue(new HSSFRichTextString(valueToWrite));
        }

        Map oMap = (Map)map.get(currentCol);
        if (oMap.get("colVal").equals(cellProps.getValue())) {
            if (cellProps.getRowNumber() > 0) {
                cell.setCellValue(new HSSFRichTextString(StringUtils.EMPTY));
            }
        }
        if (!oMap.get("colVal").equals(cellProps.getValue())) {
            Map subMap = Maps.newHashMap();
            subMap.put("colVal", cellProps.getValue());
            subMap.put("rowNum", currentRow.getRowNum());
            map.put(currentCol, subMap);
            //int rowCnt = (int)oMap.get("rowNum");
        }

        int colspan = cellProps.getColspan();

        if (colspan != 1) {
            sheet.addMergedRegion(
                    new Region(rowCount,
                            (short)currentCol,
                            rowCount,
                            (short) (currentCol + colspan - 1)));

        }
        currentCol += colspan;
    }

    /**
     * 칼럼 길이 지정.
     *
     */
    public void setColumnWith(Sheet worksheet, int headersLength) throws Exception {
        // Set column widths
        for (int i = 0; i < headersLength; i++) {
            worksheet.autoSizeColumn(i);
            worksheet.setColumnWidth(i, 5000);
        }
    }

    public void endDataRow(){
    }

    /**
     * empty implementation
     */
    public void endReport(){
        try {
            setColumnWith(sheet, currentCol);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ends the report
     */
    public void close() {
        try {
            workBook.write(getOutputStream());
            super.close();//flushes the output stream and closes the output
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    public String getSheetName(){
        return sheetName;
    }

    public void setSheetName(String name){
        this.sheetName = name;
    }
}
