package com.skplanet.pandora.repository.querycache;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface QueryCacheRepository {

	List<Map<String, Object>> selectTest(Map<String, Object> params);

}
