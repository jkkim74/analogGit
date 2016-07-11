package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * The BpmSvcCd model class.
 *
 * @author sjune
 */
@Data
public class BpmSvcCd implements Serializable {
    private static final long serialVersionUID = -9004674410632783611L;
    private String svcCdId; // SVC_ID, IDX_CL_GRP_CD 등등이 매핑될 수 있으므로 String type!
	private String svcCdNm;
	private String svcCdTitle;
}
