package com.skplanet.pandora.repository.mysql;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface MysqlRepository {

	Map<String, Object> selectTest(Map<String, Object> params);

}
