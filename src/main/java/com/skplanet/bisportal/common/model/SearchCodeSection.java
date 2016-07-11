package com.skplanet.bisportal.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by sjune on 2014-05-28.
 *
 * @author sjune
 */
@Data
public class SearchCodeSection implements Serializable {
    private static final long serialVersionUID = -6750280086468579322L;
    private String code;
    private String name;

    public SearchCodeSection(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
