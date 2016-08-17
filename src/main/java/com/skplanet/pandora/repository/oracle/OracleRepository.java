package com.skplanet.pandora.repository.oracle;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface OracleRepository {

	Map<String, Object> selectTest(Map<String, Object> params);

}
