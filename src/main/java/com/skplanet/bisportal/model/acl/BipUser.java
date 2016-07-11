package com.skplanet.bisportal.model.acl;

import lombok.Data;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 * @Desc 사용자(회원) 테이블
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class BipUser extends Organization implements Serializable {
    private static final long serialVersionUID = -5150111028543616289L;
    private int id;
	private int organizationId;
	private String username;
	private String password;
	private boolean loginCheck;
	private String fullname;
	private String sex;
	private String email;
	private String mobile;
	private int active;
	private String voyagerToken;
	private String roleJson;
	private String locale;
	private String ip;
	private String signupDate;
	private String lastupDate;
	private String returnUrl;

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
