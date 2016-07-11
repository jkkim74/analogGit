package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * The Navigation class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class Navigation implements Serializable {
	private static final long serialVersionUID = 0L;
	private String ssbiMenu; // Self-Service BI 네비게이 메뉴 허용 여부
	private String ssbiEduMenu; // BI EDU 서비스 리포트 접근 허용 여부
	private String adminMenu; // Admin 메뉴 디스플레이 여부
	private String helpDeskAdmin; // 헬프데스크 Admin
}
