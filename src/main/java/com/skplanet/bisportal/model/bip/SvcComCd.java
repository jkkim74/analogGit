package com.skplanet.bisportal.model.bip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by mimul on 2015. 7. 2..
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcComCd implements Serializable {
	private static final long serialVersionUID = 8856702338770727786L;
	private String auditDtm;     /* 등록일 */
	private String auditId;     /* 등록자 */
	private long ordIdx;     /* 코드 노출 순서값 */
	private String deleteYn;     /* 노출여부 */
	private String codeNm;     /* 코드값에 대응되는 이름 */
	private String code;     /* 공통 코드값 */
	private String codeType;     /* TYPE */
	private String svcId;     /* 서비스ID */

	public SvcComCd(String svcId, String codeType) {
		this.svcId = svcId;
		this.codeType = codeType;
	}
	public SvcComCd() {}
}
