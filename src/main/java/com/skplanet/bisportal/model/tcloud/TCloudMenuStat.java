package com.skplanet.bisportal.model.tcloud;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * Created by seoseungho on 2014. 12. 23..
 * 20150604 extends BasePivot - cookatrice
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TCloudMenuStat extends BasePivot implements Serializable {
	private static final long serialVersionUID = 3303946856276605748L;
	// 큰 메뉴 정보, 예) 클라우드, 동영상 등
	private String standardDate;
	private String title;
	private String menuDepth1;
	private String menuDepth2;
	private BigInteger uv;
	private BigInteger pv;
	private BigInteger stayTime;
}
