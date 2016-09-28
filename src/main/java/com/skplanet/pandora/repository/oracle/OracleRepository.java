package com.skplanet.pandora.repository.oracle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.UploadProgress;

public interface OracleRepository {

	AutoMappedMap selectTest(Map<String, Object> params);

	int countTable(@Param("pageId") String pageId, @Param("username") String username);

	int createTable(@Param("pageId") String pageId, @Param("username") String username);

	void truncateTable(@Param("pageId") String pageId, @Param("username") String username);

	List<AutoMappedMap> selectUploadedPreview(@Param("pageId") String pageId, @Param("username") String username);

	int countUploadedPreview(@Param("pageId") String pageId, @Param("username") String username);

	List<AutoMappedMap> selectMembers(@Param("uploadProgress") UploadProgress uploadProgress,
			@Param("offset") int offset, @Param("limit") int limit);

	int countMembers(@Param("uploadProgress") UploadProgress uploadProgress);

	String selectMbrId(Map<String, Object> params);

	List<AutoMappedMap> selectMemberInfo(Map<String, Object> params);

	List<AutoMappedMap> selectJoinInfo(Map<String, Object> params);

	List<AutoMappedMap> selectLastestUsageInfo(Map<String, Object> params);

	List<AutoMappedMap> selectMarketingMemberInfo(Map<String, Object> params);

	List<AutoMappedMap> selectMarketingMemberInfoHistory(Map<String, Object> params);

	List<AutoMappedMap> selectThirdPartyProvideHistory(Map<String, Object> params);

	List<AutoMappedMap> selectCardList(Map<String, Object> params);

	List<AutoMappedMap> selectClphnNoDup(Map<String, Object> params);

	List<AutoMappedMap> selectEmailAddrDup(Map<String, Object> params);

	List<AutoMappedMap> selectExpirePointTargets(Map<String, Object> params);

	List<AutoMappedMap> selectNotificationResults(Map<String, Object> params);

}
