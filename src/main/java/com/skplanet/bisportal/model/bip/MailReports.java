package com.skplanet.bisportal.model.bip;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The MailReports class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class MailReports implements Serializable {
	private static final long serialVersionUID = -6731472809982591532L;
	private String yesterday;
	private String ocbYesterday;
	private Map<String, List<MailReport>> reports;
	private MailImage mailImage;
	private String ocbComment = StringUtils.EMPTY;
	private String syrupComment = StringUtils.EMPTY;
	private String tstoreComment = StringUtils.EMPTY;
	private String hoppinComment = StringUtils.EMPTY;
	private String tmapComment = StringUtils.EMPTY;
	private String sk11Comment = StringUtils.EMPTY;
	private String tcloudComment = StringUtils.EMPTY;
}
