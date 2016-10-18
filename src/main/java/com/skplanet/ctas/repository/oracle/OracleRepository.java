package com.skplanet.ctas.repository.oracle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.ocb.util.AutoMappedMap;

@Repository("ctasOracleRepository")
public interface OracleRepository {

	/* Campaign */
	List<AutoMappedMap> selectCampaigns(Map<String, Object> params);

	int countCampaigns(Map<String, Object> params);

	String nextCampaignId();

	AutoMappedMap selectCampaign(Map<String, Object> params);

	int upsertCampaign(Map<String, Object> params);

	int deleteCampaign(Map<String, Object> params);

	/* Campaign Detail */
	List<AutoMappedMap> selectCampaignDetails(Map<String, Object> params);

	String nextCellId(Map<String, Object> params);

	int upsertCampaignDetail(Map<String, Object> params);

	int deleteCampaignDetail(Map<String, Object> params);

	int balanceCellExtrctCnt(Map<String, Object> params);

	/* Campaign Targeting */
	List<AutoMappedMap> selectCampaignTargetingInfo(Map<String, Object> params);

	int insertCampaignTargetingInfo(@Param("username") String username, @Param("cmpgnId") String campaignId,
			@Param("targetingInfo") Map<String, Object> targetingInfo);

	int deleteCampaignTargetingInfo(Map<String, Object> params);

	int countCampaignTargetingCsvTable(Map<String, Object> params);

	void createCampaignTargetingCsvTable(Map<String, Object> params);

	void truncateCampaignTargetingCsvTable(Map<String, Object> params);

	void insertCampaignTargetingCsvTmp(@Param("cmpgnId") String campaignId, @Param("mbrIdList") List<String> bulk);

	void migrateCampaignTargetingCsv(Map<String, Object> params);

	int countCampaignTargetingCsvTmp(Map<String, Object> params);

	int countCampaignTargetingCsv(Map<String, Object> params);

	List<AutoMappedMap> selectCell(Map<String, Object> params);

}
