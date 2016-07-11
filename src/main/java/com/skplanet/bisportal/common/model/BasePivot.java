package com.skplanet.bisportal.common.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * grid pivot 을 위한 BaseModel
 *
 * @author sjune
 */
@Data
public class BasePivot implements Serializable {
    private static final long serialVersionUID = -1046213584235266678L;
    protected String actnCategory;
    protected String mainCategory;
    protected String subCategory;
    protected String measure;
    protected BigDecimal measureValue;
}
