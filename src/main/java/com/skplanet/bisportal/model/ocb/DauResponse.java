package com.skplanet.bisportal.model.ocb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * The DauResponse class(DAU 응답 정보).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 * @see
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DauResponse implements Serializable {
	private static final long serialVersionUID = -3039951537841839519L;
	private Dau dau;
	private List<RtdDau> ocbs;
	private List<RtdDau> syrups;
}
