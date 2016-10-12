package com.skplanet.ctas.repository.oracle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.ocb.util.AutoMappedMap;

@Repository("ctasOracleRepository")
public interface OracleRepository {

	List<AutoMappedMap> selectCampaign(Map<String, Object> params);

	int countCampaign(Map<String, Object> params);

	String nextCampaignId();

	int upsertCampaign(Map<String, Object> params);

	int deleteCampaign(Map<String, Object> params);

	List<AutoMappedMap> selectCampaignDetail(Map<String, Object> params);

	int insertCampaignDetail(Map<String, Object> params);

	int deleteCampaignDetail(Map<String, Object> params);

	List<AutoMappedMap> selectCampaignTargetingInfo(Map<String, Object> params);

	int insertCampaignTargetingInfo(@Param("username") String username, @Param("cmpgnId") String campaignId,
			@Param("targetingInfo") Map<String, Object> targetingInfo);

	int deleteCampaignTargetingInfo(@Param("cmpgnId") String campaignId);

}
