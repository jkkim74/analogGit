package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by pepsi on 2014. 9. 15..
 */
@Data
public class MailReportSum implements Serializable {
	private static final long serialVersionUID = 2198886466862236398L;
	// 지난 30일간 데이터 합계
	private BigDecimal ocbTransactionCountSum = BigDecimal.ZERO;// 거래건수
	private BigDecimal ocbTransactionAmountSum = BigDecimal.ZERO;//거래액
	private BigDecimal ocbCustomerCountSum = BigDecimal.ZERO;//유실적고객수
	private BigDecimal syrupUvSum = BigDecimal.ZERO;
	private BigDecimal syrupNewRegistSum = BigDecimal.ZERO;
	private BigDecimal tstoreUvSum = BigDecimal.ZERO;
	private BigDecimal tstorePayAmountSum = BigDecimal.ZERO;
	private BigDecimal hoppinUvSum = BigDecimal.ZERO;
	private BigDecimal hoppinSalesSum = BigDecimal.ZERO;
	private BigDecimal tmapUvSum = BigDecimal.ZERO;
	private BigDecimal tmapNewRegistSum = BigDecimal.ZERO;
	private BigDecimal sk11DirectVisitSum = BigDecimal.ZERO;
	private BigDecimal sk11PayCustomerCountSum = BigDecimal.ZERO;
	private BigDecimal sk11PayAmountSum = BigDecimal.ZERO;

	// 지난 30일간 데이터 합계 항목수
	private int ocbTransactionCountCount;
	private int ocbTransactionAmountCount;
	private int ocbCustomerCountCount;
	private int syrupUvCount;
	private int syrupNewRegistCount;
	private int tstoreUvCount;
	private int tstorePayAmountCount;
	private int hoppinUvCount;
	private int hoppinSalesCount;
	private int tmapUvCount;
	private int tmapNewRegistCount;
	private int sk11DirectVisitCount;
	private int sk11PayCustomerCountCount;
	private int sk11PayAmountCount;

	// 전달 누적값
	private BigDecimal oneMonthAgoOcbTransactionCountSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoOcbTransactionAmountSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoOcbCustomerCountSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoSyrupUvSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoSyrupNewRegistSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoTstoreUvSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoTstorePayAmountSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoHoppinUvSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoHoppinSalesSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoTmapUvSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoTmapNewRegistSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoSk11DirectVisitSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoSk11PayCustomerCountSum = BigDecimal.ZERO;
	private BigDecimal oneMonthAgoSk11PayAmountSum = BigDecimal.ZERO;

	// 이번달 누적값
	private BigDecimal thisMonthOcbTransactionCountSum = BigDecimal.ZERO;
	private BigDecimal thisMonthOcbTransactionAmountSum = BigDecimal.ZERO;
	private BigDecimal thisMonthOcbCustomerCountSum = BigDecimal.ZERO;
	private BigDecimal thisMonthSyrupUvSum = BigDecimal.ZERO;
	private BigDecimal thisMonthSyrupNewRegistSum = BigDecimal.ZERO;
	private BigDecimal thisMonthTstoreUvSum = BigDecimal.ZERO;
	private BigDecimal thisMonthTstorePayAmountSum = BigDecimal.ZERO;
	private BigDecimal thisMonthHoppinUvSum = BigDecimal.ZERO;
	private BigDecimal thisMonthHoppinSalesSum = BigDecimal.ZERO;
	private BigDecimal thisMonthTmapUvSum = BigDecimal.ZERO;
	private BigDecimal thisMonthTmapNewRegistSum = BigDecimal.ZERO;
	private BigDecimal thisMonthSk11DirectVisitSum = BigDecimal.ZERO;
	private BigDecimal thisMonthSk11PayCustomerCountSum = BigDecimal.ZERO;
	private BigDecimal thisMonthSk11PayAmountSum = BigDecimal.ZERO;

	//당일 지표값
	private BigDecimal ocbTransactionCountBasicValue = BigDecimal.ZERO;
	private BigDecimal ocbTransactionAmountBasicValue = BigDecimal.ZERO;
	private BigDecimal ocbCustomerCountBasicValue = BigDecimal.ZERO;
	private BigDecimal syrupUvBasicValue = BigDecimal.ZERO;
	private BigDecimal syrupNewRegistBasicValue = BigDecimal.ZERO;
	private BigDecimal tstoreUvBasicValue = BigDecimal.ZERO;
	private BigDecimal tstorePayAmountBasicValue = BigDecimal.ZERO;
	private BigDecimal hoppinUvBasicValue = BigDecimal.ZERO;
	private BigDecimal hoppinSalesBasicValue = BigDecimal.ZERO;
	private BigDecimal tmapUvBasicValue = BigDecimal.ZERO;
	private BigDecimal tmapNewRegistBasicValue = BigDecimal.ZERO;
	private BigDecimal sk11DirectVisitBasicValue = BigDecimal.ZERO;
	private BigDecimal sk11PayCustomerCountBasicValue = BigDecimal.ZERO;
	private BigDecimal sk11PayAmountBasicValue = BigDecimal.ZERO;

	private MailEwma mailEwma;
}
