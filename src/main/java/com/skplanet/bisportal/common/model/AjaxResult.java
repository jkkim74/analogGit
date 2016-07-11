package com.skplanet.bisportal.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * The AjaxResult class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AjaxResult implements Serializable {
	private static final long serialVersionUID = 3144914125643180063L;
	private int code;// 200: 성공, 400, bad requets, 404: data not found, 500: 비즈니스 로직 에러, 998: Exception
	private String message;//실패시 메세지 지정.
	private String auditId;
}
