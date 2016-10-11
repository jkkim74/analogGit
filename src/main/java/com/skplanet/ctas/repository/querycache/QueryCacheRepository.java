package com.skplanet.ctas.repository.querycache;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.skplanet.pandora.model.AutoMappedMap;

@Repository("ctasQueryCacheRepository")
public interface QueryCacheRepository {

	List<AutoMappedMap> selectTargeting(Map<String, Object> params);

}
