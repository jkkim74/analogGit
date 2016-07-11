package com.skplanet.bisportal.model.acl;

/**
 * Created by pepsi on 2014. 5. 27..
 */

import java.io.Serializable;

import lombok.Data;

/**
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 * @Desc 접근 로그 테이블
 */
@Data
public class LoginLog implements Serializable {
	private static final long serialVersionUID = -7539219244927811477L;
	private String username;
	private String ip;
	private String agent;
	private String lastUpdate;
}
