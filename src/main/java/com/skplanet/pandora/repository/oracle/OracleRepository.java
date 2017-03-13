package com.skplanet.pandora.repository.oracle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.web.model.AutoMap;
import com.skplanet.web.model.MenuProgress;

@Repository
public interface OracleRepository {

	List<AutoMap> selectMembers(@Param("menuProgress") MenuProgress menuProgress, @Param("offset") int offset,
			@Param("limit") int limit, @Param("masking") boolean masking);

	int countMembers(@Param("menuProgress") MenuProgress menuProgress);

	String selectMbrId(Map<String, Object> params);

	String selectMbrKorNm(String mbrId);

	String selectMbrKorNmQc(Map<String, Object> params);

	List<AutoMap> selectMemberInfo(Map<String, Object> params);

	List<AutoMap> selectJoinInfo(Map<String, Object> params);

	List<AutoMap> selectLastestUsageInfo(Map<String, Object> params);

	List<AutoMap> selectMarketingMemberInfo(Map<String, Object> params);

	List<AutoMap> selectMarketingMemberInfoHistory(Map<String, Object> params);

	List<AutoMap> selectThirdPartyProvideHistory(Map<String, Object> params);

	List<AutoMap> selectCardList(Map<String, Object> params);

	List<AutoMap> selectClphnNoDup(Map<String, Object> params);

	List<AutoMap> selectEmailAddrDup(Map<String, Object> params);

	List<AutoMap> selectExtinctionSummary(Map<String, Object> params);

	List<AutoMap> selectExtinctionTargets(Map<String, Object> params);

	List<AutoMap> selectExtinctionTargetsMas(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfo1(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfo2(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfo3(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfo4(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfo5(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfo6(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfoNoMas1(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfoNoMas2(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfoNoMas3(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfoNoMas4(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfoNoMas5(Map<String, Object> params);
	
	List<AutoMap> selectTotMbrInfoNoMas6(Map<String, Object> params);

	int countExtinctionTargets(Map<String, Object> params);
	
	void updateSmsSendStatus(Map<String, Object> params);

}
