package com.skplanet.pandora.repository.oracle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.Member;
import com.skplanet.pandora.model.Preview;
import com.skplanet.pandora.model.UploadProgress;

@Repository
public interface OracleRepository {

	AutoMappedMap selectTest(Map<String, Object> params);

	int countTable(@Param("pageId") String pageId, @Param("username") String username);

	int createTable(@Param("pageId") String pageId, @Param("username") String username);

	void truncateTable(@Param("pageId") String pageId, @Param("username") String username);

	int insertBulk(@Param("pageId") String pageId, @Param("username") String username,
			@Param("bulkList") List<String> bulkList);

	List<Preview> selectPreview(@Param("pageId") String pageId, @Param("username") String username);

	List<Member> selectMergedMember(@Param("uploadProgress") UploadProgress uploadProgress, @Param("offset") int offset,
			@Param("limit") int limit);

}
