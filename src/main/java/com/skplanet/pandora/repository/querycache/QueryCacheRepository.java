package com.skplanet.pandora.repository.querycache;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.ocb.model.AutoMappedMap;

@Repository
public interface QueryCacheRepository {

	List<AutoMappedMap> selectAgreementInfo(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectJoinInfo(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectTransactionHistory(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectEmailSendHistory(@Param("mbrId") String mbrId);

	List<AutoMappedMap> selectAppPushHistory(@Param("mbrId") String mbrId);

}
