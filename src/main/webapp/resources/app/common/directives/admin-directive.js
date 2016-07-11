var directives = angular.module('app.commonDirectives');

/**
 * directives > bpmResultSearchBox
 */
directives.lazy.directive('bpmResultSearchBox', function (REPORT_DATE_TYPES, bpmResultSvc) {
    var SEARCH_BTN = '#_bpmResultSearchBtn';
    var EXCEL_BTN = '#_bpmResultExcelBtn';

    function bindEvents(scope, element) {
        EventBindingHelper.initDatePicker(element, resetWeekOrMonthDate);

        // week, month는 월단위까지만 표시하고 week은 주차정보까지 호출해준다.
        function resetWeekOrMonthDate(ev) {
            if(scope.selectedDateType.key == 'day') {
                return;
            }

            var isWeek = (scope.selectedDateType.key == 'week');
            var searchInputId = angular.element(ev.target).find('input').attr('id');

            if(searchInputId == 'search-date01') {
                scope.selectedCalStart = ev.date.ym();
                if (isWeek) {
                    setStartWeekNumbers(scope);
                }
            }

            if(searchInputId == 'search-date02') {
                scope.selectedCalEnd = ev.date.ym();
                if(isWeek) {
                    setEndWeekNumbers(scope);
                }
            }
        }

        // 조회 버튼
        element.find(SEARCH_BTN).bind('click', function () {
            scope.$apply(function () {
                scope.excelFlag = false;
                scope.searchCallback(getSearchResultParams(scope));
            });
        });
        // 엑셀 버튼
        element.find(EXCEL_BTN).bind('click', function () {
            scope.$apply(function () {
                scope.excelFlag = true;
                scope.searchCallback(getSearchResultParams(scope));
            });
        });
    }

    function getSearchResultParams(scope) {
        var result = {
            'dateType': scope.selectedDateType.key,
            'startDate': DateHelper.dateToParamString(scope.selectedCalStart),
            'endDate': DateHelper.dateToParamString(scope.selectedCalEnd),
            'svcId':scope.selectedSvc.svcCdId,
            'excelFlag': scope.excelFlag
        };

        if(scope.selectedIdxClGrp != null) {
            result.idxClGrpCd =  scope.selectedIdxClGrp.svcCdId;
        }

        if(scope.selectedIdxCl != null) {
            result.idxClCd =  scope.selectedIdxCl.svcCdId;
        }

        if(scope.selectedDateType.key == 'week') {
            result.startWeekNumber = scope.selectedStartWk.svcCdId;
            result.endWeekNumber = scope.selectedEndWk.svcCdId;
        }
        return result;
    }

    // 서비스 로드
    function loadSvcs(scope) {
        bpmResultSvc.getBpmSvcs().then(function (data) {
            scope.svcs = data;
            scope.selectedSvc = scope.svcs[0];

            // 서비스 로드 후 initCallback으로 getSearchResultParams를 넘겨준다.
            scope.initCallback(getSearchResultParams(scope));
        }).then(function () {
            loadIdxClGrps(scope);
        });
    }

    // 지표 목록 로드
    function loadIdxClGrps(scope) {
        scope.idxClGrps = {};
        scope.idxCls = {};
        bpmResultSvc.getBpmCycleToGrps(scope.selectedSvc.svcCdId).then(function (data) {
            scope.idxClGrps = data;
            scope.selectedIdxClGrp = {};
        });
    }

    // 지표 로드
    function loadIdxCls(scope) {
        scope.idxCls = {};
        scope.selectedIdxCl = {};
        if (scope.selectedIdxClGrp != null) {
            var svcId = scope.selectedSvc.svcCdId;
            var idxClGrpCd = scope.selectedIdxClGrp.svcCdId;
            bpmResultSvc.getBpmGrpToCls(svcId, idxClGrpCd).then(function (data) {
                scope.idxCls = data;
                scope.selectedIdxCl = {};
            });
        }
    }

    // 기본날짜 세팅
    function setDefaultCal(scope) {
        var defaultCal = bpmResultSvc.defaultCal(scope.selectedDateType.key);
        scope.selectedCalStart = defaultCal.startDateStr;
        scope.selectedCalEnd = defaultCal.endDateStr;
        if (scope.selectedDateType.key == 'week') {
            setStartWeekNumbers(scope);
            setEndWeekNumbers(scope);
        }
    }

    // 시작 주차 정보를 조회한다.
    function setStartWeekNumbers(scope) {
        var wkStcStrdYmw = DateHelper.dateToParamString(scope.selectedCalStart);
        scope.selectedStartWk = {};

        bpmResultSvc.getBpmWkStrds(wkStcStrdYmw).then(function (data) {
            scope.startWks = data;
            scope.selectedStartWk = scope.startWks[0];
        });
    }

    // 종료 주차 정보를 조회한다.
    function setEndWeekNumbers(scope) {
        var wkStcStrdYmw = DateHelper.dateToParamString(scope.selectedCalEnd);
        scope.selectedEndWk = {};

        bpmResultSvc.getBpmWkStrds(wkStcStrdYmw).then(function (data) {
            scope.endWks = data;
            scope.selectedEndWk = scope.endWks[0];
        });
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/bpmResultSearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            searchCallback: '=',
            excelFlag: '=',
            menuContext: '=',
            initCallback: '='
        },
        link: function (scope, element) {
            scope.dateTypes = REPORT_DATE_TYPES;
            scope.selectedDateType = scope.dateTypes[0];
            scope.selectedSvc = {};
            scope.selectedIdxClGrp = {};
            scope.selectedIdxCl = {};
            scope.excelFlag = false;
            bindEvents(scope, element);

            // 서비스 조회
            loadSvcs(scope);

            // 기본 날짜 세팅
            setDefaultCal(scope);

            // 서비스 변경
            scope.changeSvc = function() {
                loadIdxClGrps(scope);
            };

            // 지표 목록 변경
            scope.changeIdxClGrp = function() {
                loadIdxCls(scope);
            };

            // 날짜 타입 변경
            scope.changeDateType = function (dateType) {
                scope.selectedDateType = dateType;
                setDefaultCal(scope);
            };

            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });
        }
    };
});

/**
 * directives > adminSearchBox
 */
directives.lazy.directive('adminSearchBox', function () {
    var SEARCH_BTN = '#_adminSearchBtn';

    function bindEvents(element) {
        EventBindingHelper.initDatePicker(element, null);
    }

    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            scope.$apply(function () {
                var result = null;
                if (scope.dateFlag) {
                    if (scope.selectedDateType) {
                        var adminDateType = scope.selectedDateType.key;
                        result = {
                            'searchDateType': adminDateType,
                            'searchDate': DateHelper.dateToParamString(scope.selectedDate)
                        };
                    } else {
                        result = {
                            'searchDate': DateHelper.dateToParamString(scope.selectedDate)
                        };
                    }
                }
                if (scope.searchedText) {
                    result = {
                        'searchString': $.trim(scope.searchedText)
                    };
                }
                scope.searchCallback(result);
            });
        });
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/adminSearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            searchCallback: '=',
            menuContext: '=',
            adminDateTypes: '=',
            dateFlag: '=',
            textFlag: '='
        },
        link: function (scope, element) {
            scope.$on('$viewContentLoaded', bindEvents(element));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });
            if (scope.dateFlag) {
                scope.selectedDate = DateHelper.getCurrentDate();
                if (scope.adminDateTypes) {
                    scope.selectedDateType = scope.adminDateTypes[0];
                    // 날짜 타입 변경
                    scope.changeDateType = function (type) {
                        scope.selectedDateType = type;
                    };
                }
                if (scope.textFlag) {
                    scope.searchedText = null;
                }
            }
            // 리포트 조회
            bindSearchButton(scope, element);
        }
    };
});

/**
 * directives > adminComSearchBox
 */
directives.lazy.directive('adminComSearchBox', function (adminReportSvc) {
    var SEARCH_BTN = '#_adminComSearchBtn';

    function bindEvents(element) {
        EventBindingHelper.initDatePicker(element, null);
    }

    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            scope.$apply(function () {
                var result = null;
                if (scope.selectedDateType) {
                    var adminDateType = scope.selectedDateType.key;
                    result = {
                        'searchDateType': adminDateType,
                        'selectedCalStart': DateHelper.dateToParamString(scope.selectedCalStart),
                        'selectedCalEnd': DateHelper.dateToParamString(scope.selectedCalEnd)
                    };
                } else {
                    result = {
                        'selectedCalStart': DateHelper.dateToParamString(scope.selectedCalStart),
                        'selectedCalEnd': DateHelper.dateToParamString(scope.selectedCalEnd)
                    };
                }

                scope.searchCallback(result);
            });
        });
    }

    // 기본날짜 세팅
    function setDefaultCal(scope) {
        var defaultCal = adminReportSvc.defaultCal(scope.selectedDateType.key);
        scope.selectedCalStart = defaultCal.startDateStr;
        scope.selectedCalEnd = defaultCal.endDateStr;
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/adminComSearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            searchCallback: '=',
            menuContext: '=',
            adminDateTypes: '=',
            dateFlag: '='
        },
        link: function (scope, element) {
            scope.$on('$viewContentLoaded', bindEvents(element));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });
            if (scope.dateFlag) {
                scope.selectedCalStart = adminReportSvc.defaultCal(scope.selectedDateType).startDateStr;
                scope.selectedCalEnd = adminReportSvc.defaultCal(scope.selectedDateType).endDateStr;
                if (scope.adminDateTypes) {
                    scope.selectedDateType = scope.adminDateTypes[0];
                    // 날짜 타입 변경
                    scope.changeDateType = function (type) {
                        scope.selectedDateType = type;
                        setDefaultCal(scope);
                    };
                }
            }
            // 리포트 조회
            bindSearchButton(scope, element);
        }
    };
});
