package com.skplanet.bisportal.view.ocb;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.ObsDPushRespSta;
import com.skplanet.bisportal.view.AbstractExcelView;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.skplanet.bisportal.common.utils.Objects.uncheckedCast;

/**
 * Created by lko on 2014-10-16.
 *
 * The MktPushSndRsltExcelView class.
 *
 * @author kyoungoh lee
 */
public class MktPushSndRsltExcelView extends AbstractExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String menuName = "마케팅 Push별 발송 결과";
        Sheet worksheet = workbook.createSheet(menuName);
        String headers[][] = { { "발송일", "발송타입", "발송내용", "OCB App.버전", "발송시도건수", "App.도달건수", "클릭건수", "App.도달율", "고객반응율" } };
        int headLength = headers[0].length;

        // 칼럼 넓이 조정.
        super.setColumnWith(worksheet, headLength);
        // 타이틀 입력.
        super.buildTitle(worksheet, menuName, headLength);
        // 헤드 입력.
        super.buildHeaders(worksheet, headers, 0, 0);

        // confirm Row offset
        int startRowIndex = 3;

        // 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
        CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);

        // 엑셀 바디의 셀 스타일 정의(문자 좌측 정렬).
        CellStyle styleHVLeft = ExcelUtil.getLeftStyle(worksheet);

        // 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
        CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);
        CellStyle styleHVRightPre = ExcelUtil.getRightFormatStyle(workbook, worksheet);

        // 엑셀 바디 생성.
        List<ObsDPushRespSta> obsDPushRespSta = uncheckedCast(model.get("mktPushSndRsltData"));

        String[] merge = new String[5];
        int startRow = 3, lastRow = 3, startTypRow = 3, lastTypRow = 3;
        int startSndMRow = 3, lastSndMRow = 3;
        int startRcvRRow = 3, lastRcvRRow = 3;
        int startLchRRow = 3, lastLchRRow = 3;
        int ocbMbrentStaSize = obsDPushRespSta.size();
        for (ObsDPushRespSta obsDPushRespStas : obsDPushRespSta) {
            // Create a new row
            Row row = worksheet.createRow(startRowIndex++);
            // Retrieve the 발송일 value
            Cell cell1 = row.createCell(0);
            cell1.setCellValue(obsDPushRespStas.getStdDt());
            cell1.setCellStyle(styleHVCenter);

            // Retrieve the 발송타입 value
            Cell cell2 = row.createCell(1);
            cell2.setCellValue(obsDPushRespStas.getPushTyp());
            cell2.setCellStyle(styleHVCenter);

            // Retrieve the 발송내용 value
            Cell cell3 = row.createCell(2);
            cell3.setCellValue(obsDPushRespStas.getSndMsg());
            cell3.setCellStyle(styleHVLeft);

            // Retrieve the 앱 버전 value
            Cell cell4 = row.createCell(3);
            cell4.setCellValue(obsDPushRespStas.getAppVer());
            cell4.setCellStyle(styleHVCenter);

            // Retrieve the 발송시도건수 value
            Cell cell5 = row.createCell(4);
            cell5.setCellValue(obsDPushRespStas.getSndCustCnt());
            cell5.setCellStyle(styleHVRight);

            // Retrieve the 도달건수 value
            Cell cell6 = row.createCell(5);
            cell6.setCellValue(obsDPushRespStas.getRcvCustCnt());
            cell6.setCellStyle(styleHVRight);

            // Retrieve the 클릭건수 value
            Cell cell7 = row.createCell(6);
            cell7.setCellValue(obsDPushRespStas.getLchCustCnt());
            cell7.setCellStyle(styleHVRight);

            // Retrieve the App.도달율 value
            Cell cell8 = row.createCell(7);
            cell8.setCellValue(obsDPushRespStas.getRcvRate());
            cell8.setCellStyle(styleHVRightPre);

            // Retrieve the 고객반응율 value
            Cell cell9 = row.createCell(8);
            cell9.setCellValue(obsDPushRespStas.getLchRate());
            cell9.setCellStyle(styleHVRightPre);

            // 셀 병합처리
            if (StringUtils.equals(merge[0], obsDPushRespStas.getStdDt())) {
                lastRow++;
                if ((startRowIndex == ocbMbrentStaSize + 3) && (startRow < lastRow)) {
                    worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
                }
            } else {
                if (startRow < lastRow) {
                    worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
                }
                startRow = startRowIndex - 1;
                lastRow = startRowIndex - 1;
            }
            // 셀 병합처리
            if (StringUtils.equals(merge[1], obsDPushRespStas.getPushTyp())) {
                lastTypRow++;
                if ((startRowIndex == ocbMbrentStaSize + 3) && (startTypRow < lastTypRow)) {
                    worksheet.addMergedRegion(new CellRangeAddress(startTypRow, lastTypRow, 1, 1));
                }
            } else {
                if (startTypRow < lastTypRow) {
                    worksheet.addMergedRegion(new CellRangeAddress(startTypRow, lastTypRow, 1, 1));
                }
                startTypRow = startRowIndex - 1;
                lastTypRow = startRowIndex - 1;
            }
            // 셀 병합처리
            if (StringUtils.equals(merge[2], obsDPushRespStas.getSndMsg())) {
                lastSndMRow++;
                if ((startRowIndex == ocbMbrentStaSize + 3) && (startSndMRow < lastSndMRow)) {
                    worksheet.addMergedRegion(new CellRangeAddress(startSndMRow, lastSndMRow, 2, 2));
                }
            } else {
                if (startSndMRow < lastSndMRow) {
                    worksheet.addMergedRegion(new CellRangeAddress(startSndMRow, lastSndMRow, 2, 2));
                }
                startSndMRow = startRowIndex - 1;
                lastSndMRow = startRowIndex - 1;
            }
            // 셀 병합처리
            if (StringUtils.equals(merge[3], obsDPushRespStas.getSndMsg() + Float.toString(obsDPushRespStas.getRcvRate()))) {
                lastRcvRRow++;
                if ((startRowIndex == ocbMbrentStaSize + 3) && (startRcvRRow < lastRcvRRow)) {
                    worksheet.addMergedRegion(new CellRangeAddress(startRcvRRow, lastRcvRRow, 7, 7));
                }
            } else {
                if (startRcvRRow < lastRcvRRow) {
                    worksheet.addMergedRegion(new CellRangeAddress(startRcvRRow, lastRcvRRow, 7, 7));
                }
                startRcvRRow = startRowIndex - 1;
                lastRcvRRow = startRowIndex - 1;
            }
            // 셀 병합처리
            if (StringUtils.equals(merge[4], obsDPushRespStas.getSndMsg() + Float.toString(obsDPushRespStas.getLchRate()))) {
                lastLchRRow++;
                if ((startRowIndex == ocbMbrentStaSize + 3) && (startLchRRow < lastLchRRow)) {
                    worksheet.addMergedRegion(new CellRangeAddress(startLchRRow, lastLchRRow, 8, 8));
                }
            } else {
                if (startLchRRow < lastLchRRow) {
                    worksheet.addMergedRegion(new CellRangeAddress(startLchRRow, lastLchRRow, 8, 8));
                }
                startLchRRow = startRowIndex - 1;
                lastLchRRow = startRowIndex - 1;
            }
            merge[0] = obsDPushRespStas.getStdDt();
            merge[1] = obsDPushRespStas.getPushTyp();
            merge[2] = obsDPushRespStas.getSndMsg();
            merge[3] = obsDPushRespStas.getSndMsg() + Float.toString(obsDPushRespStas.getRcvRate());
            merge[4] = obsDPushRespStas.getSndMsg() + Float.toString(obsDPushRespStas.getLchRate());
        }
    }
}
