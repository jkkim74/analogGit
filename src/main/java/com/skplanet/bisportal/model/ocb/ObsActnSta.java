package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by cookatrice on 2014. 5. 14..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc 고객별액션
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsActnSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -5772438166071435993L;
	private long starAcmUserCnt;
	private long starUserCnt;
	private long ptrnAcmCnt;
	private long ptrnCnt;
	private long ptrnAcmUserCnt;
	private long ptrnUserCnt;
	private long feedClickAcmCnt;
	private long feedClickCnt;
	private long feedClickAcmUserCnt;
	private long feedClickUserCnt;
	private long dnldCmplnAcmCnt;
	private long dnldCmplnCnt;
	private long dnldCmplnAcmUserCnt;
	private long dnldCmplnUserCnt;
	private long dnldClickAcmCnt;
	private long dnldClickCnt;
	private long dnldClickAcmUserCnt;
	private long dnldClickUserCnt;
	private long presntCmplnAcmCnt;
	private long presntCmplnCnt;
	private long presntCmplnAcmUserCnt;
	private long presntCmplnUserCnt;
	private long presntClickAcmCnt;
	private long presntClickCnt;
	private long presntClickAcmUserCnt;
	private long presntClickUserCnt;
	private long prchsCmplnAcmCnt;
	private long prchsCmplnCnt;
	private long prchsCmplnAcmUserCnt;
	private long prchsCmplnUserCnt;
	private long prchsClickAcmCnt;
	private long prchsClickCnt;
	private long prchsClickAcmUserCnt;
	private long prchsClickUserCnt;
	private long shrClickAcmCnt;
	private long shrClickCnt;
	private long shrClickAcmUserCnt;
	private long shrClickUserCnt;
	private long reviewRegAcmCnt;
	private long reviewRegCnt;
	private long reviewRegAcmUserCnt;
	private long reviewRegUserCnt;
	private long photoRegAcmCnt;
	private long photoRegCnt;
	private long photoRegAcmUserCnt;
	private long photoRegUserCnt;
	private long callAcmCnt;
	private long callCnt;
	private long callAcmUserCnt;
	private long callUserCnt;
	private long chkinAcmCnt;
	private long chkinCnt;
	private long chkinAcmUserCnt;
	private long chkinUserCnt;
	private String pocIndCd;
	private String stdDt;
}
