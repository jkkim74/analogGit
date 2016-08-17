package com.skplanet.pandora.repository.querycache;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface QueryCacheRepository {

	Map<String, Object> selectTest(Map<String, Object> params);

}
