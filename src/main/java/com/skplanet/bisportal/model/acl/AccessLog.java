package com.skplanet.bisportal.model.acl;

/**
 * Created by pepsi on 2014. 5. 27..
 */

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 * @Desc 화면 접근 로그 테이블
 */
@Data
public class AccessLog implements Serializable {
	private static final long serialVersionUID = -588956547937997050L;
	private String username;
	private String serviceCode;
	private String categoryCode;
	private String menuCode;
	private String ip;
	private String agent;
	private String lastUpdate;

    //for report 사용현황
    private String kind;    //구분
    private String cnt;     //사용수
}
