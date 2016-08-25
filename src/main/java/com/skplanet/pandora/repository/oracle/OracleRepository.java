package com.skplanet.pandora.repository.oracle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.pandora.model.AutoMappedMap;

@Repository
public interface OracleRepository {

	Map<String, Object> selectTest(Map<String, Object> params);

	int countTable(@Param("pageId") String pageId, @Param("username") String username);

	int createTable(@Param("pageId") String pageId, @Param("username") String username);

	void truncateTable(@Param("pageId") String pageId, @Param("username") String username);

	int insertBulk(@Param("pageId") String pageId, @Param("username") String username,
			@Param("bulkList") List<String> bulkList);

	List<AutoMappedMap> selectPreview(@Param("pageId") String pageId, @Param("username") String username);

	List<AutoMappedMap> selectMemberInfo(Map<String, Object> params);

	List<Map<String, Object>> selectTmp(@Param("pageId") String pageId, @Param("username") String username);

}
