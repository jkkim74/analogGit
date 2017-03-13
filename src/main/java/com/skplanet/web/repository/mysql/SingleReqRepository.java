package com.skplanet.web.repository.mysql;

import com.skplanet.web.model.SingleReq;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SingleReqRepository {

    List<SingleReq> selectSingleRequestProgress(SingleReq singleReq);

    int insertSingleRequestProgress(SingleReq singleReq);

    int selectSingleReqProcessingCnt(SingleReq singleReq);

}
