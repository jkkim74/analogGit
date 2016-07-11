package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by pepsi on 2014. 10. 1..
 */
@Data
public class MailEwma implements Serializable {
	private static final long serialVersionUID = -7638411978097824065L;
	private BigDecimal ocbTransactionCountEwma = BigDecimal.ZERO;
	private BigDecimal ocbTransactionCountLcl = BigDecimal.ZERO;// 거래건수
	private BigDecimal ocbTransactionCountUcl = BigDecimal.ZERO;// 거래건수
	private BigDecimal ocbTransactionAmountEwma = BigDecimal.ZERO;
	private BigDecimal ocbTransactionAmountLcl = BigDecimal.ZERO;//거래액
	private BigDecimal ocbTransactionAmountUcl = BigDecimal.ZERO;//거래액
	private BigDecimal ocbCustomerCountEwma = BigDecimal.ZERO;//유실적고객수
	private BigDecimal ocbCustomerCountLcl = BigDecimal.ZERO;//유실적고객수
	private BigDecimal ocbCustomerCountUcl = BigDecimal.ZERO;//유실적고객수

	private BigDecimal ocbDauEwma = BigDecimal.ZERO;
	private BigDecimal ocbDauLcl = BigDecimal.ZERO;
	private BigDecimal ocbDauUcl = BigDecimal.ZERO;
	private BigDecimal ocbMauEwma = BigDecimal.ZERO;
	private BigDecimal ocbMauLcl = BigDecimal.ZERO;
	private BigDecimal ocbMauUcl = BigDecimal.ZERO;
	private BigDecimal ocbTrEwma = BigDecimal.ZERO;
	private BigDecimal ocbTrLcl = BigDecimal.ZERO;
	private BigDecimal ocbTrUcl = BigDecimal.ZERO;
	private BigDecimal ocbMtrEwma = BigDecimal.ZERO;
	private BigDecimal ocbMtrLcl = BigDecimal.ZERO;
	private BigDecimal ocbMtrUcl = BigDecimal.ZERO;

	private BigDecimal syrupUvEwma = BigDecimal.ZERO;
	private BigDecimal syrupUvLcl = BigDecimal.ZERO;
	private BigDecimal syrupUvUcl = BigDecimal.ZERO;
	private BigDecimal syrupNewRegistEwma = BigDecimal.ZERO;
	private BigDecimal syrupNewRegistLcl = BigDecimal.ZERO;
	private BigDecimal syrupNewRegistUcl = BigDecimal.ZERO;

	private BigDecimal syrupDauEwma = BigDecimal.ZERO;
	private BigDecimal syrupDauLcl = BigDecimal.ZERO;
	private BigDecimal syrupDauUcl = BigDecimal.ZERO;
	private BigDecimal syrupMauEwma = BigDecimal.ZERO;
	private BigDecimal syrupMauLcl = BigDecimal.ZERO;
	private BigDecimal syrupMauUcl = BigDecimal.ZERO;
	private BigDecimal syrupTrEwma = BigDecimal.ZERO;
	private BigDecimal syrupTrLcl = BigDecimal.ZERO;
	private BigDecimal syrupTrUcl = BigDecimal.ZERO;
	private BigDecimal syrupMtrEwma = BigDecimal.ZERO;
	private BigDecimal syrupMtrLcl = BigDecimal.ZERO;
	private BigDecimal syrupMtrUcl = BigDecimal.ZERO;

	private BigDecimal tstoreUvEwma = BigDecimal.ZERO;
	private BigDecimal tstoreUvLcl = BigDecimal.ZERO;
	private BigDecimal tstoreUvUcl = BigDecimal.ZERO;
	private BigDecimal tstorePayAmountEwma = BigDecimal.ZERO;
	private BigDecimal tstorePayAmountLcl = BigDecimal.ZERO;
	private BigDecimal tstorePayAmountUcl = BigDecimal.ZERO;

	private BigDecimal tstorePayUserEwma = BigDecimal.ZERO;
	private BigDecimal tstorePayUserLcl = BigDecimal.ZERO;
	private BigDecimal tstorePayUserUcl = BigDecimal.ZERO;
	private BigDecimal tstoreMpayUserEwma = BigDecimal.ZERO;
	private BigDecimal tstoreMpayUserLcl = BigDecimal.ZERO;
	private BigDecimal tstoreMpayUserUcl = BigDecimal.ZERO;
	private BigDecimal tstoreTransactAmountEwma = BigDecimal.ZERO;
	private BigDecimal tstoreTransactAmountLcl = BigDecimal.ZERO;
	private BigDecimal tstoreTransactAmountUcl = BigDecimal.ZERO;
	private BigDecimal tstoreMtransactAmountEwma = BigDecimal.ZERO;
	private BigDecimal tstoreMtransactAmountLcl = BigDecimal.ZERO;
	private BigDecimal tstoreMtransactAmountUcl = BigDecimal.ZERO;

	private BigDecimal hoppinUvEwma = BigDecimal.ZERO;
	private BigDecimal hoppinUvLcl = BigDecimal.ZERO;
	private BigDecimal hoppinUvUcl = BigDecimal.ZERO;
	private BigDecimal hoppinSalesEwma = BigDecimal.ZERO;
	private BigDecimal hoppinSalesLcl = BigDecimal.ZERO;
	private BigDecimal hoppinSalesUcl = BigDecimal.ZERO;

	private BigDecimal hoppinContentsUserEwma = BigDecimal.ZERO;
	private BigDecimal hoppinContentsUserLcl = BigDecimal.ZERO;
	private BigDecimal hoppinContentsUserUcl = BigDecimal.ZERO;
	private BigDecimal hoppinMcontentsUserEwma = BigDecimal.ZERO;
	private BigDecimal hoppinMcontentsUserLcl = BigDecimal.ZERO;
	private BigDecimal hoppinMcontentsUserUcl = BigDecimal.ZERO;

	private BigDecimal tmapUvEwma = BigDecimal.ZERO;
	private BigDecimal tmapUvLcl = BigDecimal.ZERO;
	private BigDecimal tmapUvUcl = BigDecimal.ZERO;
	private BigDecimal tmapNewRegistEwma = BigDecimal.ZERO;
	private BigDecimal tmapNewRegistLcl = BigDecimal.ZERO;
	private BigDecimal tmapNewRegistUcl = BigDecimal.ZERO;

	private BigDecimal tmapDauEwma = BigDecimal.ZERO;
	private BigDecimal tmapDauLcl = BigDecimal.ZERO;
	private BigDecimal tmapDauUcl = BigDecimal.ZERO;
	private BigDecimal tmapMauEwma = BigDecimal.ZERO;
	private BigDecimal tmapMauLcl = BigDecimal.ZERO;
	private BigDecimal tmapMauUcl = BigDecimal.ZERO;

	private BigDecimal tcloudUvEwma = BigDecimal.ZERO;
	private BigDecimal tcloudUvLcl = BigDecimal.ZERO;
	private BigDecimal tcloudUvUcl = BigDecimal.ZERO;
	private BigDecimal tcloudMuvEwma = BigDecimal.ZERO;
	private BigDecimal tcloudMuvLcl = BigDecimal.ZERO;
	private BigDecimal tcloudMuvUcl = BigDecimal.ZERO;

	private BigDecimal sk11DirectVisitEwma = BigDecimal.ZERO;
	private BigDecimal sk11DirectVisitLcl = BigDecimal.ZERO;
	private BigDecimal sk11DirectVisitUcl = BigDecimal.ZERO;
	private BigDecimal sk11PayCustomerCountEwma = BigDecimal.ZERO;
	private BigDecimal sk11PayCustomerCountLcl = BigDecimal.ZERO;
	private BigDecimal sk11PayCustomerCountUcl = BigDecimal.ZERO;
	private BigDecimal sk11PayAmountEwma = BigDecimal.ZERO;
	private BigDecimal sk11PayAmountLcl = BigDecimal.ZERO;
	private BigDecimal sk11PayAmountUcl = BigDecimal.ZERO;

	private BigDecimal sk11GmvEwma = BigDecimal.ZERO;
	private BigDecimal sk11GmvLcl = BigDecimal.ZERO;
	private BigDecimal sk11GmvUcl = BigDecimal.ZERO;
	private BigDecimal sk11MgmvEwma = BigDecimal.ZERO;
	private BigDecimal sk11MgmvLcl = BigDecimal.ZERO;
	private BigDecimal sk11MgmvUcl = BigDecimal.ZERO;
	private BigDecimal sk11MobileGmvEwma = BigDecimal.ZERO;
	private BigDecimal sk11MobileGmvLcl = BigDecimal.ZERO;
	private BigDecimal sk11MobileGmvUcl = BigDecimal.ZERO;
	private BigDecimal sk11MobileMgmvEwma = BigDecimal.ZERO;
	private BigDecimal sk11MobileMgmvLcl = BigDecimal.ZERO;
	private BigDecimal sk11MobileMgmvUcl = BigDecimal.ZERO;
	private BigDecimal sk11MobileLvEwma = BigDecimal.ZERO;
	private BigDecimal sk11MobileLvLcl = BigDecimal.ZERO;
	private BigDecimal sk11MobileLvUcl = BigDecimal.ZERO;
	private BigDecimal sk11MobileMlvEwma = BigDecimal.ZERO;
	private BigDecimal sk11MobileMlvLcl = BigDecimal.ZERO;
	private BigDecimal sk11MobileMlvUcl = BigDecimal.ZERO;
}
