package com.skplanet.bisportal.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by cookatrice on 2015. 01. 08..
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class OlapDimensionRequest extends JqGridRequest implements Serializable{
	private static final long serialVersionUID = 8281576027146830648L;
	private String useStandardDate;
	private String useOs;
	private String useSex;
	private String useAppVersion;
	private String useAgeRange;
	private String useTelecom;
	private String useMembership;
	private String useInputChannel;
	private String useCouponType;
	private String useMenu;
	private String useMenuDesc;
}
