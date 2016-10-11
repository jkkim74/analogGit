package com.skplanet.ctas.repository.oracle;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.skplanet.pandora.model.AutoMappedMap;

@Repository("ctasOracleRepository")
public interface OracleRepository {

	List<AutoMappedMap> selectCampaigns(Map<String, Object> params);

	int countCampaigns(Map<String, Object> params);

}
