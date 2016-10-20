package com.skplanet.ctas.repository.querycache;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.skplanet.ocb.util.AutoMappedMap;

@Repository("ctasQueryCacheRepository")
public interface QueryCacheRepository {

	void createTargetingTable(Map<String, Object> params);

	int insertTargeting(Map<String, Object> params);

	int countTargeting(Map<String, Object> params);

	List<AutoMappedMap> selectCell(Map<String, Object> params);

	List<String> selectTargeting(Map<String, Object> params);

}
