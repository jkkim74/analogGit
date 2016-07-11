package com.skplanet.bisportal.model.bip;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

/**
 * The OrgUser class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgUser implements Serializable {
	private static final long serialVersionUID = -6990273106301144212L;
	private String orgId;
	private String orgNm;
	private String supOrgCd;
	private String supOrgNm;
	private String orgPrtSeq;
	private String maxLvl;
	private String lvl;
	private String userId;
	private String userNm;
	private String loginId;
	private String emailAddr;
	private String orgCd;
	private String orgTypCd;
	private String key;
	private String title;
	private String tooltip;
	private String prevLvl;
	private String nextLvl;
	private String langCd;
	private String rstDispYn;
	private boolean folder;
	private boolean expand;
	private boolean activate;
	private String actvnYn;
	private boolean focus;
	private boolean hideCheckbox;
	private boolean isLazy;
	private boolean noLink;
	private boolean select;
	private boolean unselectable;
	private String psnTypCd;
	private String jbchargenm;
	private String jbcharge;
	private String userStCd;
	private String author;
	private String holdoffidivi;
	private String userType;
	private List<OrgUser> children = Lists.newArrayList();
	private String searchCondition;
	private String searchKeyword;
	private String searchUseYn;
	private String mblPhonNum;
	private String rowId;
	private String href;
	private long sndCttId;
	private String auditDtm; /*  */
	private String auditId; /*  */
	private String addClass;
	private String icon;
	private String groupType;
}
