package com.skplanet.pandora.repository.oracle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.pandora.common.Constant;
import com.skplanet.pandora.model.AutoMappedMap;

@Repository
public interface OracleRepository {

	Map<String, Object> selectTest(Map<String, Object> params);

	int countTable(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username);

	int createTable(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username);

	void truncateTable(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username);

	int insertBulk(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username,
			@Param("bulkList") List<String> bulkList);

	List<AutoMappedMap> selectPreview(@Param(Constant.PAGE_ID) String pageId,
			@Param(Constant.USERNAME) String username);

	List<AutoMappedMap> selectMemberInfo(Map<String, Object> params);

	List<Map<String, Object>> selectTmp(@Param(Constant.PAGE_ID) String pageId,
			@Param(Constant.USERNAME) String username);

}
