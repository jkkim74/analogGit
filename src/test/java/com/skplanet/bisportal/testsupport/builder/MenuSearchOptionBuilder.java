package com.skplanet.bisportal.testsupport.builder;

import com.skplanet.bisportal.model.bip.MenuSearchOption;

/**
 * Created by sjune on 2014-08-29.
 *
 * @author sjune
 */
public class MenuSearchOptionBuilder {
    private Long menuId;
    private String label;
    private String dateTypes;
    private String codeUrl;
    private String addType;
    private String calendarType;
    private String lastUpdate;

    private MenuSearchOptionBuilder() {
    }

    public static MenuSearchOptionBuilder aMenuSearchOption() {
        return new MenuSearchOptionBuilder();
    }

    public MenuSearchOptionBuilder menuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public MenuSearchOptionBuilder label(String label) {
        this.label = label;
        return this;
    }

    public MenuSearchOptionBuilder dateTypes(String dateTypes) {
        this.dateTypes = dateTypes;
        return this;
    }

    public MenuSearchOptionBuilder codeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
        return this;
    }

    public MenuSearchOptionBuilder addType(String addType) {
        this.addType = addType;
        return this;
    }

    public MenuSearchOptionBuilder calendarType(String calendarType) {
        this.calendarType = calendarType;
        return this;
    }

    public MenuSearchOptionBuilder lastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public MenuSearchOption build() {
        MenuSearchOption menuSearchOption = new MenuSearchOption();
        menuSearchOption.setMenuId(menuId);
        menuSearchOption.setLabel(label);
        menuSearchOption.setDateTypes(dateTypes);
        menuSearchOption.setCodeUrl(codeUrl);
        menuSearchOption.setAddType(addType);
        menuSearchOption.setCalendarType(calendarType);
        menuSearchOption.setLastUpdate(lastUpdate);
        return menuSearchOption;
    }
}
