package com.skplanet.bisportal.model.bip;

import lombok.Data;

/**
 * Created by seoseungho on 2014. 10. 2..
 */
@Data
public class DssComment {

    // dssComment ID
    private long id;
    // dss ID
    private long dssId;
    // 댓글 내용
    private String comment;

    private String deleteYn;
    private String createId;
    private String createName;
    private String createDate;
    private String updateId;
    private String updateDate;

}
