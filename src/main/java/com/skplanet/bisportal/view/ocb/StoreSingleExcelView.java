package com.skplanet.bisportal.view.ocb;

import com.skplanet.bisportal.common.utils.ExcelUtil;
import com.skplanet.bisportal.model.ocb.ObsStoreDetlSta;
import com.skplanet.bisportal.view.AbstractExcelView;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.skplanet.bisportal.common.utils.Objects.uncheckedCast;

/**
 * The StoreSingleExcelView class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
public class StoreSingleExcelView extends AbstractExcelView {
  /**
   * 매장 개별 엑셀 저장.
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
    String menuName = "매장개별";
    Sheet worksheet = workbook.createSheet(menuName);
    String headers[][] =
        {{"매장ID", "매장명", "기준일", "체크인건수", "체크인유저수", "전화걸기건수", "전화걸기유저수", "사진등록완료건수", "사진등록완료유저수",
            "리뷰등록완료건수", "리뷰등록완료유저수", "혜택제공건수", "보유단골개수"}};
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
    List<ObsStoreDetlSta> obsStoreDetlStas = uncheckedCast(model.get("storeSingleData"));
    for (ObsStoreDetlSta obsStoreDetlSta : obsStoreDetlStas) {
      Row row = worksheet.createRow(startRowIndex++);
      // Retrieve the 매장ID value
      Cell cell1 = row.createCell(0);
      cell1.setCellValue(obsStoreDetlSta.getStoreId());
      cell1.setCellStyle(styleHVCenter);

      // Retrieve the 매장명 value
      Cell cell2 = row.createCell(1);
      cell2.setCellValue(obsStoreDetlSta.getStoreNm());
      cell2.setCellStyle(styleHVCenter);

      // Retrieve the 기준일 value
      Cell cell3 = row.createCell(2);
      cell3.setCellValue(obsStoreDetlSta.getStdDt());
      cell3.setCellStyle(styleHVCenter);

      // Retrieve the 체크인건수 value
      Cell cell4 = row.createCell(3);
      cell4.setCellValue(obsStoreDetlSta.getChkinCnt());
      cell4.setCellStyle(styleHVRight);

      // Retrieve the 체크인유저수 value
      Cell cell5 = row.createCell(4);
      cell5.setCellValue(obsStoreDetlSta.getChkinUserCnt());
      cell5.setCellStyle(styleHVRight);

      // Retrieve the 전화걸기건수 value
      Cell cell6 = row.createCell(5);
      cell6.setCellValue(obsStoreDetlSta.getCallCnt());
      cell6.setCellStyle(styleHVRight);

      // Retrieve the 전화걸기유저수 value
      Cell cell7 = row.createCell(6);
      cell7.setCellValue(obsStoreDetlSta.getCallUserCnt());
      cell7.setCellStyle(styleHVRight);

      // Retrieve the 사진등록완료건수 value
      Cell cell8 = row.createCell(7);
      cell8.setCellValue(obsStoreDetlSta.getPhotoRegCnt());
      cell8.setCellStyle(styleHVRight);

      // Retrieve the 사진등록완료유저수 value
      Cell cell9 = row.createCell(8);
      cell9.setCellValue(obsStoreDetlSta.getPhotoRegUserCnt());
      cell9.setCellStyle(styleHVRight);

      // Retrieve the 리뷰등록완료건수 value
      Cell cell10 = row.createCell(9);
      cell10.setCellValue(obsStoreDetlSta.getRviewRegCnt());
      cell10.setCellStyle(styleHVRight);

      // Retrieve the 리뷰등록완료유저수 value
      Cell cell11 = row.createCell(10);
      cell11.setCellValue(obsStoreDetlSta.getRviewRegUserCnt());
      cell11.setCellStyle(styleHVRight);

      // Retrieve the 혜택제공건수 value
      Cell cell12 = row.createCell(11);
      cell12.setCellValue(obsStoreDetlSta.getBnftOfferCnt());
      cell12.setCellStyle(styleHVRight);

      // Retrieve the 보유단골개수 value
      Cell cell13 = row.createCell(12);
      cell13.setCellValue(obsStoreDetlSta.getPtrnCnt());
      cell13.setCellStyle(styleHVRight);
    }
  }
}
