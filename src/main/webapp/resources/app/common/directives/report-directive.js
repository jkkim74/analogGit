var directives = angular.module('app.commonDirectives');

/**
 * directives > reportSubNav
 */
directives.lazy.directive('reportSubNav', function ($log) {
    return {
        restrict: 'E',
        templateUrl: "page/templates/reportSubNav.tpl.html?_=" + DateHelper.timestamp(),
        scope: false, /* TODO refactoring to isolation scope */
        link: function (scope) {
            if (!scope.excelConfig) {
                alert('Cannot find excelConfig in scope!');
                return;
            }

            /**
             * 엑셀 다운로드
             */
            scope.excelDownload = function () {
                var $reportExcelForm = $('#reportExcelForm');
                $reportExcelForm.attr('action', scope.excelConfig.downloadUrl);
                $reportExcelForm.find('input[name="dateType"]').val(scope.searchDateType);
                $reportExcelForm.find('input[name="startDate"]').val(scope.searchStartDate);
                $reportExcelForm.find('input[name="endDate"]').val(scope.searchEndDate);
                $reportExcelForm.find('input[name="xlsName"]').val(scope.excelConfig.xlsName);
                if (!angular.isUndefined(scope.searchPocCode))
                    $reportExcelForm.find('input[name="pocCode"]').val(scope.searchPocCode);
                if (!angular.isUndefined(scope.searchKpiCode))
                    $reportExcelForm.find('input[name="kpiCode"]').val(scope.searchKpiCode);
                if (!angular.isUndefined(scope.searchString))
                    $reportExcelForm.find('input[name="searchString"]').val(scope.searchString);
                if (scope.excelConfig.pivotFlag === 'Y') {
                    $reportExcelForm.find('input[name="titleName"]').val(scope.excelConfig.titleName);
                    var htmlData = $('#' + scope.excelConfig.tableId).html();
                    try {
                        htmlData = encodeURIComponent(htmlData);
                        $reportExcelForm.find('input[name="htmlData"]').val(htmlData);
                    } catch (err) {
                        $log.debug(err.message);
                    }
                }
                if (!angular.isUndefined(scope.feedType))
                    $reportExcelForm.find('input[name="feedType"]').val(scope.feedType);
                $reportExcelForm.submit();
            };
        }
    };
});

/**
 * commonDirectives > summarySubNav
 */
directives.lazy.directive('summarySubNav', function (summaryReportSvc) {
    return {
        restrict: 'E',
        templateUrl: "page/templates/summarySubNav.tpl.html?_=" + DateHelper.timestamp(),
        scope: false, /* TODO refactoring to isolation scope */
        link: function (scope) {
            /**
             * 엑셀 다운로드
             */
            scope.excelDownload = function () {
                var summaryJsons = summaryReportSvc.getSummaryExcel(scope.svcId);
                var $summaryExcelForm = $('#excelForm');
                $summaryExcelForm.find('input[name="svcId"]').val(scope.svcId);
                $summaryExcelForm.find('input[name="dateType"]').val(scope.dateType);
                $summaryExcelForm.find('input[name="basicDate"]').val(scope.basicDateParamString);
                $summaryExcelForm.find('input[name="titleName"]').val(summaryJsons.titleName);
                $summaryExcelForm.find('input[name="xlsName"]').val(summaryJsons.xlsName);
                $summaryExcelForm.find('input[name="whereConditions"]').val(encodeURIComponent(JSON.stringify(scope.measures)));
                $summaryExcelForm.submit();

            };
        }
    };
});

/**
 * directives > reportSubNav
 */
directives.lazy.directive('bossReportSubNav', function ($log) {
    return {
        restrict: 'E',
        templateUrl: "page/templates/summarySubNav.tpl.html?_=" + DateHelper.timestamp(),
        scope: false, /* TODO refactoring to isolation scope */
        link: function (scope) {
            if (!scope.excelConfig) {
                alert('Cannot find excelConfig in scope!');
                return;
            }
            /**
             * 엑셀 다운로드
             */
            scope.excelDownload = function () {
                var $bossExcelForm = $('#excelForm');
                $bossExcelForm.attr('action', scope.excelConfig.downloadUrl);
                $bossExcelForm.find('input[name="dateType"]').val(scope.searchDateType);
                $bossExcelForm.find('input[name="xlsName"]').val(scope.excelConfig.xlsName);
                $bossExcelForm.find('input[name="titleName"]').val(scope.excelConfig.titleName);
                var htmlData = $('#' + scope.excelConfig.tableId + ' .pvtRendererArea').html();
                try {
                    htmlData = encodeURIComponent(htmlData);
                    $bossExcelForm.find('input[name="htmlData"]').val(htmlData);
                } catch (err) {
                    $log.debug(err.message);
                }
                $bossExcelForm.submit();

            };
        }
    };
});

/**
 * directives > bleSubNav
 */
directives.lazy.directive('bleSubNav', function () {
    return {
        restrict: 'E',
        templateUrl: "page/templates/bleSubNav.tpl.html?_=" + DateHelper.timestamp(),
        scope: false, /* TODO refactoring to isolation scope */
        link: function (scope) {
            if (!scope.excelConfig) {
                alert('Cannot find excelConfig in scope!');
                return;
            }
            /**
             * 엑셀 다운로드
             */
            scope.excelDownload = function () {
                var $bleExcelForm = $('#bleExcelForm');
                $bleExcelForm.attr('action', scope.excelConfig.downloadUrl);
                $bleExcelForm.find('input[name="dateType"]').val(scope.searchDateType);
                $bleExcelForm.find('input[name="startDate"]').val(scope.searchStartDate);
                $bleExcelForm.find('input[name="endDate"]').val(scope.searchEndDate);
                $bleExcelForm.find('input[name="xlsName"]').val(scope.excelConfig.xlsName);
                //$bleExcelForm.find('input[name="measureCode"]').val(scope.searchMeasureType);
                $bleExcelForm.find('input[name="itemCode"]').val(scope.searchCode);
                $bleExcelForm.find('input[name="serviceTypeCode"]').val(scope.searchServiceType);
                $bleExcelForm.find('input[name="statContents"]').val(scope.searchStatContent.join(','));
                if (!angular.isUndefined(scope.searchString))
                    $bleExcelForm.find('input[name="searchString"]').val(scope.searchString);
                $bleExcelForm.submit();
            };
        }
    };
});

/**
 * dashboardDirectives > dashboardBmSubNav
 */
directives.lazy.directive('dashboardBmSubNav', function () {
    return {
        restrict: 'E',
        templateUrl: "page/templates/dashboardBmSubNav.tpl.html?_=" + DateHelper.timestamp(),
        scope: false, /* TODO refactoring to isolation scope */
        link: function (scope) {
            if (!scope.excelConfig) {
                alert('Cannot find excelConfig in scope!');
                return;
            }
            /**
             * 엑셀 다운로드
             */
            scope.excelDownload = function () {
                var $reportExcelForm = $('#excelForm');
                $reportExcelForm.attr('action', scope.excelConfig.downloadUrl);
                $reportExcelForm.find('input[name="svcId"]').val(scope.svcId);
                $reportExcelForm.find('input[name="dateType"]').val(scope.dateType);
                $reportExcelForm.find('input[name="basicDate"]').val(scope.basicDateParamString);
                $reportExcelForm.find('input[name="titleName"]').val(scope.excelConfig.titleName);
                $reportExcelForm.find('input[name="xlsName"]').val(scope.excelConfig.xlsName);
                $reportExcelForm.find('input[name="whereConditions"]').val(encodeURIComponent(JSON.stringify(scope.measures)));
                $reportExcelForm.submit();
            };
        }
    };
});

/**
 * directives > reportSearchBox
 */
directives.lazy.directive('reportSearchBox', function (reportSvc, REPORT_DATE_TYPES, DATE_TYPE_MONTH, $timeout, $http) {
    var activeMenuItem;
    //var searchConfig;
    var SEARCH_BTN = '#_searchBtn';

    function bindEvents(element) {
        EventBindingHelper.initDatePicker(element, null);
    }

    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            var searchDateType = scope.selectedDateType;
            if (!DateHelper.isValid(scope.selectedCalStart, 'YYYY.MM.DD')) {
                alert('조회 시작일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchStartDate = DateHelper.dateToParamString(scope.selectedCalStart);
            if (!DateHelper.isValid(scope.selectedCalEnd, 'YYYY.MM.DD')) {
                alert('조회 종료일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchEndDate = DateHelper.dateToParamString(scope.selectedCalEnd);

            if (searchDateType == DATE_TYPE_MONTH) {
                searchStartDate = searchStartDate.substring(0, 6);
                searchEndDate = searchEndDate.substring(0, 6);
            }
            scope.$apply(function () {
                var result = {
                    'searchDateType': searchDateType
                };
                if (scope.searchedSelectCode) {
                    result.searchCode = scope.searchedSelectCode.code;
                }

                if (scope.searchedText) {
                    result.searchString = $.trim(scope.searchedText);
                }
                result.searchStartDate = searchStartDate;
                result.searchEndDate = searchEndDate;
                scope.searchCallback(result);
            });
        });
    }

    /**
     * 날짜 타입 세팅
     * @param menuItem
     * @returns {*}
     */
    function getFilteredSearchDateTypes() {
        // 메뉴검색옵션을 사용하지 않거나 dateTypes이 명시되지 않았으면 기본 값을 return 한다.
        if (activeMenuItem.menuSearchOptionYn != 'Y'
            || activeMenuItem.menuSearchOption.dateTypes == null) {
            return REPORT_DATE_TYPES;
        }
        var filteredDateTypes = activeMenuItem.menuSearchOption.dateTypes;
        var resultDateTypes = [];
        angular.forEach(REPORT_DATE_TYPES, function (obj) {
            if (filteredDateTypes.indexOf(obj.key) > -1) {
                resultDateTypes.push(obj);
            }
        });
        return resultDateTypes;
    }

    /**
     * 검색 영역을 세팅한다.
     * @param scope
     */
    function initSearchSection(scope) {
        if (activeMenuItem.menuSearchOptionYn != 'Y') return;

        scope.menuSearchOption = activeMenuItem.menuSearchOption;
        if (scope.menuSearchOption.addType == 'select') {
            // select 검색 영역을 만든다.
            $http.get(scope.menuSearchOption.codeUrl).success(function (result) {
                scope.menuSearchOption.data = result;
                scope.searchedSelectCode = scope.menuSearchOption.data[0];
            });

        } else if (scope.menuSearchOption.addType == 'text') {
            // text 검색 영역을 만든다.
            scope.searchedText = null;
        }
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/reportSearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            searchConfig: '=',
            menuContext: '=',
            searchCallback: '='
        },
        link: function (scope, element) {
            scope.$on('$viewContentLoaded', bindEvents(element));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });

            activeMenuItem = scope.menuContext.menu;
            // 날짜 정보를 세팅한다.
            scope.dateTypes = getFilteredSearchDateTypes();
            scope.selectedDateType = scope.dateTypes[0].key;
            scope.selectedCalStart = reportSvc.defaultCal(scope.selectedDateType).startDateStr;
            scope.selectedCalEnd = reportSvc.defaultCal(scope.selectedDateType).endDateStr;

            // 검색 영역을 세팅한다.
            initSearchSection(scope);

            /**
             * 날짜 타입 변경
             * @param dateType
             */
            scope.changeDateType = function (dateType) {
                var defaultCal = reportSvc.defaultCal(dateType.key);
                scope.selectedCalStart = defaultCal.startDateStr;
                scope.selectedCalEnd = defaultCal.endDateStr;
                scope.selectedDateType = dateType.key;
            };

            // 리포트 조회
            bindSearchButton(scope, element);
        }
    };
});

/**
 * directives > syrupReportSearchBox
 */
directives.lazy.directive('syrupReportSearchBox', function (reportSvc, REPORT_DATE_TYPES, DATE_TYPE_MONTH, $timeout, $http) {
    var activeMenuItem;
    var searchConfig;
    var SEARCH_BTN = '#_searchBtn';

    function bindEvents(element) {
        EventBindingHelper.initDatePicker(element, null);
    }

    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            var searchDateType = scope.selectedDateType;
            if (!DateHelper.isValid(scope.selectedCalStart, 'YYYY.MM.DD')) {
                alert('조회 시작일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchStartDate = DateHelper.dateToParamString(scope.selectedCalStart);
            if (!DateHelper.isValid(scope.selectedCalEnd, 'YYYY.MM.DD')) {
                alert('조회 종료일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchEndDate = DateHelper.dateToParamString(scope.selectedCalEnd);

            if (searchDateType == DATE_TYPE_MONTH) {
                searchStartDate = searchStartDate.substring(0, 6);
                searchEndDate = searchEndDate.substring(0, 6);
            }

            scope.$apply(function () {
                var result = {
                    'searchDateType': searchDateType
                };
                if (scope.searchedSelectCode) {
                    result.searchCode = scope.searchedSelectCode.code;
                }
                if (scope.searchedText) {
                    result.searchString = $.trim(scope.searchedText);
                }
                result.searchStartDate = searchStartDate;
                result.searchEndDate = searchEndDate;
                scope.searchCallback(result);
            });
        });

    }

    /**
     * 날짜 타입 세팅
     * @param menuItem
     * @returns {*}
     */
    function getFilteredSearchDateTypes() {
        // 메뉴검색옵션을 사용하지 않거나 dateTypes이 명시되지 않았으면 기본 값을 return 한다.
        if (activeMenuItem.menuSearchOptionYn != 'Y'
            || activeMenuItem.menuSearchOption.dateTypes == null) {
            return REPORT_DATE_TYPES;
        }
        var filteredDateTypes = activeMenuItem.menuSearchOption.dateTypes;
        var resultDateTypes = [];
        angular.forEach(REPORT_DATE_TYPES, function (obj) {
            if (filteredDateTypes.indexOf(obj.key) > -1) {
                resultDateTypes.push(obj);
            }
        });
        return resultDateTypes;
    }

    /**
     * 검색 영역을 세팅한다.
     * @param scope
     */
    function initSearchSection(scope) {
        if (activeMenuItem.menuSearchOptionYn != 'Y')
            return;
        scope.menuSearchOption = activeMenuItem.menuSearchOption;
        if (scope.menuSearchOption.addType == 'select') {
            // select 검색 영역을 만든다.
            $http.get(scope.menuSearchOption.codeUrl).success(function (result) {
                scope.menuSearchOption.data = result;
                scope.searchedSelectCode = scope.menuSearchOption.data[0];
            });

        } else if (scope.menuSearchOption.addType == 'text') {
            // text 검색 영역을 만든다.
            scope.searchedText = null;
        }
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/syrupReportSearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            searchConfig: '=',
            menuContext: '=',
            searchCallback: '='
        },
        link: function (scope, element) {
            scope.$on('$viewContentLoaded', bindEvents(element));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });

            activeMenuItem = scope.menuContext.menu;
            searchConfig = scope.searchConfig;

            // 날짜 정보를 세팅한다.
            scope.dateTypes = getFilteredSearchDateTypes();
            scope.selectedDateType = scope.dateTypes[0].key;
            scope.selectedCalStart = reportSvc.defaultCustomCal(scope.selectedDateType, searchConfig.dayPeriod, searchConfig.weekPeriod, searchConfig.monthPeriod).startDateStr;
            scope.selectedCalEnd = reportSvc.defaultCustomCal(scope.selectedDateType, searchConfig.dayPeriod, searchConfig.weekPeriod, searchConfig.monthPeriod).endDateStr;

            // 검색 영역을 세팅한다.
            initSearchSection(scope);

            /**
             * 날짜 타입 변경
             * @param dateType
             */
            scope.changeDateType = function (dateType) {
                var defaultCal = reportSvc.defaultCustomCal(dateType.key, searchConfig.dayPeriod, searchConfig.weekPeriod, searchConfig.monthPeriod);
                scope.selectedCalStart = defaultCal.startDateStr;
                scope.selectedCalEnd = defaultCal.endDateStr;
                scope.selectedDateType = dateType.key;
            };

            // 리포트 조회
            bindSearchButton(scope, element);
        }
    };
});

/**
 * directives > reportSearchBox
 */
directives.lazy.directive('reportOlapSearchBox', function (reportSvc, REPORT_DATE_TYPES, DATE_TYPE_MONTH, $timeout, $http) {
    var activeMenuItem;
    var searchConfig;
    var SEARCH_BTN = '#_searchBtn';

    function bindEvents(element) {
        EventBindingHelper.initDatePicker(element, null);
    }

    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            var searchDateType = scope.selectedDateType;
            if (!DateHelper.isValid(scope.selectedCalStart, 'YYYY.MM.DD')) {
                alert('조회 시작일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchStartDate = DateHelper.dateToParamString(scope.selectedCalStart);
            if (!DateHelper.isValid(scope.selectedCalEnd, 'YYYY.MM.DD')) {
                alert('조회 종료일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchEndDate = DateHelper.dateToParamString(scope.selectedCalEnd);

            if (searchDateType == DATE_TYPE_MONTH) {
                searchStartDate = searchStartDate.substring(0, 6);
                searchEndDate = searchEndDate.substring(0, 6);
            }

            scope.$apply(function () {
                var result = {
                    'searchDateType': searchDateType
                };

                if (scope.searchedSelectCode) {
                    result.searchCode = scope.searchedSelectCode.code;
                }

                if (scope.searchedText) {
                    result.searchString = $.trim(scope.searchedText);
                }

                result.searchStartDate = searchStartDate;
                result.searchEndDate = searchEndDate;

                scope.searchCallback(result);
            });
        });

    }

    /**
     * 날짜 타입 세팅
     * @param menuItem
     * @returns {*}
     */
    function getFilteredSearchDateTypes() {
        // 메뉴검색옵션을 사용하지 않거나 dateTypes이 명시되지 않았으면 기본 값을 return 한다.
        if (activeMenuItem.menuSearchOptionYn != 'Y'
            || activeMenuItem.menuSearchOption.dateTypes == null) {
            return REPORT_DATE_TYPES;
        }

        var filteredDateTypes = activeMenuItem.menuSearchOption.dateTypes;
        var resultDateTypes = [];
        angular.forEach(REPORT_DATE_TYPES, function (obj) {
            if (filteredDateTypes.indexOf(obj.key) > -1) {
                resultDateTypes.push(obj);
            }
        });
        return resultDateTypes;
    }

    /**
     * 검색 영역을 세팅한다.
     * @param scope
     */
    function initSearchSection(scope) {
        if (activeMenuItem.menuSearchOptionYn != 'Y') return;

        scope.menuSearchOption = activeMenuItem.menuSearchOption;
        if (scope.menuSearchOption.addType == 'select') {
            // select 검색 영역을 만든다.
            $http.get(scope.menuSearchOption.codeUrl).success(function (result) {
                scope.menuSearchOption.data = result;
                scope.searchedSelectCode = scope.menuSearchOption.data[0];
            });
        } else if (scope.menuSearchOption.addType == 'text') {
            // text 검색 영역을 만든다.
            scope.searchedText = null;
        }
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/reportOlapSearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            searchConfig: '=',
            menuContext: '=',
            searchCallback: '=',
            measures: '=',
            dimensions: '='
        },
        link: function (scope, element) {
            scope.$on('$viewContentLoaded', bindEvents(element));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });

            activeMenuItem = scope.menuContext.menu;
            searchConfig = scope.searchConfig;

            // 날짜 정보를 세팅한다.
            scope.dateTypes = getFilteredSearchDateTypes();
            scope.selectedDateType = scope.dateTypes[0].key;
            scope.selectedCalStart = reportSvc.defaultCal(scope.selectedDateType).startDateStr;
            scope.selectedCalEnd = reportSvc.defaultCal(scope.selectedDateType).endDateStr;

            // 검색 영역을 세팅한다.
            initSearchSection(scope);

            /**
             * 날짜 타입 변경
             * @param dateType
             */
            scope.changeDateType = function (dateType) {
                var defaultCal = reportSvc.defaultCal(dateType.key);
                scope.selectedCalStart = defaultCal.startDateStr;
                scope.selectedCalEnd = defaultCal.endDateStr;
                scope.selectedDateType = dateType.key;
            };

            // 리포트 조회
            bindSearchButton(scope, element);
        }
    };
});

/**
 * directives > reportSearchBox
 */
directives.lazy.directive('reportOlapSearchBoxWithDrag', function (reportSvc, REPORT_DATE_TYPES, DATE_TYPE_MONTH, $timeout, $http) {
    var activeMenuItem;
    var searchConfig;
    var SEARCH_BTN = '#_searchBtn';

    function bindEvents(element, searchConfig, draggable, metrics) {
        EventBindingHelper.initDatePicker(element, null);
        dragAreaInit(searchConfig, draggable, metrics);
    }

    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            var searchDateType = scope.selectedDateType;
            if (!DateHelper.isValid(scope.selectedCalStart, 'YYYY.MM.DD')) {
                alert('조회 시작일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchStartDate = DateHelper.dateToParamString(scope.selectedCalStart);
            if (!DateHelper.isValid(scope.selectedCalEnd, 'YYYY.MM.DD')) {
                alert('조회 종료일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchEndDate = DateHelper.dateToParamString(scope.selectedCalEnd);

            if (searchDateType == DATE_TYPE_MONTH) {
                searchStartDate = searchStartDate.substring(0, 6);
                searchEndDate = searchEndDate.substring(0, 6);
            }

            scope.$apply(function () {
                var result = {
                    'searchDateType': searchDateType
                };

                if (scope.searchedSelectCode) {
                    result.searchCode = scope.searchedSelectCode.code;
                }

                if (scope.searchedText) {
                    result.searchString = $.trim(scope.searchedText);
                }

                result.searchStartDate = searchStartDate;
                result.searchEndDate = searchEndDate;

                scope.searchCallback(result);
            });
        });

    }

    /**
     * 날짜 타입 세팅
     * @param menuItem
     * @returns {*}
     */
    function getFilteredSearchDateTypes() {
        // 메뉴검색옵션을 사용하지 않거나 dateTypes이 명시되지 않았으면 기본 값을 return 한다.
        if (activeMenuItem.menuSearchOptionYn != 'Y'
            || activeMenuItem.menuSearchOption.dateTypes == null) {
            return REPORT_DATE_TYPES;
        }

        var filteredDateTypes = activeMenuItem.menuSearchOption.dateTypes;
        var resultDateTypes = [];
        angular.forEach(REPORT_DATE_TYPES, function (obj) {
            if (filteredDateTypes.indexOf(obj.key) > -1) {
                resultDateTypes.push(obj);
            }
        });
        return resultDateTypes;
    }

    /**
     * 검색 영역을 세팅한다.
     * @param scope
     */
    function initSearchSection(scope) {
        if (activeMenuItem.menuSearchOptionYn != 'Y') return;

        scope.menuSearchOption = activeMenuItem.menuSearchOption;
        if (scope.menuSearchOption.addType == 'select') {
            // select 검색 영역을 만든다.
            $http.get(scope.menuSearchOption.codeUrl).success(function (result) {
                scope.menuSearchOption.data = result;
                scope.searchedSelectCode = scope.menuSearchOption.data[0];
            });
        } else if (scope.menuSearchOption.addType == 'text') {
            // text 검색 영역을 만든다.
            scope.searchedText = null;
        }
    }


    /**
     * olap drag 영역 초기화 refectoring
     * @param searchConfig
     * @param draggable
     * @param metrics
     */
    function dragAreaInit(searchConfig, draggable, metrics) {
        var $dragArea = $('div[ng-controller="' + searchConfig.ctrlId + '"] #y-sortable');
        $dragArea.sortable({
            'tolerance': 'pointer',
            'placeholder': 'ui-state-highlight',
            'update': function (event) {
                var $curDradArea = $('div[ng-controller="' + searchConfig.ctrlId + '"] #y-sortable');
                _.each($(event.target).children(), function (li, index) {
                    if (li.textContent.trim().toLowerCase() === 'measure') {
                        if (index !== 0 && index !== draggable.length - 1) {
                            alert('measure는 처음과 끝에만 위치 할 수 있습니다.');
                            $curDradArea.sortable('cancel');
                            return;
                        }
                    }
                    _.extend(_.findWhere(metrics, {'code': $(li).data('code')}), {'order': index});
                });
            }
        }).disableSelection();
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/reportOlapSearchBoxWithDrag.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            searchConfig: '=',
            menuContext: '=',
            searchCallback: '=',
            measure: '=',
            dimensions: '=',
            draggable: '='
        },
        link: function (scope, element) {
            var metrics = scope.dimensions.concat(scope.measure);
            scope.$on('$viewContentLoaded', bindEvents(element, scope.searchConfig, scope.draggable, metrics));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });

            activeMenuItem = scope.menuContext.menu;
            searchConfig = scope.searchConfig;

            // 날짜 정보를 세팅한다.
            scope.dateTypes = getFilteredSearchDateTypes();
            scope.selectedDateType = scope.dateTypes[0].key;
            scope.selectedCalStart = reportSvc.defaultCustomCal(scope.selectedDateType, searchConfig.dayPeriod, searchConfig.weekPeriod, searchConfig.monthPeriod).startDateStr;
            scope.selectedCalEnd = reportSvc.defaultCustomCal(scope.selectedDateType, searchConfig.dayPeriod, searchConfig.weekPeriod, searchConfig.monthPeriod).endDateStr;

            // 검색 영역을 세팅한다.
            initSearchSection(scope);

            /**
             * 날짜 타입 변경
             * @param dateType
             */
            scope.changeDateType = function (dateType) {
                var defaultCal = reportSvc.defaultCustomCal(dateType.key,searchConfig.dayPeriod, searchConfig.weekPeriod, searchConfig.monthPeriod);
                scope.selectedCalStart = defaultCal.startDateStr;
                scope.selectedCalEnd = defaultCal.endDateStr;
                scope.selectedDateType = dateType.key;
            };

            // 리포트 조회
            bindSearchButton(scope, element);

            // dimension에만 동작, 사용여부를 토글한다.
            scope.toggleUsage = function ($event) {
                var $li;
                $li = angular.element($event.target);
                var selectedCode = _.findWhere(scope.dimensions, {'code': $li.data('code')});
                if (selectedCode) {
                    if (selectedCode.selected) {
                        selectedCode.selected = false;
                    } else {
                        selectedCode.selected = true;
                    }
                }
            };
        }
    };
});

/**
 * directives > reportIndicator
 */
directives.lazy.directive('reportIndicator', function ($timeout) {
    var MSG_LOADING = '데이터를 조회 중입니다.';
    var MSG_EMPTY_DATA = '조회 조건에 맞는 데이터가 존재하지 않습니다.';

    function showMessageArea($dataArea, $element, message) {
        // element 를 가린다.
        $element.css({
            'left': -99999,
            'position': 'absolute'
        }).empty();
        // 메시지 영역을 모여준다.
        $timeout(function () {
            $dataArea.addClass('data-condition');
            $dataArea.find('.displayArea:visible').text(message);
        }, 1);
    }

    function showDataArea($dataArea, $element) {
        $dataArea.hide();
        // element 를 보여준다.
        $element.css({
            'left': 0,
            'position': ''
        });

        // 데이터 영역을 비운다.
        // grid 렌더링 시간이 짧으면 로딩 기능 제거가 잘안된다(렌더링전에 삭제가 호출되어서). 로딩 삭제 후 data-area 보여주게 처리함.
        $timeout(function () {
            $dataArea.show();
            $dataArea.removeClass('data-condition');
            $dataArea.find('.displayArea:visible').empty();
        }, 1);
    }

    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var $dataArea = angular.element(element).closest('div.data-area:visible');
            $dataArea.append('<p class="displayArea"></p>');
            scope.$watch(attrs.reportIndicator, function (newValue) {
                if (newValue.loading) {
                    showMessageArea($dataArea, $(element), MSG_LOADING);
                } else {
                    showDataArea($dataArea, $(element));
                    if (newValue.emptyData) {
                        showMessageArea($dataArea, $(element), MSG_EMPTY_DATA);
                    }
                }
            }, true);
        }
    };
});

/**
 * directives > summaryReportSearchBox
 */
directives.lazy.directive('summaryReportSearchBox', function ($compile, $timeout, REPORT_DATE_TYPES, summaryReportSvc, $templateCache) {
    var SEARCH_BTN = '#_summaryReportSearchBtn';

    function bindEvents(scope, element) {
        // datepicker 바인딩
        EventBindingHelper.initDatePicker(element, null);

        // popover compile & binding
        var popoverHtml = $compile($templateCache.get('popoverContents.tpl.html'))(scope);
        EventBindingHelper.initSummaryReportMeasurePopup(element, popoverHtml, function () {
            // 지표 팝업을 닫는다.
            scope.addedMeasures = [];
            $timeout(function () {
                angular.element('.popover-content input[type="checkbox"]')
                    .prop('checked', false);
            }, 0);
        });
    }

    /**
     * 조회버튼 binding
     * @param scope
     * @param element
     */
    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            if (!DateHelper.isValid(scope.selectedDate, 'YYYY.MM.DD')) {
                alert('조회 일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            scope.$apply(function () {
                var dateType = scope.selectedDatePeriodType.key;
                var selectedDate = DateHelper.stringToDate(scope.selectedDate);
                var diffMs = summaryReportSvc.getPreviousDiffMs(dateType, selectedDate);
                var startDate = new Date(diffMs);
                var result = {
                    'searchDateType': dateType,
                    'searchStartDate': DateHelper.dateToParamString(startDate),
                    'searchEndDate': DateHelper.dateToParamString(scope.selectedDate),
                    'searchMeasures': (scope.addedMeasures.length) ? scope.addedMeasures : scope.measures //선택된 지표가 없으면 전체를 보낸다.
                };
                scope.searchCallback(result);
            });
        });
    }

    /**
     * 지표 스택을 생성한다. 8개씩 3등분 한다.
     * @param measures
     * @returns {Array}
     */
    function setMeasureStack(measures) {
        var stackSize = 8;
        var divSize = 3;
        var measureStack = [];
        for (var i = 0; i < divSize; i++) {
            measureStack.push(measures.slice(stackSize * i, (stackSize * (i + 1))));
        }
        return measureStack;
    }

    /**
     * 지표 팝업을 적용한다.
     */
    function applyMeasures() {
        angular.element('.indicator-select').popover('hide');
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/summaryReportSearchBox.html?_=" + DateHelper.timestamp(),
        scope: {
            searchCallback: '=',
            initCallback: '=',
            svcId: '=',
            basicDate: '='
        },
        link: function (scope, element) {
            scope.$on('$viewContentLoaded', bindEvents(scope, element));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });

            scope.datePeriodTypes = REPORT_DATE_TYPES;
            scope.selectedDatePeriodType = REPORT_DATE_TYPES[0];
            scope.selectedDate = scope.basicDate;
            // measure 정보를 구한뒤 initCallback을 한다.
            summaryReportSvc.getMeasures(scope.svcId).then(function (result) {
                scope.measures = result.data;
                scope.measureStack = setMeasureStack(scope.measures);
                scope.initCallback({'measures': scope.measures});
            });

            /**
             * 지표 선택
             * @type {Array}
             */
            scope.addedMeasures = [];
            scope.selectMeasure = function (measure) {
                var index = scope.addedMeasures.indexOf(measure);
                if (index == -1) {
                    measure.checked = true;
                    scope.addedMeasures.push(measure);
                } else {
                    measure.checked = false;
                    scope.addedMeasures.splice(index, 1);
                }
            };

            /**
             * 날짜 타입 변경
             * @param dateType
             */
            scope.changeDateType = function (dateType) {
                scope.selectedDate = summaryReportSvc.defaultDateStr(dateType.key);
                scope.selectedDatePeriodType = dateType;
            };
            /**
             * 지표 전체 선택
             */
            scope.selectAllMeasures = function () {
                scope.addedMeasures = [];
                for (var i = 0, size = scope.measureStack.length; i < size; i++) {
                    angular.forEach(scope.measureStack[i], function (measure) {
                        measure.checked = true;
                        scope.addedMeasures.push(measure);
                    });
                }
            };
            /**
             * 지표 clear
             */
            scope.clearMeasures = function () {
                scope.addedMeasures = [];
                for (var i = 0, size = scope.measureStack.length; i < size; i++) {
                    angular.forEach(scope.measureStack[i], function (measure) {
                        measure.checked = false;
                    });
                }
            };
            /**
             * 지표 적용
             */
            scope.applyMeasures = function () {
                applyMeasures();
            };
            // 리포트 조회
            bindSearchButton(scope, element);
        }
    };
});

/**
 * directives > bleSearchBox
 */
directives.lazy.directive('bleSearchBox', function (reportSvc, BLE_DATE_TYPES, DATE_TYPE_MONTH) {
    var searchConfig;
    var SEARCH_BTN = '#_bleSearchBtn';

    function bindEvents(element) {
        EventBindingHelper.initDatePicker(element, null);
    }

    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            var searchDateType = scope.selectedDateType;
            if (!DateHelper.isValid(scope.selectedCalStart, 'YYYY.MM.DD')) {
                alert('조회 시작일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchStartDate = DateHelper.dateToParamString(scope.selectedCalStart);
            if (!DateHelper.isValid(scope.selectedCalEnd, 'YYYY.MM.DD')) {
                alert('조회 종료일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchEndDate = DateHelper.dateToParamString(scope.selectedCalEnd);

            if (searchDateType == DATE_TYPE_MONTH) {
                searchStartDate = searchStartDate.substring(0, 6);
                searchEndDate = searchEndDate.substring(0, 6);
            }
            scope.$apply(function () {
                var result = {
                    'searchDateType': searchDateType
                };
                if (scope.searchedSelectCode) {
                    result.searchCode = scope.searchedSelectCode.code;
                }
                if (scope.searchedText) {
                    result.searchString = $.trim(scope.searchedText);
                }
                if (scope.selectedServiceType) {
                    result.searchServiceType = searchConfig.serviceTypes[scope.selectedServiceType.index];
                }
                if (scope.selectedMeasureType) {
                    result.searchMeasureType = searchConfig.measureTypes[scope.selectedMeasureType.index];
                }
                if (scope.selectedStatContent) {
                    var statFileds = [];
                    _.map(scope.selectedStatContent.codes, function (value, key) {
                        if (value)
                            statFileds.push(key);
                    });
                    if (!statFileds.length) {
                        alert(searchConfig.statContentName + "을 선택해 주세요.");
                        return;
                    }
                    result.searchStatContent = statFileds;
                }
                if (scope.selectedNewTech) {
                    var techFileds = [];
                    _.map(scope.selectedNewTech.codes, function (value, key) {
                        if (value)
                            techFileds.push(key);
                    });
                    if (!techFileds.length) {
                        alert(searchConfig.newTechName + "을 선택해 주세요.");
                        return;
                    }
                    result.searchNewTech = techFileds;
                }
                if (scope.selectedVersionType) {
                    var versionFileds = [];
                    _.map(scope.selectedVersionType.codes, function (value, key) {
                        if (value)
                            versionFileds.push(key);
                    });
                    if (!versionFileds.length) {
                        alert(searchConfig.versionTypeName + "을 선택해 주세요.");
                        return;
                    }
                    result.searchVersionType = versionFileds;
                }
                if (scope.selectedConditionType) {
                    var conditionFileds = [];
                    _.map(scope.selectedConditionType.codes, function (value, key) {
                        if (value)
                            conditionFileds.push(key);
                    });
                    result.searchConditionType = conditionFileds;
                }
                if (scope.selectedErrorType) {
                    var errorFileds = [];
                    _.map(scope.selectedErrorType.codes, function (value, key) {
                        if (value)
                            errorFileds.push(key);
                    });
                    result.searchErrorType = errorFileds;
                }
                result.searchStartDate = searchStartDate;
                result.searchEndDate = searchEndDate;

                scope.searchCallback(result);
            });
        });

    }

    /**
     * 검색 영역을 세팅한다.
     * @param scope
     */
    function initSearchSection(scope) {
        if (searchConfig.searchItems)
            scope.searchedSelectCode = searchConfig.searchItems[0];
        if (searchConfig.serviceTypes)
            scope.selectedServiceType = {index: 0};
        if (searchConfig.measureTypes)
            scope.selectedMeasureType = {index: 0};
        if (searchConfig.statContents) {
            scope.selectedStatContent = {
                codes: {"1": true, "2": true}
            };
        }
        if (searchConfig.newTechs) {
            scope.selectedNewTech = {
                codes: {"1": true}
            };
        }
        if (searchConfig.versionTypes) {
            scope.selectedVersionType = {
                codes: {"1": true}
            };
        }
        if (searchConfig.conditionTypes) {
            scope.selectedConditionType = {
                codes: {"1": false}
            };
        }
        if (searchConfig.errorTypes) {
            scope.selectedErrorType = {
                codes: {"1": false}
            };
        }
        scope.searchedText = null;
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/bleSearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            searchConfig: '=',
            menuContext: '=',
            searchCallback: '='
        },
        link: function (scope, element) {
            scope.$on('$viewContentLoaded', bindEvents(element));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });
            searchConfig = scope.searchConfig;

            // 날짜 정보를 세팅한다.
            scope.dateTypes = BLE_DATE_TYPES;
            scope.selectedDateType = scope.dateTypes[0].key;
            scope.selectedCalStart = DateHelper.getPreviousDate(1, 'YYYY.MM.DD');
            scope.selectedCalEnd = DateHelper.getPreviousDate(1, 'YYYY.MM.DD');

            // 검색 영역을 세팅한다.
            initSearchSection(scope);

            /**
             * 날짜 타입 변경
             * @param dateType
             */
            scope.changeDateType = function (dateType) {
                var defaultCal = reportSvc.defaultCustomCal(dateType.key, 1, 1, 1);
                scope.selectedCalStart = defaultCal.startDateStr;
                scope.selectedCalEnd = defaultCal.endDateStr;
                scope.selectedDateType = dateType.key;
            };

            // 리포트 조회
            bindSearchButton(scope, element);
        }
    };
});

/**
 * directives > funnelSearchBox
 */
directives.lazy.directive('funnelSearchBox', function (reportSvc, REPORT_DATE_TYPES, DATE_TYPE_MONTH) {
    var activeMenuItem;
    var SEARCH_BTN = '#_funnelSearchBtn';

    function bindEvents(element) {
        EventBindingHelper.initDatePicker(element, null);
    }

    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            var searchDateType = scope.selectedDateType;
            if (!DateHelper.isValid(scope.selectedCalStart, 'YYYY.MM.DD')) {
                alert('조회 시작일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchStartDate = DateHelper.dateToParamString(scope.selectedCalStart);
            if (!DateHelper.isValid(scope.selectedCalEnd, 'YYYY.MM.DD')) {
                alert('조회 종료일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchEndDate = DateHelper.dateToParamString(scope.selectedCalEnd);

            if (searchDateType == DATE_TYPE_MONTH) {
                searchStartDate = searchStartDate.substring(0, 6);
                searchEndDate = searchEndDate.substring(0, 6);
            }
            scope.$apply(function () {
                var result = {
                    'searchDateType': searchDateType
                };

                if (scope.searchedProcCode) {
                    result.searchProcCode = scope.searchedProcCode.code;
                }
                if (scope.searchedPocCode) {
                    result.searchPocCode = scope.searchedPocCode.code;
                }
                if (scope.searchedSstCode) {
                    if (scope.searchedProcCode.code === 'p1') {
                        result.searchSstCode = scope.searchedSstCode.code;
                    } else {
                        result.searchSstCode = '';
                    }
                }
                result.searchStartDate = searchStartDate;
                result.searchEndDate = searchEndDate;

                scope.searchCallback(result);
            });
        });

    }

    /**
     * 날짜 타입 세팅
     * @param menuItem
     * @returns {*}
     */
    function getFilteredSearchDateTypes() {
        // 메뉴검색옵션을 사용하지 않거나 dateTypes이 명시되지 않았으면 기본 값을 return 한다.
        if (activeMenuItem.menuSearchOptionYn != 'Y'
            || activeMenuItem.menuSearchOption.dateTypes == null) {
            return REPORT_DATE_TYPES;
        }

        var filteredDateTypes = activeMenuItem.menuSearchOption.dateTypes;
        var resultDateTypes = [];
        angular.forEach(REPORT_DATE_TYPES, function (obj) {
            if (filteredDateTypes.indexOf(obj.key) > -1) {
                resultDateTypes.push(obj);
            }
        });
        return resultDateTypes;
    }

    /**
     * 검색 영역을 세팅한다.
     * @param scope
     */
    function initSearchSection(scope) {
        scope.funnelSearchOption = {};
        scope.isService = true;
        // select 검색 영역을 만든다.
        reportSvc.getReportApi("/commonCode/searchBox/23").then(function (result) {
            scope.funnelSearchOption = result;
            scope.searchedProcCode = scope.funnelSearchOption.process[0];
            scope.searchedPocCode = scope.funnelSearchOption.poc[0];
            scope.searchedSstCode = scope.funnelSearchOption.sst[0];
        });
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/reportFunnelSearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            menuContext: '=',
            searchCallback: '='
        },
        link: function (scope, element) {
            scope.$on('$viewContentLoaded', bindEvents(element));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });
            activeMenuItem = scope.menuContext.menu;

            // 날짜 정보를 세팅한다.
            scope.dateTypes = getFilteredSearchDateTypes();
            scope.selectedDateType = scope.dateTypes[0].key;
            scope.selectedCalStart = reportSvc.defaultCal(scope.selectedDateType).startDateStr;
            scope.selectedCalEnd = reportSvc.defaultCal(scope.selectedDateType).endDateStr;

            // 검색 영역을 세팅한다.
            initSearchSection(scope);

            /**
             * 날짜 타입 변경
             * @param dateType
             */
            scope.changeDateType = function (dateType) {
                var defaultCal = reportSvc.defaultCal(dateType.key);
                scope.selectedCalStart = defaultCal.startDateStr;
                scope.selectedCalEnd = defaultCal.endDateStr;
                scope.selectedDateType = dateType.key;
            };

            scope.checkProcess = function() {
                if (scope.searchedProcCode.code === 'p1') {
                    scope.isService = true;
                } else {
                    scope.isService = false;
                }
            };

            // 리포트 조회
            bindSearchButton(scope, element);
        }
    };
});

/**
 * directives > sankeySearchBox
 */
directives.lazy.directive('sankeySearchBox', function ($http) {
    var SEARCH_BTN = '#_sankeySearchBtn';

    function bindEvents(scope, element) {
       EventBindingHelper.initDatePicker(element, null);
    }

    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            if (!DateHelper.isValid(scope.startDate, 'YYYY.MM.DD') || !DateHelper.isValid(scope.endDate, 'YYYY.MM.DD')) {
                alert('조회 일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            scope.$apply(function () {
                var result = {
                    'searchStartDate': DateHelper.dateToParamString(scope.startDate),
                    'searchEndDate': DateHelper.dateToParamString(scope.endDate),
                    'searchSite': 'voyager',
                    'searchService': 'ocb',
                    'searchType': scope.type,
                    'firstDepthName' : getTypeName(scope)
                };
               scope.searchCallback(result);
            });
        });
    }

    function getTypeName(scope){
        var tmpName = '';
        _.some(scope.sankeyChartTypeList, function(element, index, list){
            if(element.codeId == scope.type){
                tmpName = element.codeName;
                return element.codeName;
            }
        });
        return tmpName;
    }

    function initSankeyChartType(scope) {
        var tmpCode = scope.menuContext.service.code + '_' + scope.menuContext.category.code + '_' + scope.menuContext.menu.code;
        var url = "/commonGroupCode/getCodes?groupCodeId=" + tmpCode;
        $http.get(url).success(function (data, status, headers, config) {
            scope.sankeyChartTypeList = data;
        });
   }

    return {
        restrict: 'E',
        templateUrl: "page/templates/reportSankeySearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            searchConfig: '=',
            searchCallback: '=',
            menuContext: '='
        },
        link: function (scope, element, attrs) {

            initSankeyChartType(scope);

            scope.$on('$viewContentLoaded', bindEvents(scope, element));

            scope.startDate = DateHelper.stringToYmdStr4(scope.searchConfig.startDate);
            scope.endDate = DateHelper.stringToYmdStr4(scope.searchConfig.endDate);
            scope.type = scope.searchConfig.type;

            // 리포트 조회
            bindSearchButton(scope, element);
        }
    };
});

/**
 * directives > mstrSearchBox
 */
directives.lazy.directive('mstrSearchBox', function (reportSvc) {
    //var activeMenuItem;
    var SEARCH_BTN = '#_mstrSearchBtn';

    function bindEvents(element) {
        EventBindingHelper.initDatePicker(element, null);
    }

    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function() {
            var searchDateType = scope.selectedDateType;
            if (!DateHelper.isValid(scope.selectedCalStart, 'YYYY.MM.DD')) {
                alert('조회 시작일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchStartDate = DateHelper.dateToParamString(scope.selectedCalStart);
            if (!DateHelper.isValid(scope.selectedCalEnd, 'YYYY.MM.DD')) {
                alert('조회 종료일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            var searchEndDate = DateHelper.dateToParamString(scope.selectedCalEnd);
            //if (searchDateType == DATE_TYPE_MONTH) {
            //    searchStartDate = searchStartDate.substring(0, 6);
            //    searchEndDate = searchEndDate.substring(0, 6);
            //}
            scope.$apply(function () {
                var result = {
                    'searchDateType': searchDateType
                };
                result.searchStartDate = searchStartDate;
                result.searchEndDate = searchEndDate;

                scope.searchCallback(result);
            });
        });
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/reportMstrSearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            menuContext: '=',
            searchCallback: '=',
            dateTypes: '=',
            changeDateCallback: '='
        },
        link: function (scope, element) {
            scope.$on('$viewContentLoaded', bindEvents(element));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });
            //activeMenuItem = scope.menuContext.menu;

            // 날짜 정보를 세팅한다.
            scope.selectedDateType = scope.dateTypes[0].key;
            scope.selectedCalStart = reportSvc.defaultCal(scope.selectedDateType).startDateStr;
            scope.selectedCalEnd = reportSvc.defaultCal(scope.selectedDateType).endDateStr;

            /**
             * 날짜 타입 변경
             * @param dateType
             */
            scope.changeDateType = function (dateType) {
                var defaultCal = reportSvc.defaultCal(dateType.key);
                scope.selectedCalStart = defaultCal.startDateStr;
                scope.selectedCalEnd = defaultCal.endDateStr;
                scope.selectedDateType = dateType.key;

                var result = {
                    'searchDateType': scope.selectedDateType
                };
                result.searchStartDate = DateHelper.dateToParamString(scope.selectedCalStart);
                result.searchEndDate = DateHelper.dateToParamString(scope.selectedCalEnd);

                scope.changeDateCallback(result);
            };

            // 리포트 조회
            bindSearchButton(scope, element);
        }
    };
});

/**
 * directives > mstrReport
 */
directives.lazy.directive('mstrReport', function () {
    return {
        restrict: 'E',
        scope: {
            iframeId: '='
        },
        link: function (scope, element) {
            element.append("<iframe id='" + scope.iframeId + "' name='" + scope.iframeId + "' frameborder='0'></iframe>");
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });
        }
    };
});
