package com.skplanet.bisportal.view.ocb;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.ObsFeedSta;
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
 * The FeedExposureOrderExcelView class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
public class FeedExposureOrderExcelView extends AbstractExcelView {
  /**
   * feed(노출순서별) 리포트 엑셀 저장.
   * 
   * @param model 요청 파라미터 객체들.
   * @param workbook 엑셀 객체.
   * @param request HttpServletRequest.
   * @param response HttpServletResponse.
   * @return void
   */
  @Override
  @SuppressWarnings({"unchecked"})
  protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    String menuName = "feed(노출순서별)";
    Sheet worksheet = workbook.createSheet(menuName);
    String headers[][] =
        {{"피드ID", "피드명", "시작일자", "종료일자", "IS인벤토리", "제휴사타겟팅", "노출순서", "기준일자", "클릿횟수", "클릭UV", "클릭LV"}};
    int headLength = headers[0].length;

    // 칼럼 넓이 조정.
    super.setColumnWith(worksheet, headLength);
    // 타이틀 입력.
    super.buildTitle(worksheet, menuName, headLength);
    // 헤드 입력.
    super.buildHeaders(worksheet, headers, 0, 0);

    // Row offset
    int startRowIndex = 3;

    // 엑셀 바디의 셀 스타일 정의(문자 가운데 정렬).
    CellStyle styleHVCenter = ExcelUtil.getCenterStyle(worksheet);
    // 엑셀 바디의 셀 스타일 정의(숫자 오른쪽 정렬, 3자리 콤마).
    CellStyle styleHVRight = ExcelUtil.getRightStyle(workbook, worksheet);

    // 엑셀 바디 생성.
    List<ObsFeedSta> obsFeedStas = uncheckedCast(model.get("feedExposureOrderData"));
    String[] merge = new String[2];
    int startRow = 3, lastRow = 3, startSexRow = 3, lastSexRow = 3;
    int obsFeedStaSize = obsFeedStas.size();
    for (ObsFeedSta obsFeedSta : obsFeedStas) {
      // Create a new row
      Row row = worksheet.createRow(startRowIndex++);
      // Retrieve the 피드ID value
      Cell cell1 = row.createCell(0);
      cell1.setCellValue(obsFeedSta.getFeedId());
      cell1.setCellStyle(styleHVCenter);

      // Retrieve the 피드명 value
      Cell cell2 = row.createCell(1);
      cell2.setCellValue(obsFeedSta.getFeedNm());
      cell2.setCellStyle(styleHVCenter);

      // Retrieve the 시작일자 value
      Cell cell3 = row.createCell(2);
      cell3.setCellValue(obsFeedSta.getFeedStartDate());
      cell3.setCellStyle(styleHVCenter);

      // Retrieve the 종료일자 value
      Cell cell4 = row.createCell(3);
      cell4.setCellValue(obsFeedSta.getFeedEndDate());
      cell4.setCellStyle(styleHVCenter);

      // Retrieve the IS인벤토리 value
      Cell cell5 = row.createCell(4);
      cell5.setCellValue(obsFeedSta.getIsYn());
      cell5.setCellStyle(styleHVCenter);

      // Retrieve the 제휴사타겟팅 value
      Cell cell6 = row.createCell(5);
      cell6.setCellValue(obsFeedSta.getAllianceYn());
      cell6.setCellStyle(styleHVCenter);

      // Retrieve the 노출순서 value
      Cell cell7 = row.createCell(6);
      cell7.setCellValue(obsFeedSta.getDispOrder());
      cell7.setCellStyle(styleHVCenter);

      // Retrieve the 기준일자 value
      Cell cell8 = row.createCell(7);
      cell8.setCellValue(obsFeedSta.getStdDt());
      cell8.setCellStyle(styleHVCenter);

      // Retrieve the 클릭 횟수 value
      Cell cell9 = row.createCell(8);
      cell9.setCellValue(obsFeedSta.getClickCnt());
      cell9.setCellStyle(styleHVRight);

      // Retrieve the 클릭 UV value
      Cell cell10 = row.createCell(9);
      cell10.setCellValue(obsFeedSta.getClickCntOnUv());
      cell10.setCellStyle(styleHVRight);

      // Retrieve the클릭 LV value
      Cell cell11 = row.createCell(10);
      cell11.setCellValue(obsFeedSta.getClickCntOnLv());
      cell11.setCellStyle(styleHVRight);

      if (StringUtils.equals(merge[0], obsFeedSta.getFeedId())) {
        lastRow++;
        if ((startRowIndex == obsFeedStaSize + 3) && (startRow < lastRow)) {
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 1, 1));
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 2, 2));
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 3, 3));
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 4, 4));
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 5, 5));
        }
      } else {
        if (startRow < lastRow) {
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 0, 0));
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 1, 1));
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 2, 2));
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 3, 3));
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 4, 4));
          worksheet.addMergedRegion(new CellRangeAddress(startRow, lastRow, 5, 5));
        }
        startRow = startRowIndex - 1;
        lastRow = startRowIndex - 1;
      }

      if (StringUtils.equals(merge[1], obsFeedSta.getDispOrder())) {
        lastSexRow++;
        if ((startRowIndex == obsFeedStaSize + 3) && (startSexRow < lastSexRow)) {
          worksheet.addMergedRegion(new CellRangeAddress(startSexRow, lastSexRow, 6, 6));
        }
      } else {
        if (startSexRow < lastSexRow) {
          worksheet.addMergedRegion(new CellRangeAddress(startSexRow, lastSexRow, 6, 6));
        }
        startSexRow = startRowIndex - 1;
        lastSexRow = startRowIndex - 1;
      }
      merge[0] = obsFeedSta.getFeedId();
      merge[1] = obsFeedSta.getDispOrder();
    }
  }
}
