package com.skplanet.pandora.repository.querycache;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.pandora.model.AutoMappedMap;

@Repository("pandoraQueryCacheRepository")
public interface QueryCacheRepository {

	List<AutoMappedMap> selectTest(Map<String, Object> params);

	List<AutoMappedMap> selectAgreementInfo(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectJoinInfo(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectTransactionHistory(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectEmailSendHistory(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectAppPushHistory(@Param("mbrId") String mbrId);

}
