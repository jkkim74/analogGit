package com.skplanet.bisportal.testsupport.builder;

import com.skplanet.bisportal.model.bip.Menu;

/**
 * Created by sjune on 2014-08-20.
 *
 * @author sjune
 */
public class MenuBuilder {
    private Long id;
    private String code;
    private String name;
    private Long parentId;
    private Long commonCodeId;
    private String commonCodeName;
    private String imageUrl;
	private Long oprSvcId;
    private Integer orderIdx;
    private String visibleYn;
    private String deleteYn;
    private String menuSearchOptionYn;
    private String summaryReportYn;
    private String lastUpdate;
    private String auditId;

    private MenuBuilder() {
    }

    public static MenuBuilder aMenu() {
        return new MenuBuilder();
    }

    public MenuBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MenuBuilder code(String code) {
        this.code = code;
        return this;
    }

    public MenuBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MenuBuilder parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public MenuBuilder commonCodeId(Long commonCodeId) {
        this.commonCodeId = commonCodeId;
        return this;
    }

    public MenuBuilder commonCodeName(String commonCodeName) {
        this.commonCodeName = commonCodeName;
        return this;
    }

    public MenuBuilder imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public MenuBuilder orderIdx(Integer orderIdx) {
        this.orderIdx = orderIdx;
        return this;
    }

    public MenuBuilder oprSvcId(Long oprSvcId) {
        this.oprSvcId = oprSvcId;
        return this;
    }


    public MenuBuilder visibleYn(String visibleYn) {
        this.visibleYn = visibleYn;
        return this;
    }

    public MenuBuilder deleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
        return this;
    }

    public MenuBuilder menuSearchOptionYn(String menuSearchOptionYn) {
        this.menuSearchOptionYn = menuSearchOptionYn;
        return this;
    }

    public MenuBuilder summaryReportYn(String summaryReportYn) {
        this.summaryReportYn = summaryReportYn;
        return this;
    }

    public MenuBuilder lastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public MenuBuilder auditId(String auditId) {
        this.auditId = auditId;
        return this;
    }

    public Menu build() {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setCode(code);
        menu.setName(name);
        menu.setParentId(parentId);
        menu.setCommonCodeId(commonCodeId);
        menu.setCommonCodeName(commonCodeName);
        menu.setImageUrl(imageUrl);
        menu.setOprSvcId(oprSvcId);
        menu.setOrderIdx(orderIdx);
        menu.setVisibleYn(visibleYn);
        menu.setDeleteYn(deleteYn);
        menu.setMenuSearchOptionYn(menuSearchOptionYn);
        menu.setSummaryReportYn(summaryReportYn);
        menu.setLastUpdate(lastUpdate);
        menu.setAuditId(auditId);
        return menu;
    }
}
