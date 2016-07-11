package com.skplanet.bisportal.model.tcloud;

import com.skplanet.bisportal.common.model.JqGridRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by seoseungho on 2014. 12. 23..
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TCloudRequest extends JqGridRequest {
    private static final long serialVersionUID = 2200766797891377434L;
    private String commonGroupCode;
}
