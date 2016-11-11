package com.skplanet.web.repository.oracle;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.web.model.AutoMap;

@Repository
public interface UploadTempRepository {

	int countTable(@Param("menuId") String menuId, @Param("username") String username);

	void createTable(@Param("menuId") String menuId, @Param("username") String username);

	void truncateTable(@Param("menuId") String menuId, @Param("username") String username);

	List<AutoMap> selectUploaded(@Param("menuId") String menuId, @Param("username") String username);

	int countUploaded(@Param("menuId") String menuId, @Param("username") String username);

}
