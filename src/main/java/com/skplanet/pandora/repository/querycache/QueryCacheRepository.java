package com.skplanet.pandora.repository.querycache;

import java.util.List;
import java.util.Map;

import com.skplanet.web.model.SingleReq;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.web.model.AutoMap;

@Repository
public interface QueryCacheRepository {

	List<AutoMap> selectAgreementInfo(@Param("mbrId") String mbrId);

	List<AutoMap> selectJoinInfo(@Param("mbrId") String mbrId);

	List<AutoMap> selectTransactionHistory(@Param("mbrId") String mbrId);

	List<AutoMap> selectEmailSendHistory(@Param("mbrId") String mbrId);

	List<AutoMap> selectAppPushHistory(@Param("mbrId") String mbrId);

	List<AutoMap> selectTrSingleRequest(SingleReq singleReq);

	List<AutoMap> selectSearchEmail(Map<String, Object> params);

}
