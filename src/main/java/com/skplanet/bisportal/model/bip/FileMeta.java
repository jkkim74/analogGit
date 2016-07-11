package com.skplanet.bisportal.model.bip;

import lombok.Data;

/**
 * Created by seoseungho on 2014. 9. 25..
 */
@Data
public class FileMeta {
    private long id;
    private String containerName;
    private long contentId;
    private String category;
    private String uuid;
    private String fileName;
    private int fileSize;
    private String createId;
    private String createDate;
    private String updateId;
    private String updateDate;
    private String deleteYn;
}
