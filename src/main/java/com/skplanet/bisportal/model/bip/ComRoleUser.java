package com.skplanet.bisportal.model.bip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * The ComRoleUser class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComRoleUser implements Serializable {
	private static final long serialVersionUID = -3547063231057267359L;
	private long roleId;
	private int count;
	private String name;
	private String loginId;
	private String userNm;
	private String loginidUserNm;
	private String orgNm;
	private String userType;
	private String auditId;
	private String auditDtm;
}
