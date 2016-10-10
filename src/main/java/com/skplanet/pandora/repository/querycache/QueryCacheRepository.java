package com.skplanet.pandora.repository.querycache;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.skplanet.pandora.model.AutoMappedMap;

public interface QueryCacheRepository {

	List<AutoMappedMap> selectTest(Map<String, Object> params);

	List<AutoMappedMap> selectAgreementInfo(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectJoinInfo(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectTransactionHistory(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectEmailSendHistory(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectAppPushHistory(@Param("mbrId") String mbrId);

	// CTAS
	List<AutoMappedMap> selectTargeting(Map<String, Object> params);

}
