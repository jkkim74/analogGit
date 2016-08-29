package com.skplanet.pandora.repository.oracle;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.pandora.common.Constant;
import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.Member;
import com.skplanet.pandora.model.Preview;
import com.skplanet.pandora.model.UploadProgress;

@Repository
public interface OracleRepository {

	AutoMappedMap selectTest(Map<String, Object> params);

	int countTable(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username);

	int createTable(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username);

	void truncateTable(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username);

	int insertBulk(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username,
			@Param("bulkList") List<String> bulkList);

	List<Preview> selectPreview(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username);

	List<Member> selectMerged(UploadProgress uploadProgress);

}
