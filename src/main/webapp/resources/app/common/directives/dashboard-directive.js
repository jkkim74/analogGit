var directives = angular.module('app.commonDirectives');

/**
 * dashboardDirectives > dashboardReportSearchBox
 */
directives.lazy.directive('dashboardBmSearchBox', function (DASHBOARD_BM_DATE_TYPES, dashboardReportSvc) {
    var SEARCH_BTN = '#_searchBtn';
    var DATE_TYPES = [];
    for (var i = 0; i < DASHBOARD_BM_DATE_TYPES.length; i++) {
        DATE_TYPES.push(DASHBOARD_BM_DATE_TYPES[i].key);
    }

    function bindEvents(element) {
        EventBindingHelper.initDatePicker(element, null);
    }

    function bindSearchButton(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            if (!DateHelper.isValid(scope.selectedDate, 'YYYY.MM.DD')) {
                alert('조회 일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            scope.$apply(function () {
                var dateType = scope.selectedDatePeriodType.key;
                var selectedDate = DateHelper.stringToDate(scope.selectedDate);
                var diffMs = dashboardReportSvc.getPreviousDiffMs(dateType, selectedDate);
                var startDate = new Date(diffMs);
                var result = {
                    'searchDateType': dateType,
                    'searchStartDate': DATE_TYPES.indexOf(dateType) != -1 ? DateHelper.getPreviousDateFromDate(scope.selectedDate, dateType - 1, 'YYYYMMDD') : DateHelper.dateToParamString(startDate),
                    'searchEndDate': DATE_TYPES.indexOf(dateType) != -1 ? DateHelper.dateToParamString(scope.selectedDate) : DateHelper.dateToParamString(scope.selectedDate)
                };
                scope.searchCallback(result);
            });
        });
    }

    function callSearchCallback(scope) {
        var dateType = scope.selectedDatePeriodType.key;
        var selectedDate = DateHelper.stringToDate(scope.selectedDate);
        var diffMs = dashboardReportSvc.getPreviousDiffMs(dateType, selectedDate);
        var startDate = new Date(diffMs);
        var result = {
            'searchDateType': dateType,
            'searchStartDate': DATE_TYPES.indexOf(dateType) != -1 ? DateHelper.getPreviousDateFromDate(scope.selectedDate, dateType - 1, 'YYYYMMDD') : DateHelper.dateToParamString(startDate),
            'searchEndDate': DATE_TYPES.indexOf(dateType) != -1 ? DateHelper.dateToParamString(scope.selectedDate) : DateHelper.dateToParamString(scope.selectedDate)
        };
        scope.searchCallback(result);
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/dashboardBmSearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            searchCallback: '=',
            menuContext: '='
        },
        link: function (scope, element) {
            scope.$on('$viewContentLoaded', bindEvents(element));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });
            scope.datePeriodTypes = scope.menuContext.datePeriodTypes;
            if (scope.datePeriodTypes == null) {
                scope.datePeriodTypes = DASHBOARD_BM_DATE_TYPES;
            }

            //ocb, gifticon일 경우 2일전, 그외 1일전 날짜 세팅. (voyop-338, cookatrice)
            var tempMenuCode = scope.menuContext.menu.code;
            var diffDateValue = (tempMenuCode == 'ocb' || tempMenuCode == 'gifticon') ? 2 : 1;

            scope.selectedDate = DateHelper.getPreviousDate(diffDateValue, 'YYYY.MM.DD');
            scope.selectedDatePeriodType = scope.datePeriodTypes[0];

            // 날짜 타입 변경
            scope.changeDateType = function (dateType) {
                scope.selectedDatePeriodType = dateType;
                callSearchCallback(scope);
            };

            // 리포트 조회
            bindSearchButton(scope, element);

            // make one-page bm dashboard refresh
            scope.$on('bmDashboardSearchBoxRefrash', function (event, menuContext) {
                //console.log('call bmDashboardSearchBoxRefrash....');
                var tempMenuCode = menuContext.menu.code;
                var diffDateValue = (tempMenuCode == 'ocb' || tempMenuCode == 'gifticon') ? 2 : 1;
                scope.selectedDate = DateHelper.getPreviousDate(diffDateValue, 'YYYY.MM.DD');
                scope.selectedDatePeriodType = scope.datePeriodTypes[0];

                scope.$emit('bmDashboardMenuClick');
            });
        }
    };
});

/**
 * dashboardDirectives > dashboardOcSearchBox
 */
directives.lazy.directive('dashboardOcSearchBox', function (DASHBOARD_BM_DATE_TYPES, dashboardReportSvc, ocDashboardSvc) {
    var SEARCH_BTN = '#_searchBtn';
    var DATE_TYPES = _.map(DASHBOARD_BM_DATE_TYPES, function (value) { return value.key; });

    function bindEvents(scope) {
        //EventBindingHelper.initDatePicker(element, null);
        ocDashboardSvc.bindingDatePicker(scope.searchConfig.dateType);
    }

    function doDaySearchBtnClick(scope, element) {
        element.find(SEARCH_BTN).bind('click', function () {
            if (!DateHelper.isValid(scope.selectedDate, 'YYYY.MM.DD')) {
                alert('조회 일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            scope.$apply(function () {
                var dateType = scope.selectedDatePeriodType.key;
                var selectedDate = DateHelper.stringToDate(scope.selectedDate);
                var diffMs = dashboardReportSvc.getPreviousDiffMs(dateType, selectedDate);
                var startDate = new Date(diffMs);
                var result = {
                    'searchDateType': dateType,
                    'searchStartDate': DATE_TYPES.indexOf(dateType) != -1 ? DateHelper.getPreviousDateFromDate(scope.selectedDate, dateType - 1, 'YYYYMMDD') : DateHelper.dateToParamString(startDate),
                    'searchEndDate': DATE_TYPES.indexOf(dateType) != -1 ? DateHelper.dateToParamString(scope.selectedDate) : DateHelper.dateToParamString(scope.selectedDate)
                };
                scope.searchCallback(result);
            });
        });
    }

    function doWeekSearchBtnClick(scope, element){
        element.find(SEARCH_BTN).bind('click', function () {
            if (!DateHelper.isValid(scope.startDate, 'YYYY.MM.DD') || !DateHelper.isValid(scope.endDate, 'YYYY.MM.DD')) {
                alert('조회 일자가 정상적인 포멧이 아닙니다.');
                return;
            }
            scope.$apply(function () {
                var result = {
                    'searchDateType': 'week',
                    'searchStartDate': DateHelper.dateToParamString(scope.startDate),
                    'searchEndDate': DateHelper.dateToParamString(scope.endDate)
                };
                scope.searchCallback(result);
            });
        });
    }

    function bindSearchButton(scope, element) {
          if (scope.searchConfig.dateType == 'day') {
             doDaySearchBtnClick(scope, element);
          } else if(scope.searchConfig.dateType == 'week'){
              doWeekSearchBtnClick(scope, element);
          }
    }

    function callSearchCallback(scope) {
        var dateType = scope.selectedDatePeriodType.key;
        var selectedDate = DateHelper.stringToDate(scope.selectedDate);
        var diffMs = dashboardReportSvc.getPreviousDiffMs(dateType, selectedDate);
        var startDate = new Date(diffMs);
        var result = {
            'searchDateType': dateType,
            'searchStartDate': DATE_TYPES.indexOf(dateType) != -1 ? DateHelper.getPreviousDateFromDate(scope.selectedDate, dateType - 1, 'YYYYMMDD') : DateHelper.dateToParamString(startDate),
            'searchEndDate': DATE_TYPES.indexOf(dateType) != -1 ? DateHelper.dateToParamString(scope.selectedDate) : DateHelper.dateToParamString(scope.selectedDate)
        };
        scope.searchCallback(result);
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/dashboardOcSearchBox.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            searchConfig: '=',
            searchCallback: '=',
            menuContext: '='
        },
        link: function (scope, element) {

            //if searchConfig is undefinded, init it's dateType is 'day'.
            if(angular.isUndefined(scope.searchConfig)){
                scope.searchConfig = {dateType : 'day'};
            }

            scope.$on('$viewContentLoaded', bindEvents(scope, element));
            scope.$on('$destroy', function() {
                element.off(); // deregister all event handlers
                element.unbind();
            });
            if (scope.searchConfig.dateType == 'day') {
                scope.datePeriodTypes = scope.menuContext.datePeriodTypes;
                if (scope.datePeriodTypes == null) {
                    scope.datePeriodTypes = DASHBOARD_BM_DATE_TYPES;
                }

                scope.selectedDate = DateHelper.getPreviousDate(1, 'YYYY.MM.DD');
                scope.selectedDatePeriodType = scope.datePeriodTypes[0];

                // 날짜 타입 변경
                scope.changeDateType = function (dateType) {
                    scope.selectedDatePeriodType = dateType;
                    callSearchCallback(scope);
                };
            } else if (scope.searchConfig.dateType == 'week') {
                scope.startDate = DateHelper.stringToYmdStr4(scope.searchConfig.startDate);
                scope.endDate = DateHelper.stringToYmdStr4(scope.searchConfig.endDate);

            }

            // 리포트 조회
            bindSearchButton(scope, element);
        }
    };
});

