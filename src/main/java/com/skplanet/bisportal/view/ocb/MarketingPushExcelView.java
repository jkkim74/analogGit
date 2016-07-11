package com.skplanet.bisportal.view.ocb;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.ObsMktPushSend;
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
 * The MarketingPushExcelView class.
 *
 * Created by ophelisis on 2015. 8. 12..
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 */
public class MarketingPushExcelView extends AbstractExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String menuName = "OCB 마케팅 Push별 발송 결과";
        Sheet worksheet = workbook.createSheet(menuName);
        String headers[][] = { { "발송일","발송시작시","타이틀","발송내용","Push Type","Display Type","EventID","Big Picture 여부","발송건수","도달건수","Push 클릭건수","알림함 클릭건수","바로적립 클릭건수","클릭건수 합계","도달율","반응율" } };
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
        List<ObsMktPushSend> ocbMarketingPushSend = uncheckedCast(model.get("marketingPushData"));

        String[] merge = new String[2];
        int startRow = 3, lastRow = 3, startTypRow = 3, lastTypRow = 3;
        int ocbMbrentStaSize = ocbMarketingPushSend.size();
        for (ObsMktPushSend ocbMarketingPushSends : ocbMarketingPushSend) {
            // Create a new row
            Row row = worksheet.createRow(startRowIndex++);

            // Retrieve the 발송일 value
            Cell cell1 = row.createCell(0);
            cell1.setCellValue(ocbMarketingPushSends.getStdDt());
            cell1.setCellStyle(styleHVCenter);

            // Retrieve the 발송시작시간 value
            Cell cell2 = row.createCell(1);
            cell2.setCellValue(ocbMarketingPushSends.getSndTime());
            cell2.setCellStyle(styleHVCenter);

            // Retrieve the 타이틀 value
            Cell cell3 = row.createCell(2);
            cell3.setCellValue(ocbMarketingPushSends.getTitle());
            cell3.setCellStyle(styleHVLeft);

            // Retrieve the 발송내용 value
            Cell cell4 = row.createCell(3);
            cell4.setCellValue(ocbMarketingPushSends.getMsg());
            cell4.setCellStyle(styleHVCenter);

            // Retrieve the Push Type value
            Cell cell5 = row.createCell(4);
            cell5.setCellValue(ocbMarketingPushSends.getPushType());
            cell5.setCellStyle(styleHVRight);

            // Retrieve the Display Type value
            Cell cell6 = row.createCell(5);
            cell6.setCellValue(ocbMarketingPushSends.getDisplayType());
            cell6.setCellStyle(styleHVRight);

            // Retrieve the EventID value
            Cell cell7 = row.createCell(6);
            cell7.setCellValue(ocbMarketingPushSends.getEventId());
            cell7.setCellStyle(styleHVRight);

            // Retrieve the Big Picture 여부 value
            Cell cell8 = row.createCell(7);
            cell8.setCellValue(ocbMarketingPushSends.getBigPicture());
            cell8.setCellStyle(styleHVRightPre);

            // Retrieve the 발송건수 value
            Cell cell9 = row.createCell(8);
            cell9.setCellValue(ocbMarketingPushSends.getCustCnt());
            cell9.setCellStyle(styleHVRightPre);

            // Retrieve the 도달건수 value
            Cell cell10 = row.createCell(9);
            cell10.setCellValue(ocbMarketingPushSends.getPushRcvCustCnt());
            cell10.setCellStyle(styleHVRightPre);

            // Retrieve the Push 클릭건수 value
            Cell cell11 = row.createCell(10);
            cell11.setCellValue(ocbMarketingPushSends.getPushClkCustCnt());
            cell11.setCellStyle(styleHVRightPre);

            // Retrieve the 알림함 클릭건수 value
            Cell cell12 = row.createCell(11);
            cell12.setCellValue(ocbMarketingPushSends.getNotiClkCustCnt());
            cell12.setCellStyle(styleHVRightPre);

            // Retrieve the 바로적립 클릭건수 value
            Cell cell13 = row.createCell(12);
            cell13.setCellValue(ocbMarketingPushSends.getDirtClkCnt());
            cell13.setCellStyle(styleHVRightPre);

            // Retrieve the 클릭건수 합계 value
            Cell cell14 = row.createCell(13);
            cell14.setCellValue(ocbMarketingPushSends.getClkSum());
            cell14.setCellStyle(styleHVRightPre);

            // Retrieve the 도달율 value
            Cell cell15 = row.createCell(14);
            cell15.setCellValue(ocbMarketingPushSends.getRcvPer());
            cell15.setCellStyle(styleHVRightPre);

            // Retrieve the 반응율 value
            Cell cell16 = row.createCell(15);
            cell16.setCellValue(ocbMarketingPushSends.getRatPer());
            cell16.setCellStyle(styleHVRightPre);

            // 셀 병합처리
            if (StringUtils.equals(merge[0], ocbMarketingPushSends.getStdDt())) {
                lastRow++;
                if ((startRowIndex == ocbMbrentStaSize + 3) && (startRow < lastRow)) {
                    if ("_SUMMARY".equals(ocbMarketingPushSends.getSndTime())) {
                        cell1.setCellValue("일별 집계");
                        worksheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, 0, 7));
                        lastRow--;
                    }
                    worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
                } else {
                    if ("_SUMMARY".equals(ocbMarketingPushSends.getSndTime())) {
                        cell1.setCellValue("일별 집계");
                        worksheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, 0, 7));
                        lastRow--;
                    }
                }
            } else {
                if (startRow < lastRow) {
                    worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
                }
                startRow = startRowIndex - 1;
                lastRow = startRowIndex - 1;
            }
            // 셀 병합처리
            if (StringUtils.equals(merge[1], ocbMarketingPushSends.getSndTime())) {
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
            merge[0] = ocbMarketingPushSends.getStdDt();
            merge[1] = ocbMarketingPushSends.getSndTime();
        }
    }
}
