var factories = angular.module('app.commonFactory');

/**
 *  factories > reportSvc
 */
(factories.lazy || factories).factory('reportSvc', function ($timeout, $http, $q, menuSvc, urlHandleSvc) {
    /**
     * 리포트 기본 milliseconds를 구한다.
     * @param dateType
     * @param date
     * @returns {*}
     */
    function getReportDiffMs(dateType, date) {
        if (dateType == 'week') {
            return DateHelper.getWeeklyDiffMs(date, 6); // 최근 6주 (주별)
        } else if (dateType == 'month') {
            return DateHelper.getMonthlyDiffMs(date, 6); // 최근 6개월 (월별)
        } else {
            return DateHelper.getDailyDiffMs(date, 10);// 최근 10일 (일별)
        }
    }

    function getCustomDiffMs(dateType, date, diffDay, diffWeeks, diffMonths) {
        //set default value.
        if(angular.isUndefined(diffDay)){
            diffDay = 10;
        }
        if(angular.isUndefined(diffWeeks)){
            diffWeeks = 6;
        }
        if(angular.isUndefined(diffMonths)){
            diffMonths = 6;
        }

        if (dateType == 'week') {
            return DateHelper.getWeeklyDiffMs(date, diffWeeks);
        } else if (dateType == 'month') {
            return DateHelper.getMonthlyDiffMs(date, diffMonths);
        } else {
            return DateHelper.getDailyDiffMs(date, diffDay);
        }
    }

    return {
        /**
         * 리포트 기본 시작일/종료일 조회
         *
         * @returns {{startDate: Date, endDate: Date, startDateStr: *, endDateStr: *, startDateStrPlan: *, endDateStrPlan: *}}
         */
        defaultCal: function (dateType) {
            var diffDate = getReportDiffMs(dateType, new Date());
            var startDate = new Date(diffDate);
            var endDate = DateHelper.getPreviousDayDate(new Date(), 1);     //report 조회 날짜 하루전 표시
            return {
                'startDate': startDate,  // Date
                'endDate': endDate,
                'startDateStr': moment(startDate).format('YYYY.MM.DD'),  // yyyy.mm.dd
                'endDateStr': moment(endDate).format('YYYY.MM.DD'),
                'startDateStrPlain': (dateType == 'month') ? moment(startDate).format('YYYYMM') : moment(startDate).format('YYYYMMDD'), // yyyymm || yyyymmdd
                'endDateStrPlain': (dateType == 'month') ? moment(endDate).format('YYYYMM') : moment(endDate).format('YYYYMMDD')
            };
        },
        /**
         * 리포트 기본 전일하루/전주/전달 조회
         *
         * @returns {{startDate: Date, endDate: Date, startDateStr: *, endDateStr: *, startDateStrPlan: *, endDateStrPlan: *}}
         */
        defaultCustomCal: function (dateType, diffDay, diffWeeks, diffMonths) {
            var diffDate = getCustomDiffMs(dateType, new Date(), diffDay, diffWeeks, diffMonths);
            var startDate = new Date(diffDate);
            var endDate = DateHelper.getPreviousDayDate(new Date(), 1);     //report 조회 날짜 하루전 표시
            return {
                'startDate': startDate,  // Date
                'endDate': endDate,
                'startDateStr': moment(startDate).format('YYYY.MM.DD'),  // yyyy.mm.dd
                'endDateStr': moment(endDate).format('YYYY.MM.DD'),
                'startDateStrPlain': (dateType == 'month') ? moment(startDate).format('YYYMM') : moment(startDate).format('YYYYMMDD'), // yyyymm || yyyymmdd
                'endDateStrPlain': (dateType == 'month') ? moment(endDate).format('YYYYMM') : moment(endDate).format('YYYYMMDD')
            };
        },
        /**
         * 리포트별 데이토 조회
         * @param url
         * @returns {*}
         */
        getReportApi: function (url) {
            return $http.get(url).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    return $q.reject(response.data);
                }
            }, function (response) {
                return $q.reject(response.data);
            });
        },
        /**
         * Report API Wrapper(post)
         * @param url
         * @param requestData
         * @returns {code: 200,  message: string}
         */
        postReportApi: function (url, requestData) {
            return $http.post(url, requestData).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * menuUrl에 해당하는 item을 반환한다.
         * @param menuUrl
         * @returns {{service: *, category: *, menu: *, resourceUrl: string}}
         */
        getItemByMenuUrl: function (menuUrl) {
            var serviceItem;
            var categoryItem;
            var menuItem;

            // serviceId 기준으로 service를 찾는다.
            var reportServices = menuSvc.getReportService();
            serviceItem = _.find(reportServices, function(obj) {
                return (menuUrl.serviceCode == obj.code);
            });
            categoryItem = _.find(serviceItem.categories, function(obj) {
                return (menuUrl.categoryCode == obj.code);
            });
            menuItem = _.find(categoryItem.menus, function(obj) {
                return (menuUrl.menuCode == obj.code);
            });
           return this.newMenuItem(serviceItem, categoryItem, menuItem);
        },

        /**
         * 새로운 menuItem 을 생성한다.
         * @param service
         * @param category
         * @param menu
         * @returns {{service: *, category: *, menu: *, resourceUrl: string}}
         */
        newMenuItem: function (service, category, menu) {
            var templateUrl;
            //var templateUrl = 'page/' + resourceUri + '.html';
            if (menu.menuSearchOption && menu.menuSearchOption.addType === 'mstr') {
               templateUrl = (menu.authority == 'Y') ? 'page/mstr/mstr.html' : 'page/authority/unauthorized.html';
            } else {
                var resourceUri = service.code + '/' + category.code;
                if (menu) {
                    resourceUri += '/' + menu.code;
                }
                templateUrl = (menu.authority == 'Y')
                    ? 'page/' + resourceUri + '.html' : 'page/authority/unauthorized.html';
            }
            return {
                service: service,
                category: category,
                menu: menu,
                templateUrl: templateUrl
            };
        },

        /**
         * url을 정보를 이용하여 menuItem을 생성한다..
         * @returns {{service: *, category: *, menu: *, resourceUrl: string}}
         */
        urlToMenuItem: function () {
            var menuUrl = urlHandleSvc.getMenuUrl();
            return this.getItemByMenuUrl(menuUrl);
        },

        /**
         * 카테고리 메뉴를 클릭한다.
         * @param categoryCode
         */
        triggerClickCategoryMenu: function (categoryCode) {
            var $categoryMenu = angular.element('#category-menu-' + categoryCode);
            if (!$categoryMenu.hasClass('active')) {
                $timeout(function () {
                    $categoryMenu.triggerHandler('click');
                }, 0);
            }
        }

    };
});


/**
 *  factories > reportTabSvc Service
 */
(factories.lazy || factories).factory('reportTabSvc', function (REPORT_TAB_LIMIT_SIZE) {
    var reportTabs = [];

    return {
        /**
         * 이미 생성된 tab인지 체크
         * @param item
         * @returns {boolean}
         */
        isExists: function (item) {
            var result = false;
            reportTabs.forEach(function (obj) {
                if (obj.service.code == item.service.code
                    && obj.category.code == item.category.code) {
                    // 요약리포트는 category id까지만 비교한다.
                    if (item.category.summaryReportYn == 'Y') {
                        return result = true;
                    } else {
                        if (obj.menu.code == item.menu.code) {
                            return result = true;
                        }
                    }
                }
            });
            return result;
        },

        /**
         * 리포트 최대 갯수 검증
         * @returns {boolean}
         */
        isAddable: function () {
            return (reportTabs.length < REPORT_TAB_LIMIT_SIZE);
        },

        /**
         * tab을 추가한다.
         * @param item
         */
        addTab: function (item) {
            reportTabs.push(item);
        },

        /**
         * tab을 제거한다.
         * @param menuItem
         */
        removeTab: function (item) {
            _.find(reportTabs, function(obj, i) {
                if (item.menu.id == obj.menu.id) {
                    reportTabs.splice(i, 1);
                    return true;
                }
            });
        },

        /**
         * clear tabs
         */
        removeAll: function () {
            reportTabs = [];
        },

        /**
         * 가장 마지막에 등록된 tab을 가져온다.
         * @returns lastTab
         */
        getLastTab: function () {
            return reportTabs.slice(-1)[0];
        },

        /**
         * getReportTabs
         * @returns {Array}
         */
        getReportTabs: function () {
            return reportTabs;
        }
    };
});

/**
 *  factories > summaryReportSvc
 */
(factories.lazy || factories).factory('summaryReportSvc', function ($http) {

    /**
     * 날짜 컬럼 레이블 반환.
     * @param textLabel
     * @param dateObject
     * @param dateType
     * @returns {string}
     */
    function getDateColumnLabel(textLabel, dateObject, dateType) {
        var dateLabel;

        if (dateType == 'week') {
            var weekRange = DateHelper.getWeekendRange(dateObject);
            dateLabel = [weekRange.mondayYmd, '~', weekRange.sundayYmd].join('');
        } else if (dateType == 'month') {
            dateLabel = DateHelper.dateObjectToYyyymmdd(dateObject, '.').substring(0, 7);
        } else {
            dateLabel = DateHelper.dateObjectToYymmdd(dateObject);
        }

        return [textLabel, '<br /><span class="text-normal">(', dateLabel, ')</span>'].join('');
    }

    /**
     * 추이 리포트 레이블 반환
     * @param textLabel
     * @param startDate
     * @param endDate
     * @param dateType
     * @returns {string}
     */
    function getTrendPeriodColumnLabel(textLabel, startDate, endDate, dateType) {
        var startDateLabel, endDateLabel;

        if (dateType == 'week') {
            startDateLabel = DateHelper.getWeekendRange(startDate).mondayYmd; // 시작일자의 월요일
            endDateLabel = DateHelper.getWeekendRange(endDate).sundayYmd; // 기준일의 일요일
        } else if (dateType == 'month') {
            startDateLabel = DateHelper.dateObjectToYyyymmdd(startDate, '.').substring(0, 7);
            endDateLabel = DateHelper.dateObjectToYyyymmdd(endDate, '.').substring(0, 7);
        } else {
            startDateLabel = DateHelper.dateObjectToYymmdd(startDate);
            endDateLabel = DateHelper.dateObjectToYymmdd(endDate);
        }
        return [textLabel, '<br /><span class="text-normal">(', startDateLabel, '~', endDateLabel, ')</span>'].join('');
    }

    return {
        /**
         * 요약 리포트 기본 날짜 조회
         * @param dateType (day: 어제, week: 지난주 일요일, month: 지난달 마지막날짜)
         * @returns {*}
         */
        defaultDateStr: function (dateType) {
            var now = new Date();

            if (dateType == 'week') {
                var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                var lastSundayDate = new Date(today.setDate(today.getDate() - today.getDay()));
                return lastSundayDate.ymd();
            } else if (dateType == 'month') {
                return new Date(now.setDate(0)).ymd();
            } else {
                return DateHelper.getPreviousDayDate(now, 1).ymd();
            }
        },
        /**
         * 요약 리포트 이전 milliseconds를 구한다.
         * @param dateType (day : 8일, week : 14주, month : 13개월)
         * @param date 기준일
         * @returns {*}
         */
        getPreviousDiffMs: function (dateType, date) {
            if (dateType == 'week') {
                return DateHelper.getWeeklyDiffMs(date, 14); // 14주
            } else if (dateType == 'month') {
                return DateHelper.getMonthlyDiffMs(date, 13); // 13개월간
            } else {
                return DateHelper.getDailyDiffMs(date, 8);// 8일간
            }
        },

        /**
         * 지표 정보를 요청한다.
         * @param id
         * @returns {*}
         */
        getMeasures: function (id) {
            return $http.get('/summaryReport/measures/' + id);
        },

        /**
         * 지표 실적 목록 조회
         * @param dateType
         * @param condition
         * @returns {*}
         */
        getSummaryDailyResult: function (dateType, condition) {
            var url;
            if (dateType == 'week') {
                url = '/summaryReport/result/weekly';
            } else if (dateType == 'month') {
                url = '/summaryReport/result/monthly';
            } else {
                url = '/summaryReport/result/daily';
            }
            return $http.post(url, condition);
        },

        /**
         * 날짜 칼럼 레이블
         * @param dateType
         * @param date (yyyymmdd)
         */
        getTableColumnNames: function (dateType, date) {
            var basicDate = DateHelper.stringToDateObject(date);  // 기준일

            var columnNames;
            if (dateType == 'week') {
                columnNames = [
                    getTrendPeriodColumnLabel('14주간 추이', new Date(DateHelper.getWeeklyDiffMs(basicDate, 14)), basicDate, dateType),
                    '지표',
                    getDateColumnLabel('기준주', basicDate, dateType),
                    getDateColumnLabel('1주전', new Date(DateHelper.getWeeklyDiffMs(basicDate, 1)), dateType),
                    getDateColumnLabel('1년전', new Date(DateHelper.getMonthlyDiffMs(basicDate, 12)), dateType)
                ];
            } else if (dateType == 'month') {
                columnNames = [
                    getTrendPeriodColumnLabel('13개월 추이', new Date(DateHelper.getMonthlyDiffMs(basicDate, 12)), basicDate, dateType),
                    '지표',
                    getDateColumnLabel('기준월', basicDate, dateType),
                    getDateColumnLabel('1달전', new Date(DateHelper.getMonthlyDiffMs(basicDate, 1)), dateType),
                    getDateColumnLabel('1년전', new Date(DateHelper.getMonthlyDiffMs(basicDate, 12)), dateType)
                ];
            } else {
                columnNames = [
                    getTrendPeriodColumnLabel('8일간 추이', new Date(DateHelper.getDailyDiffMs(basicDate, 7)), basicDate, dateType),
                    '지표',
                    getDateColumnLabel('기준일', basicDate, dateType),
                    getDateColumnLabel('1일전', new Date(DateHelper.getDailyDiffMs(basicDate, 1)), dateType),
                    getDateColumnLabel('1주전', new Date(DateHelper.getWeeklyDiffMs(basicDate, 1)), dateType),
                    getDateColumnLabel('1달전', new Date(DateHelper.getMonthlyDiffMs(basicDate, 1)), dateType)
                ];
            }

            return columnNames;
        },

        /**
         * 날짜 칼럼 데이터
         * @param dateType
         */
        getTableColumnModels: function (dateType) {
            var columnModels;
            if (dateType == 'week') {
                columnModels = [
                    {name: 'chartHtml', index: 'chartHtml', sortable: false},
                    {name: 'measure', index: 'measure', sortable: false},
                    {name: 'basicMeasureValue', index: 'basicMeasureValue', align: "right", sortable: false},
                    {name: 'oneWeekAgoMeasureValue', index: 'oneWeekAgoMeasureValue', align: "right", sortable: false},
                    {name: 'oneYearAgoMeasureValue', index: 'oneYearAgoMeasureValue', align: "right", sortable: false}
                ];
            } else if (dateType == 'month') {
                columnModels = [
                    {name: 'chartHtml', index: 'chartHtml', sortable: false},
                    {name: 'measure', index: 'measure', sortable: false},
                    {name: 'basicMeasureValue', index: 'basicMeasureValue', align: "right", sortable: false},
                    {
                        name: 'oneMonthAgoMeasureValue',
                        index: 'oneMonthAgoMeasureValue',
                        align: "right",
                        sortable: false
                    },
                    {name: 'oneYearAgoMeasureValue', index: 'oneYearAgoMeasureValue', align: "right", sortable: false}
                ];
            } else {
                columnModels = [
                    {name: 'chartHtml', index: 'chartHtml', sortable: false},
                    {name: 'measure', index: 'measure', sortable: false},
                    {name: 'basicMeasureValue', index: 'basicMeasureValue', align: "right", sortable: false},
                    {name: 'oneDayAgoMeasureValue', index: 'oneDayAgoMeasureValue', align: "right", sortable: false},
                    {name: 'oneWeekAgoMeasureValue', index: 'oneWeekAgoMeasureValue', align: "right", sortable: false},
                    {name: 'oneMonthAgoMeasureValue', index: 'oneMonthAgoMeasureValue', align: "right", sortable: false}
                ];
            }

            return columnModels;
        },

        /**
         * 서비스별로 View, Title, XlsName 지정
         * @param svcId
         */
        getSummaryExcel: function (svcId) {
            var summaryExcels;
            if (svcId == 1) {//Tstore
                summaryExcels = {titleName: 'Tstore 요약페이지', xlsName: 'tstore_summary.xls', viewName: 'tstoreExcelView'};
            } else if (svcId == 11) {// 11st
                summaryExcels = {titleName: 'SK11st 요약페이지', xlsName: 'sk11st_summary.xls', viewName: 'sk11stExcelView'};
            } else if (svcId == 4) {// Tmap
                summaryExcels = {titleName: 'Tmap 요약페이지', xlsName: 'tmap_summary.xls', viewName: 'tmapExcelView'};
            } else if (svcId == 6) {// Hoppin
                summaryExcels = {titleName: 'Hoppin 요약페이지', xlsName: 'hoppin_summary.xls', viewName: 'hoppinExcelView'};
            } else if (svcId == 7) {// Tcloud
                summaryExcels = {titleName: 'Tcloud 요약페이지', xlsName: 'tcloud_summary.xls', viewName: 'tcloudExcelView'};
            } else if (svcId == 8) {// Syrup
                summaryExcels = {titleName: 'Syrup 요약페이지', xlsName: 'syrup_summary.xls', viewName: 'syrupExcelView'};
            } else if (svcId == 25) {// OCB
                summaryExcels = {titleName: 'OCB 요약페이지', xlsName: 'ocb_summary.xls', viewName: 'ocbExcelView'};
            } else {// 26 기프티콘
                summaryExcels = {
                    titleName: '기프티콘 요약페이지',
                    xlsName: 'gifticon_summary.xls',
                    viewName: 'gifticonExcelView'
                };
            }
            return summaryExcels;
        },
        /**
         * 기준일 지표 레이블을 표현한다.
         */
        basicMeasureLabel: function (basicMeasureValue) {
            if (basicMeasureValue == null) {
                return '-';
            }
            return basicMeasureValue.numberFormat();
        },

        /**
         * 지표 레이블을 표현한다.
         */
        measureLabel: function (basicMeasureValue, measureValue) {
            if (measureValue == null) {
                return '-';
            }

            if (basicMeasureValue == null || basicMeasureValue == 0 || measureValue == 0) {
                return measureValue.numberFormat();
            }

            var buttonClass, percentageText;
            var percentage = CalcHelper.percentageTwoNumbers(measureValue, basicMeasureValue);

            if (percentage > 0) {
                buttonClass = 'label-primary';
                percentageText = '+' + percentage;
            } else if (percentage < 0) {
                buttonClass = 'label-warning';
                percentageText = percentage;
            } else {
                buttonClass = 'label-default';
                percentageText = '=' + percentage;
            }

            return measureValue.numberFormat() + '<br /><span class="label ' + buttonClass + '">' + percentageText + '%</span>';
        }

    };
});


/**
 *  factories > olapSvc
 */
(factories.lazy || factories).factory('olapSvc', function () {

    /**
     * 현재 선택된 dimension중 x(or y)축에 해당되는 값의 code 리스트(selected:true, axis 값으로 필터링)
     * @param selectedDimensions
     * @param axis
     * @returns {*}
     */
    function filterOutForAxis(selectedDimensions, axis) {
        return _.chain(selectedDimensions).filter(function (item) {
            if (item.selected &&
                item.axis.toLowerCase() === axis.toLowerCase()) {
                return true;
            }
        }).sortBy(function (item) {
            return item.order;
        }).map(function (item) {
            return item.code;
        }).value();
    }

    /**
     * 현재 선택된 dimension 리스트
     * @param dimensions
     * @param measure
     * @returns {*}
     */
    function getSelectedDimensions(dimensions, measure) {
        return _.chain(dimensions.concat(measure)).filter(function (item) {
            return item.selected;
        }).sortBy(function (object) {
            return object.order;
        }).value();
    }

    /**
     * 현재 선택된 dimension code 리스트
     * @param selectedDimensions
     * @returns {*}
     */
    function getSelectedDimensionsCode(selectedDimensions) {
        return _.map(selectedDimensions, function (item) {
            return item.code;
        });
    }

    /**
     * measure, dimension 영문-한글 매핑 사전 만들기
     * @param measure
     * @param dimensions
     * @returns {*}
     */
    function makeDictionary(measure, dimensions) {
        return _.extend(
            _.object(_.map(measure.belongs, function (x) {
                return [x.code, x.name]
            })),
            _.object(_.map(dimensions, function (x) {
                return [x.code, x.name]
            }))
        );
    }

    /**
     * formatting data for pivot table
     * @param datas
     * @param measureBelongs
     * @param selectedDimensionCodes
     * @returns {Array}
     */
    function makePivotData(datas, measureBelongs, selectedDimensionCodes) {
        var populatedData = [];
        _.each(datas, function (data) {
            _.each(_.filter(measureBelongs, function (belong) {
                if (belong.selected)return true;
            }), function (measure, index) {
                var copiedObject;
                if ({}.hasOwnProperty.call(data, measure.code)) {
                    // data의 값을 기준으로 표를 그리기 위한 데이터를 생산한다.
                    copiedObject = angular.extend({
                        'measure': index + '@@' + measure.code,
                        'measureValue': data[measure.code]
                    }, data);
                    populatedData.push(_.pick(copiedObject, selectedDimensionCodes.concat(['measure', 'measureValue'])));
                }
            });
        });
        return populatedData;
    }



    return {
        getSelectedDimensionsCode: function (dimensions, measure) {
            return getSelectedDimensionsCode(getSelectedDimensions(dimensions, measure));
        },
        /**
         * drag area item 순서정렬 및 준비
         * @param dimensions
         * @param measure
         * @returns {*}
         */
        prepareDraggable: function (dimensions, measure) {
            return _.chain(dimensions.concat(measure)).filter(function (item) {
                return item.draggable;
            }).sortBy(function (item) {
                return item.order;
            }).each(function (item, index) {
                // 0 부터 draggable.length로 맞춘다.
                item.order = index;
            }).value();
        },
        /**
         * draw pivot Table
         * @param config
         * @param datas
         * @param measure
         * @param dimensions
         * @param columnWidth - 2015.06.08 공통 columnWidth 추가
         */
        drawPivotTable: function(config, datas, measure, dimensions, columnWidth){

            var selectedDimensions = getSelectedDimensions(dimensions, measure);
            var selectedDimensionCodes = getSelectedDimensionsCode(selectedDimensions);

            $('#'+config.tableId).pivot(
                makePivotData(datas, measure.belongs, selectedDimensionCodes),
                {
                    cols: filterOutForAxis(selectedDimensions, 'x'),
                    rows: filterOutForAxis(selectedDimensions, 'y'),
                    vals: ['measureValue'],
                    aggregator: $.pivotUtilities.aggregators.intSum(['measureValue']),
                    renderer: pivotTableRendererForMergingTableHeader,
                    'rendererOptions': {
                        dictionary : makeDictionary(measure, dimensions)
                    }
                }
            );
            //draw fixed header
            DomHelper.createDynamicHeaderForPivot(config.tableId/*, 'div[ng-controller="' + config.ctrlId + '"]'*/);

            //resize the olap pivot table width at first time
            DomHelper.fitDynamicHeaderForPivot(config.tableId);

            //two-line header make to one-line header.
            DomHelper.makeOneLineDynamicHeader(config.tableId, columnWidth);
        }
    };
});

