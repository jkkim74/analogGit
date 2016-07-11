package com.skplanet.bisportal.repository.bip;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.skplanet.bisportal.model.bip.FileMeta;

/**
 * Created by seoseungho on 2014. 9. 17.
 */
@Repository
public class FileMetaRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	public int addFileMeta(FileMeta fileMeta) {
		return sqlSession.insert("addFileMeta", fileMeta);
	}

	public List<FileMeta> getFileMetaLists(FileMeta params) {
		return sqlSession.selectList("getFileMetaLists", params);
	}

	public int deleteFile(List<Long> fileMetaIdList, String updateId) {
        Map<String, Object> parameterMap = Maps.newHashMap();
        parameterMap.put("fileMetaIdList", fileMetaIdList);
        parameterMap.put("updateId", updateId);
		return sqlSession.update("deleteFile", parameterMap);
	}

    public int deleteFileByContentId(long contentId, String updateId) {
        Map<String, Object> parameterMap = Maps.newHashMap();
        parameterMap.put("contentId", contentId);
        parameterMap.put("updateId", updateId);
        return sqlSession.update("deleteFileByContentId", parameterMap);
    }

}
