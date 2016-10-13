package com.skplanet.ctas.repository.querycache;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("ctasQueryCacheRepository")
public interface QueryCacheRepository {

	void createTargetingTable(Map<String, Object> params);

	int insertTargeting(Map<String, Object> params);

	int countTargeting(Map<String, Object> params);

}
