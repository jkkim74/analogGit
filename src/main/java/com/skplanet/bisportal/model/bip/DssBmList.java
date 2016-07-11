package com.skplanet.bisportal.model.bip;

import lombok.Data;

/**
 * Created by seoseungho on 2014. 9. 25..
 */
@Data
public class DssBmList {
    private Long id;

    // DSS ID
    private Long dssId;

    // BM ID (MENU 테이블 참조) 향후 바꿔야 함.
    private Long bmId;

    // 사용자 표시용 데이터
    private String bmName;
    private String createDate;
    private String createId;
    private String updateId;
    private String updateDate;
}
