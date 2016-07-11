package com.skplanet.bisportal.model.bip;

import lombok.Data;

/**
 * Created by sjune on 2014-07-01.
 * 
 * @author sjune
 */
@Data
public class EisSvcComCd {
    private Long svcId;
	private String idxClGrpCd;
	private String idxClCd;
	private String idxCttCd;
	private String idxCttCdVal;
	private String idxCttCdDesc;
	private Long ordIdx; //정렬순서
}
