package com.skplanet.pandora.repository.querycache;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.skplanet.pandora.model.AutoMappedMap;

@Repository
public interface QueryCacheRepository {

	List<AutoMappedMap> selectTest(Map<String, Object> params);

}
