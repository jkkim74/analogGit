/**
 * Common Controllers
 */
angular.module('app.commonControllers', [])
    .controller('appCtrl', function ($scope, $state, $location, $window, $cookies, menuSvc, userSvc, ngDialog, apiSvc) {
        // 로그인 안된 사용자의 경우 하위로직 수행하지 않고 break시킴.
        $scope.isLogin = userSvc.isLogin();
        if ($scope.isLogin) {
            $scope.logoHref = "main.index";
            $scope.user = userSvc.getUser();
        } else {
            $scope.logoHref = "main.signin";
            return;
        }

        $scope.isAdmin = function () {
            return (menuSvc.getNavigationMenu().adminMenu == 'Y');
        };

        $scope.isMstrSsbiUsers = (menuSvc.getNavigationMenu().ssbiMenu == 'Y');

        $scope.isMstrEduUsers = function() {
            return (menuSvc.getNavigationMenu().ssbiEduMenu == 'Y');
        };

        var mstrCookie = $.cookie("mstr_projects");
        if (mstrCookie) {
            $scope.mstrProjects = $.parseJSON($.cookie("mstr_projects"));
        }
        var link ='http://jira.skplanet.com/secure/Dashboard.jspa?selectPageId=12005';

        /**
         * 대시보드 이동
         */
        $scope.goDashboardMenu = function () {
            var dashboardDefaultUrl = menuSvc.getDashboardDefaultUrl();
            if (dashboardDefaultUrl == '0') {
                swal('등록된 Dashboard 메뉴가 없습니다.');
                return;
            } else {
                $location.url(dashboardDefaultUrl);
            }
       };

        $scope.checkMstrSsbiUsers = function(flag) {
            if (!flag) {
                swal({
                    title: '선택하신 메뉴에 대한 권한이 없습니다.',
                    text: 'Voyager 서비스데스크에서 권한신청을 해주세요.',
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "권한신청",
                    cancelButtonText: "닫기",
                    closeOnConfirm: true
                }, function () {
                    $window.open(link, '_blank');
                });
            }
        };

        /**
         * 리포트 이동
         * @param serviceCode
         */
        $scope.goReportsMenu = function (serviceCode) {
            var reportDefaultUrl = menuSvc.getReportDefaultUrl(serviceCode);
            if (reportDefaultUrl == '0') {
                swal('등록된 리포트가 없습니다.');
                return;
            } else {
                $location.url(reportDefaultUrl);
            }
        };

        /**
         * Admin 메뉴 이동
         */
        $scope.goAdminMenu = function () {
            var adminDefaultUrl = menuSvc.getAdminDefaultUrl();
            if (adminDefaultUrl == '0') {
                swal('권한이 허용된 메뉴가 없습니다.');
                return;
            } else {
                $location.url(adminDefaultUrl);
            }
            //if (userSvc.isEmailAdmin()) {
            //    $location.url('/admin/admin?categoryCode=02&menuCode=0208');
            //} else if (userSvc.isMenuAdmin()) {
            //    $location.url('/admin/admin?categoryCode=02&menuCode=0206');
            //} else if (userSvc.isSsbiAdminUsers()) {
            //    $location.url('/admin/admin?categoryCode=01&menuCode=0105');
            //} else {
            //    swal({
            //        title: '선택하신 메뉴에 대한 권한이 없습니다.',
            //        text: 'Voyager 서비스데스크에서 권한신청을 해주세요.',
            //        showCancelButton: true,
            //        confirmButtonColor: "#DD6B55",
            //        confirmButtonText: "권한신청",
            //        cancelButtonText: "닫기",
            //        closeOnConfirm: true
            //    }, function () {
            //        $window.open(link, '_blank');
            //    });
            //}
        };

        /**
         * Dataset Sharing 이동
         */
        $scope.goDssMenu = function () {
            $state.go('main.dss.menu', {'menu': 'all'});// 추천보고서 잠시 안보이게 저치 2015.11.02
        };

        /**
         * Helpdesk 이동
         */
        $scope.goHelpDeskMenu = function () {
            //menuSvc.getHelpDeskService();
            $location.url(menuSvc.getHelpDeskDefaultUrl());
        };

        /**
         * Self-Service BI 이동
         */
        $scope.goSelfServiceBiMenu = function (projectId) {
            var session = apiSvc.getMstrSession(projectId);
            if (!session || !session.sessionId) {
                //swal("서버와의 통신이 원활하지 않습니다.");
                swal({
                    title: '서버와의 통신이 원활하지 않습니다.',
                    text: '해당 메세지가 지속적으로 반복되면 재로그인 해주세요',
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "로그아웃",
                    cancelButtonText: "닫기",
                    closeOnConfirm: true
                }, function () {
                    $window.location.href = "/logout";
                });
                return false;
            }

            if ($scope.mstrPopup) {
                $scope.mstrPopup.close();
            }
            $scope.mstrPopup = $window.open(session.baseUrl + "?evt=3010&src=mstrWeb.3010&usrSmgr=" + session.sessionId, "_blank");

            //var tmpForm = document.createElement("form");
            //tmpForm.setAttribute("method", "post");
            //tmpForm.setAttribute("action", session.baseUrl + "?evt=3010&src=mstrWeb.3010");
            //tmpForm.setAttribute("target", "_blank");
            //
            //var hiddenField = document.createElement("input");
            //hiddenField.setAttribute("type", "hidden");
            //hiddenField.setAttribute("name", "usrSmgr");
            //hiddenField.setAttribute("value", session.sessionId);
            //tmpForm.appendChild(hiddenField);
            //document.body.appendChild(tmpForm);
            //
            //tmpForm.submit();
            //tmpForm.remove();
        };

        $scope.pop = function () {
            var today = ~~(DateHelper.getCurrentDateByPattern('YYYYMMDD')); // string to integer
            if (today >= 20141201 && today <= 20141231) {
                var userName = $scope.user.username;
                userSvc.getSign(userName).then(function (data) {
                    if (data.length > 0) {
                        var signYn = data[0].signYn;
                        userSvc.getCntSign(userName).then(function (data) {
                            if (data.cnt <= 0) {
                                $scope.signYn = signYn;
                                ngDialog.open({
                                    template: 'page/sign/user.sign.html',
                                    scope: $scope,
                                    controller: 'signCtrl',
                                    className: 'ngdialog-theme-default',
                                    closeByDocument: false,
                                    showClose: false,
                                    closeByEscape: false
                                });
                            }
                        });
                    }
                });
            }
        };

        /**
         * 왼쪽 슬라이드 메뉴 처리.
         * @param item
         */
        $scope.slide = function () {
            DomHelper.toggleSlide();
        };

    })
    .controller('reportCtrl', function ($scope, userSvc, reportSvc, urlHandleSvc, reportTabSvc, accessSvc) {
        // 리포트 탭
        $scope.reportTabs = [];
        // 메뉴 아이템 정보를 담고 있는 메뉴 컨텍스트
        $scope.menuContext = {
            service: {},
            category: {},
            menu: {},
            week: {},
            templateUrl: null
        };

        /**
         * 리포트 메뉴를 로드한다.
         */
        $scope.loadReportMenu = function () {
            reportTabSvc.removeAll();
            $scope.menuContext = reportSvc.urlToMenuItem();
            if ($scope.menuContext)
                addReportTab($scope.menuContext);
        };

        /**
         * activeItem을 리포트 탭에 추가한다.
         */
        function addReportTab(item) {
            // tab 존재 여부 체크
            if (reportTabSvc.isExists(item)) {
                $scope.showReportTab(item);
                return;
            }
            // tab 추가 여부 체크
            if (!reportTabSvc.isAddable()) {
                alert('리포트탭 최대 개수를 초과했습니다.');
                return;
            }
            // 탭을 추가한다.
            reportTabSvc.addTab(item);
            // 리포트탭을 갱신한다.
            $scope.reportTabs = reportTabSvc.getReportTabs();
            // url를 갱신한다.
            //urlHandleSvc.updateMenuUrl(item);
            // 탭을 보여준다.
            $scope.showReportTab(item);

            // 로그를 등록한다.
            accessSvc.createAccessLog();
        }

        /**
         * $viewContentLoaded 이벤트 핸들러
         *
         * 화면 상태전환이나 새로고침을 하는 경우 url 베이스로 리포트를 load한다.
         */
        $scope.$on('$viewContentLoaded', function () {
            $scope.loadReportMenu();
        });

        /**
         * 카테고리를 선택한다.
         * @param category
         */
        $scope.selectCategory = function (category) {
            if (category.summaryReportYn == 'Y') {
                var newItem = reportSvc.newMenuItem(
                    $scope.menuContext.service,
                    category,
                    category.menus[0]);
                addReportTab(newItem);
            }
        };

        /**
         * 메뉴를 선택한다.
         * @param category
         * @param menu
         */
        $scope.selectMenu = function (category, menu) {
            var newItem = reportSvc.newMenuItem(
                $scope.menuContext.service,
                category,
                menu);
            addReportTab(newItem);
        };

        /**
         * 리포트 탭을 제거한다.
         * @param item
         */
        $scope.removeReportTab = function (item) {
            reportTabSvc.removeTab(item);
            var lastItem = reportTabSvc.getLastTab();
            if (lastItem) {
                $scope.showReportTab(lastItem);
            }
        };

        /**
         * 탭을 보여준다.
         * @param item
         */
        $scope.showReportTab = function (item) {
            urlHandleSvc.updateMenuUrl(item);
            $scope.menuContext = reportSvc.urlToMenuItem();
            // MSTR용 처리.
            item.templateUrl = $scope.menuContext.templateUrl;
            reportSvc.triggerClickCategoryMenu(item.category.code);
        };

        /**
         * sidebar 렌더링이 완료되면 scirpt.js 를 바인딩한다.
         */
        $scope.doneRepeatSidebar = function () {
            EventBindingHelper.initReportSidebar();
        };

        /**
         * 노출 여부 체크
         * @param item
         */
        $scope.checkVisible = function (item) {
            if (item.deleteYn == 'Y' || item.visibleYn == 'N' ||
                (item.deleteYn != 'Y' && item.visibleYn == 'P' && item.authority != 'Y')) {
                return false;
            }
            return true;
            //if (item.deleteYn == 'Y') {
            //    return false;
            //}
            //
            //if (item.code == '0902' || item.code == '0906') {
            //    return userSvc.isKhubAdmin();
            //}
            //
            //if (item.id == 673 || item.id == 674 || item.id == 675 ||
            //    item.id == 676 || item.id == 677) {
            //    return userSvc.isTcloudAdmins();
            //}
            //
            //if(item.id == 747) {
            //    return userSvc.isMktPushAdmins();
            //}
            //
            //if(item.id == 99980) {
            //    return userSvc.isTestAdmins();
            //}
            //
            //if (userSvc.isReportAdmin()) {
            //    return true;
            //}
            //
            //if (item.id == 936) {
            //    return userSvc.isOneidAdmin();
            //}
            //
            //if (item.visibleYn == 'N') {
            //    return false;
            //}
            //return true;
        };
    })
    .controller('helpdeskCtrl', function ($scope, $stateParams, $location, helpDeskSvc, userSvc) {
        $scope.categoryCode = $stateParams.categoryCode;
        $scope.menuCode = $stateParams.menuCode;

        //TODO
        $scope.init = function () {
            $scope.helpdeskMenuItems = helpDeskSvc.getHelpDeskMenuItems();
            $scope.loadHelpDeskUrl = 'page/helpdesk/' + $scope.categoryCode + '/' + $scope.menuCode + '.html';
        };

        /**
         * sidebar 렌더링이 완료되면 scirpt.js 를 바인딩한다.
         */
        $scope.doneRepeatSidebar = function () {
            EventBindingHelper.initReportSidebar();
        };

        $scope.selectHelpDeskMenu = function (categoryCode, menuCode) {
            $location.url('/helpdesk/' + categoryCode + '/' + menuCode);
        };

        /**
         * sub menu가 단일일경우, category 클릭시 해당 sub menu로 바로 이동
         * @param category
         */
        $scope.selectCategory = function (category) {
            var categoryCode = category.code;
            var menuCode = category.menus[0].code;
            if (category.summaryReportYn == 'Y') {
                $location.url('/helpdesk/' + categoryCode + '/' + menuCode);
            }
        };

        $scope.checkVisible = function (item) {
            if (item.deleteYn == 'Y' || item.visibleYn == 'N' ||
                (item.deleteYn != 'Y' && item.visibleYn == 'P' && item.authority != 'Y')) {
                return false;
            }
            return true;
            // TODO 임시 helpdesk admin
            //if (userSvc.isHelpDeskAdmin()) {
            //    return true;
            //}
            // TODO visibleYn 제거 처리 - 권한 개념이 생기면 visibleYn이 필요없음.
            //return item.visibleYn != 'N';
        };
    })
    .controller('adminCtrl', function ($scope, $location, $window, adminSvc, userSvc, accessSvc, menuSvc) {
        $scope.menuCode = $location.search()['menuCode'];
        $scope.categoryCode = $location.search()['categoryCode'];

        $scope.init = function () {
            $scope.adminMenuItems = menuSvc.getAdminService();
            showAdminMenuItem();
        };

        function showAdminMenuItem() {
            $location.url('/admin/admin?categoryCode=' + $scope.categoryCode + '&menuCode=' + $scope.menuCode);
            $scope.menuContext = adminSvc.urlToMenuItem();
            var menuAuthority = $scope.menuContext.menu.authority;
            $scope.templateUrl = (menuAuthority == 'Y')
                ? $scope.menuContext.templateUrl : 'page/authority/unauthorized.html';
            //$scope.templateUrl = $scope.menuContext.templateUrl;
            // 로그를 등록한다.
            accessSvc.createAccessLog();
        }

        /**
         * sidebar 렌더링이 완료되면 scirpt.js 를 바인딩한다.
         */
        $scope.doneRepeatSidebar = function () {
            EventBindingHelper.initReportSidebar();
        };

        /** sub menu가 단일일경우, category 클릭시 해당 sub menu로 바로 이동 */
        $scope.selectCategory = function (category) {
            var categoryCode = category.code;
            var menuCode = category.menus[0].code;
            if (category.summaryReportYn == 'Y') {
                $scope.selectAdminMenu(categoryCode, menuCode);
            }
        };

        $scope.selectAdminMenu = function (categoryCode, menuCode) {
            $scope.categoryCode = categoryCode;
            $scope.menuCode = menuCode;
            showAdminMenuItem();
        };

        /**
         * 노출 여부 체크
         * @param item
         */
        $scope.checkVisible = function (item) {
            if ((item.commonCodeId === 9 && item.deleteYn != 'Y') ||
                (item.deleteYn != 'Y' && item.visibleYn != 'N' && item.authority == 'Y')) {
                return true;
            }
            // TODO 임시 관리자 권한 처리 제거, 경영실적 이메일 권한
            //if (userSvc.isEmailAdmin()) {
            //    return true;
            //}
            //if (item.id == 85 || item.id == 93) {
            //    return userSvc.isMenuAdmin();
            //}
            //if (item.id == 84 || item.id == 1069) {
            //    return userSvc.isSsbiAdminUsers();
            //}
            // TODO visibleYn 제거 처리 - 권한 개념이 생기면 visibleYn이 필요없음.
            //if (item.visibleYn === 'N') {
            //    return false;
            //}
            return false;
        };
    })
    .controller('dashboardCtrl', function ($scope, $location, $stateParams, $window, bmDashboardSvc, userSvc, accessSvc, menuSvc, BOSS_SERVICE_ID) {
        $scope.menuCode = $location.search()['menuCode'];
        $scope.categoryCode = $location.search()['categoryCode'];

        $scope.init = function () {
            $scope.dashboardMenuItems = menuSvc.getDashboardService();
            showDashboardMenuItem();
        };

        /** for favorite dashboard refresh */
        $scope.$on('clickFavoriteDashboardItem', function (event, favorite) {
            $scope.selectDashboardMenu(favorite.categoryCode, favorite.menuCode);
        });

        function showDashboardMenuItem() {
            $location.url('/dashboard/bs?categoryCode=' + $scope.categoryCode + '&menuCode=' + $scope.menuCode);
            $scope.menuContext = bmDashboardSvc.urlToMenuItem();
            var menuAuthority = $scope.menuContext.menu.authority;
            $scope.templateUrl = (menuAuthority == 'Y')
                ? 'page/dashboard/' + $scope.categoryCode + "/" + $scope.menuCode + '.html'
                : 'page/authority/unauthorized.html';
            if($scope.categoryCode == 'bc') {
                $scope.templateUrl = (menuAuthority == 'Y')
                    ? 'page/dashboard/bc/bmDashboard.html'
                    : 'page/authority/unauthorized.html';
                $scope.menuContext.bossSvcId = _.values(_.pick(BOSS_SERVICE_ID, $scope.menuCode))[0];
                $scope.$broadcast('bmDashboardSearchBoxRefrash', $scope.menuContext);
            }
            // 로그를 등록한다.
            accessSvc.createAccessLog();
        }

        /** 특정 그래프 막기 */
        function checkBlockChart(chartId) {
            var isBlockFlag = false;
            var blockChartIds = ['#id_29_L112_M001_S001', '#id_29_L116_M001_S001'];
            _.some(blockChartIds, function (element) {
                if(chartId == element){
                    isBlockFlag = true;
                }
                return isBlockFlag;
            });
            return isBlockFlag;
        }

        /** sidebar 렌더링이 완료되면 scirpt.js 를 바인딩한다. */
        $scope.doneRepeatSidebar = function () {
            EventBindingHelper.initDashboard();
        };

        /** sub menu가 단일일경우, category 클릭시 해당 sub menu로 바로 이동 */
        $scope.selectCategory = function (category) {
            var categoryCode = category.code;
            var menuCode = category.menus[0].code;
            if (category.summaryReportYn == 'Y') {
                $scope.selectDashboardMenu(categoryCode, menuCode);
            }
        };

        /** move select menu */
        $scope.selectDashboardMenu = function (categoryCode, menuCode) {
            $scope.categoryCode = categoryCode;
            $scope.menuCode = menuCode;
            showDashboardMenuItem();
        };

        /** 노출 여부 체크 */
        $scope.checkVisible = function (item) {
            if (item.deleteYn == 'Y' || item.visibleYn == 'N' ||
                (item.deleteYn != 'Y' && item.visibleYn == 'P' && item.authority != 'Y')) {
                return false;
            }
            return true;
            //TODO reportAdminIds is top-level. JUST OPEN ALL MENU
            //if (userSvc.isReportAdmin()) {
            //    return true;
            //}
            //return userSvc.isDashboardAdmins(item.id, item.commonCodeId);
        };

        /**
         * 대시보드 차트 그리기
         * @param chartId
         * @param rawData
         * @param trandLine (true : 추세선 그리기, false : 추세선 안그리기)
         * @param period
         * @param extraDataSet (oldDataSet : one-year befor dataSet, past7DayDataSet : 직전7일 UV 데이터)
         */
        $scope.drawChart = function (chartId, rawData, trandLine, period, extraDataSet) {
            var chartData = bmDashboardSvc.makeChartData(rawData);
            var drawData = bmDashboardSvc.separateChartDrawValue(chartData);
            var chartMaxValue = Math.max.apply(Math, drawData);
            var chartMinValue = Math.min.apply(Math, drawData);
            var oldRawData = null;
            var past7DayRawData = null;

            if(!(extraDataSet === undefined || extraDataSet == null)){
                oldRawData = extraDataSet.oldDataSet;
                past7DayRawData = extraDataSet.past7DayDataSet;
                }

            var oldChartData = (oldRawData === undefined || oldRawData == null) ? false : bmDashboardSvc.makeChartData(oldRawData);
            var oldDrawData = null;
            if (oldChartData) {
                oldDrawData = bmDashboardSvc.separateChartDrawValue(oldChartData);
                var tmpMaxValue = Math.max.apply(Math, oldDrawData);
                var tmpMinValue = Math.min.apply(Math, oldDrawData);
                chartMaxValue = (chartMaxValue > tmpMaxValue) ? chartMaxValue : tmpMaxValue;
                chartMinValue = (chartMinValue < tmpMinValue) ? chartMinValue : tmpMinValue;
            }

            var past7DayChartData = (past7DayRawData === undefined || past7DayRawData == null) ? false : bmDashboardSvc.makeChartData(past7DayRawData);
            var past7DayDrawData = null;
            if(past7DayChartData){
                past7DayDrawData = bmDashboardSvc.separateChartDrawValue(past7DayChartData);
                var tmpMaxValue2 = Math.max.apply(Math, past7DayDrawData);
                var tmpMinValue2 = Math.min.apply(Math, past7DayDrawData);
                chartMaxValue = (chartMaxValue > tmpMaxValue2) ? chartMaxValue : tmpMaxValue2;
                chartMinValue = (chartMinValue < tmpMinValue2) ? chartMinValue : tmpMinValue2;
                }

            $(chartId).sparkline(drawData, {
                    height: '156px',
                    width: '100%',
                lineColor: '#5bc0c4',
                    lineWidth: '2',
                    fillColor: false,
                    minSpotColor: false,
                    maxSpotColor: false,
                spotColor: '#5bc0c4',
                    spotRadius: 3,
                    highlightLineColor: '#aaa',
                highlightSpotColor: '#5bc0c4',
//                tooltipFormat: getTooltipFormatter(),
                chartRangeMin: chartMinValue,
                chartRangeMax: chartMaxValue
            });
            if (trandLine) {
                $(chartId).sparkline(bmDashboardSvc.makeTrendlineChartData(drawData), {
                        composite: true,
                        fillColor: false,
                    lineColor: '#CBCBCB',
                    spotColor: '#CBCBCB',
//                    tooltipFormat:'<span style="color: {{color}}">&#9679;</span> {{prefix}}{{y}}{{suffix}}',
                        height: '156px',
                        width: '100%',
                        highlightLineColor: '#ffeeee',
                    highlightSpotColor: '#CBCBCB',
                    chartRangeMin: chartMinValue,
                    chartRangeMax: chartMaxValue
                });
            }
            if (oldChartData && !checkBlockChart(chartId)) {
                $(chartId).sparkline(oldDrawData, {
                        composite: true,
                        fillColor: false,
                    lineColor: '#6E6E6E',
                    spotColor: '#6E6E6E',//E2A9F3
                        height: '156px',
                        width: '100%',
                        highlightLineColor: '#ffeeee',
                    highlightSpotColor: '#CBCBCB',
                    chartRangeMin: chartMinValue,
                    chartRangeMax: chartMaxValue
                });
            }
            if (past7DayChartData) {
                $(chartId).sparkline(past7DayDrawData, {
                        composite: true,
                        fillColor: false,
                        lineColor: '#F19F9F',
                        spotColor: '#F19F9F',
                        height: '156px',
                        width: '100%',
                        highlightLineColor: '#ffeeee',
                    highlightSpotColor: '#CBCBCB',
                    chartRangeMin: chartMinValue,
                    chartRangeMax: chartMaxValue
                });
            }

            /*******************************************************************************
             * When mouse over the sparklinechart, To SHOW select tooltip's date.
             *******************************************************************************/
            $(chartId).bind('sparklineRegionChange', function (ev) {

                function tooltipCurDate(index) {
                    //check for index error
                    if (chartData.length <= index) {
                        return;
                    }
                    var tmpStr = chartData[index][0];
                    var result = '(' + tmpStr.substr(0, 4) + '-' + tmpStr.substr(4, 2) + '-' + tmpStr.substr(6, 2) + ')';
                    if (period === 'week') {
                        result = '(' + tmpStr.substr(0, 4) + '.' + tmpStr.substr(4, 2) + '-' + tmpStr.substr(6, 2) + '주차)';
                    }
                    return result;
            }

                var sparkline = ev.sparklines[0],
                    region = sparkline.getCurrentRegionFields();
                if (region.x != undefined) {
                    $(chartId + 'CurDate').text(tooltipCurDate(region.x));
                    }
            }).bind('mouseleave', function () {
                $(chartId + 'CurDate').text('');
                });

            $scope.curChartTrandlineslp = bmDashboardSvc.chartTrandlineslp;
        };


        /**
         * 대시보드 차트 그리기
         * @param chartId
         * @param rawData
         * @param trandLine (true : 추세선 그리기, false : 추세선 안그리기)
         * @param period
         * @param extraDataSet (oldDataSet : one-year befor dataSet, past7DayDataSet : 직전7일 UV 데이터)
         */
        $scope.drawOCCustomChart = function (chartId, rawData, trandLine, period, extraDataSet) {

            function getSparkData( drawOption ) {
                if ( !drawOption.drawRawData ) {
                    return null;
                }

                var chartData = bmDashboardSvc.makeChartData(drawOption.drawRawData);
                var drawData = bmDashboardSvc.separateChartDrawValue(chartData);
                var tmpMaxValue = Math.max.apply(Math, drawData);
                var tmpMinValue = Math.min.apply(Math, drawData);

                var chartValue = drawOption.chartValue || { min: 0, max: 0 };
                chartValue.max = (chartValue.max > tmpMaxValue) ? chartValue.max : tmpMaxValue;
                chartValue.min = (chartValue.min < tmpMinValue) ? chartValue.min : tmpMinValue;

                return $.extend( drawOption, { chartData: chartData, drawData: drawData, chartValue: chartValue});
            };

            function drawSparkLine( drawOption ) {
                $(drawOption.viewId).sparkline(drawOption.drawData, $.extend(true, drawOption.chartDrawOption,
                    { chartRangeMin: drawOption.chartValue.min, chartRangeMax: drawOption.chartValue.max }
                ));

                if (drawOption.trandLine) {
                    $(drawOption.viewId).sparkline(bmDashboardSvc.makeTrendlineChartData(drawOption.drawData), {
                        composite: true,
                        fillColor: false,
                        lineColor: '#CBCBCB',
                        spotColor: '#CBCBCB',
                        height: '156px',
                        width: '100%',
                        highlightLineColor: '#ffeeee',
                        highlightSpotColor: '#CBCBCB',
                        chartRangeMin: drawOption.chartValue.min,
                        chartRangeMax: drawOption.chartValue.max
                    });
                }
                if (drawOption.chartBind) {
                    /*******************************************************************************
                     * When mouse over the sparklinechart, To SHOW select tooltip's date.
                     *******************************************************************************/
                    $(drawOption.viewId).bind('sparklineRegionChange', function (ev) {
                        var tooltipCurDate = function(index) {
                            //check for index error
                            if (drawOption.chartData.length <= index) {
                                return;
                            }
                            var tmpStr = drawOption.chartData[index][0];
                            var result = '(' + tmpStr.substr(0, 4) + '-' + tmpStr.substr(4, 2) + '-' + tmpStr.substr(6, 2) + ')';
                            if (period === 'week') {
                                result = '(' + tmpStr.substr(0, 4) + '.' + tmpStr.substr(4, 2) + '-' + tmpStr.substr(6, 2) + '주차)';
                            }
                            return result;
                        };
                        var sparkline = ev.sparklines[0],
                            region = sparkline.getCurrentRegionFields();
                        if (region.x != undefined) {
                            $(drawOption.viewId + 'CurDate').text(tooltipCurDate(region.x));
                        }
                    }).bind('mouseleave', function () {
                        $(drawOption.viewId + 'CurDate').text('');
                    });
                }
            };

            // #531D7B
            // #8EDC8D
            // #5bc0c4
            // #786CB5
            // #B990EF
            var arraySparkLineData = [];

            arraySparkLineData.push( getSparkData({
                viewId: chartId,
                drawRawData: rawData,
                chartValue: null,
                trandLine: false,
                chartBind: true,
                chartDrawOption: {
                    height: '156px',
                    width: '100%',
                    lineColor: '#786CB5',
                    lineWidth: '2',
                    fillColor: false,
                    minSpotColor: false,
                    maxSpotColor: false,
                    spotColor: '#786CB5',
                    spotRadius: 3,
                    highlightLineColor: '#aaa',
                    highlightSpotColor: '#5bc0c4'
                }
            }) );

            if (extraDataSet && extraDataSet.oldDataSet && !checkBlockChart(chartId)) {
                arraySparkLineData.push( getSparkData({
                    viewId: chartId,
                    drawRawData: extraDataSet.oldDataSet,
                    chartValue: {
                        max: arraySparkLineData[arraySparkLineData.length - 1].chartValue.max,
                        min: arraySparkLineData[arraySparkLineData.length - 1].chartValue.min
                    },
                    chartDrawOption: {
                        composite: true,
                        fillColor: false,
                        lineWidth: '1',
                        lineColor: '#6E6E6E',
                        spotColor: '#6E6E6E',//E2A9F3
                        height: '156px',
                        width: '100%',
                        highlightLineColor: '#ffeeee',
                        highlightSpotColor: '#CBCBCB'
                    }
                }) );
            }

            if (extraDataSet && extraDataSet.average7DayDataSet) {
                arraySparkLineData.push( getSparkData({
                    viewId: chartId,
                    drawRawData: extraDataSet.average7DayDataSet,
                    chartValue: {
                        max: arraySparkLineData[arraySparkLineData.length - 1].chartValue.max,
                        min: arraySparkLineData[arraySparkLineData.length - 1].chartValue.min
                    },
                    chartDrawOption: {
                        composite: true,
                        fillColor: false,
                        lineWidth: '1',
                        lineColor: '#F19F9F',
                        spotColor: '#F19F9F',
                        height: '156px',
                        width: '100%',
                        highlightLineColor: '#ffeeee',
                        highlightSpotColor: '#CBCBCB'
                    }
                }) );
            }

            if (extraDataSet && extraDataSet.average30DayDataSet) {
                arraySparkLineData.push( getSparkData({
                    viewId: chartId,
                    drawRawData: extraDataSet.average30DayDataSet,
                    chartValue: {
                        max: arraySparkLineData[arraySparkLineData.length - 1].chartValue.max,
                        min: arraySparkLineData[arraySparkLineData.length - 1].chartValue.min
                    },
                    chartDrawOption: {
                        composite: true,
                        fillColor: false,
                        lineWidth: '1',
                        lineColor: '#F19F9F',
                        spotColor: '#F19F9F',
                        height: '156px',
                        width: '100%',
                        highlightLineColor: '#ffeeee',
                        highlightSpotColor: '#CBCBCB'
                    }
                }) );
            }

            var chartMaxMinValue = arraySparkLineData[ arraySparkLineData.length - 1].chartValue;
            for ( var i in arraySparkLineData ) {
                arraySparkLineData[i].chartValue = chartMaxMinValue;
                drawSparkLine( arraySparkLineData[i] );
            }
            $scope.curChartTrandlineslp = bmDashboardSvc.chartTrandlineslp;
        };
    })
    .controller('signCtrl', function ($scope, $stateParams, $location, userSvc, ngDialog, $timeout) {
        $scope.init = function () {
            userSvc.getSign($scope.user.username).then(function (data) {
                if (data.length > 0) {
                    $scope.signUserNm = data[0].userNm;
                    $scope.signUserOrgNm = data[0].corgNm;
                    var dateSeparatorStr = DateHelper.getCurrentDateByPattern('YYYYMMDD');
                    if (data[0].signDt) {
                        var signDate = data[0].signDt.substr(6, 4) + data[0].signDt.substr(0, 2) + data[0].signDt.substr(3, 2);
                        $scope.pnetSignYYYY = signDate.substr(0, 4);
                        $scope.pnetSignMM = signDate.substr(4, 2);
                        $scope.pnetSignDD = signDate.substr(6, 2);
                    }
                    $scope.signYYYY = dateSeparatorStr.substr(0, 4);
                    $scope.signMM = dateSeparatorStr.substr(4, 2);
                    $scope.signDD = dateSeparatorStr.substr(6, 2);
                    $scope.signLoginId = data[0].loginId;
                    $scope.signLoginIdIn = data[0].userId;
                    $scope.signUserNmView = data[0].userNm;
                    $scope.signLoginIdInView = data[0].userId;
                    $scope.signUserOrgNmView = data[0].orgNm;
                }
            });
        };
        $scope.createSign = function () {
            if ($scope.signLoginIdIn == undefined || $scope.signLoginIdIn == "") {
                $scope.ngdialogTitle = "사번을 입력하세요.";
                ngDialog.open({
                    template: '/page/popup/ngdialog.alert.html', scope: $scope
                });
                return;
            }
            if ($scope.signUserId != $scope.signLoginIdIn) {
                $scope.ngdialogTitle = "자신의 사번을 정확히 입력해 주세요.";
                ngDialog.open({
                    template: '/page/popup/ngdialog.alert.html', scope: $scope
                });
                return;
            }
            $scope.ngdialogTitle = "고객정보보호서약서에 동의하십니까?";
            ngDialog.openConfirm({
                template: '/page/popup/ngdialog.confirm.html', className: 'ngdialog-theme-default', scope: $scope
            }).then(function () {
                userSvc.createSign($scope.user.username).then(function (data) {
                    if (data > 0) {
                        ngDialog.close();
                        $scope.ngdialogTitle = "서약처리되었습니다.";
                        var dialog = ngDialog.open({
                            template: '/page/popup/ngdialog.alert.html', scope: $scope
                        });
                        $timeout(function () {
                            dialog.close();
                        }, 1000);
                    }
                });
            }, function () {
                $scope.ngdialogTitle = "취소하셨습니다.";
                ngDialog.open({
                    template: '/page/popup/ngdialog.alert.html', scope: $scope
                });
            });
        };
    });
