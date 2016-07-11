package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by pepsi on 2014. 10. 2..
 */
@Data
public class MailImage implements Serializable {
	private static final long serialVersionUID = 7744022964934045053L;
	private String ocbTransactionCountBrandUrl;
	private String ocbTransactionAmountBrandUrl;
	private String ocbCustomerBrandUrl;
	private String ocbTransactionCountDimensionUrl;
	private String ocbTransactionAmountDimensionUrl;
	private String ocbCustomerDimensionUrl;

	private String ocbDauBrandUrl;
	private String ocbTrBrandUrl;
	private String ocbDauDimensionUrl;
	private String ocbTrDimensionUrl;

	private String syrupUvBrandUrl;
	private String syrupNewRegistBrandUrl;
	private String syrupUvDimensionUrl;
	private String syrupNewRegistDimensionUrl;

	private String syrupDauBrandUrl;
	private String syrupTrBrandUrl;
	private String syrupDauDimensionUrl;
	private String syrupTrDimensionUrl;

	private String tstoreUvBrandUrl;
	private String tstorePayAmountBrandUrl;
	private String tstoreUvDimensionUrl;
	private String tstorePayAmountDimensionUrl;

	private String tstorePayUserBrandUrl;
	private String tstoreTransactAmountBrandUrl;
	private String tstorePayUserDimensionUrl;
	private String tstoreTransactAmountDimensionUrl;

	private String hoppinUvBrandUrl;
	private String hoppinNewRegistBrandUrl;
	private String hoppinUvDimensionUrl;
	private String hoppinNewRegistDimensionUrl;

	private String hoppinContentsUserBrandUrl;
	private String hoppinContentsUserDimensionUrl;

	private String tmapUvBrandUrl;
	private String tmapNewRegistBrandUrl;
	private String tmapUvDimensionUrl;
	private String tmapNewRegistDimensionUrl;

	private String tmapMauBrandUrl;
	private String tmapMauDimensionUrl;

	private String tcloudUvBrandUrl;
	private String tcloudUvDimensionUrl;

	private String sk11DirectVisitBrandUrl;
	private String sk11PayCustomerCountBrandUrl;
	private String sk11PayAmountBrandUrl;
	private String sk11DirectVisitDimensionUrl;
	private String sk11PayCustomerCountDimensionUrl;
	private String sk11PayAmountDimensionUrl;

	private String sk11GmvBrandUrl;
	private String sk11MobileGmvBrandUrl;
	private String sk11MobileLvBrandUrl;
	private String sk11GmvDimensionUrl;
	private String sk11MobileGmvDimensionUrl;
	private String sk11MobileLvDimensionUrl;
}
