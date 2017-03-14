package com.skplanet.web.repository.mysql;

import com.skplanet.web.model.SingleReq;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SingleReqRepository {

    List<SingleReq> selectSingleRequestProgress(SingleReq singleReq);

    int insertSingleRequestProgress(SingleReq singleReq);

    int selectSingleReqProcessingCnt(@Param("username")String username);

    void updateSingleRequestProgress(SingleReq singleReq);
}
