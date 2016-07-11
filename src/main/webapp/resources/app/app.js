var App = angular.module('App', ['ngSanitize','ui.router','ui.sortable','ngCookies','nvd3ChartDirectives', 'ngTable',
    'ui.tree', 'ui.bootstrap','truncate','angular-carousel','angularFileUpload', 'blockUI', 'angular-underscore','infinite-scroll',
    'summernote','app.constants','app.commonDirectives','app.commonFactory','app.commonControllers', 'ngDialog', 'Scope.safeApply']);
var API_BASE_URL = API_BASE_URL || 'https://voyager.skplanet.com';
/**
 * Application constants
 */
angular.module('app.constants', [])
    .constant('APP_VERSION', '1.0')
    .constant('API_BASE_URL', API_BASE_URL)  // via app.jsp
    .constant('DEFAULT_URL', '/index')
    .constant('DATE_TYPE_DAY', 'day')
    .constant('DATE_TYPE_WEEK', 'week')
    .constant('DATE_TYPE_MONTH', 'month')
    .constant('USER_ROLES', {
        bs: 73,
        ocb: 1,
        syrup: 3,
        gifticon: 4,
        tmap: 8,
        tstore: 6,
        sk11st: 5,
        tcloud: 9,
        hoppin: 7,
        dss: 181,
        helpdesk: 182,
        admin: 83
    })
    .constant('REPORT_DATE_TYPES', [
        {key: 'day', label: '일간', sort: 0},
        {key: 'week', label: '주간', sort: 1},
        {key: 'month', label: '월간', sort: 2}
    ])
    .constant('BLE_DATE_TYPES', [
        {key: 'sum', label: '합산'},
        {key: 'day', label: '일간'},
        {key: 'week', label: '주간'},
        {key: 'month', label: '월간'}
    ])
    .constant('DASHBOARD_BM_DATE_TYPES', [
        {key: '30', label: '30일'},
        {key: '90', label: '90일'},
        {key: '180', label: '180일'}
    ])
    .constant('BOSS_SERVICE_ID', {
        ocb: 25,
        syrup: 8,
        gifticon: 26,
        sk11st: 11,
        tstore: 1,
        hoppin: 6,
        tmap: 4,
        tcloud: 7
    })
    .constant('HELPDESK_WORK_REQUEST_TYPES', {
        NEW: 10717,
        MODIFY: 10718,
        ERROR: 10719,
        AUTH: 10720
    })
    .constant('REPORT_TAB_LIMIT_SIZE', 12)
    .constant('MAX_EWMA_SIZE', 4)
    .constant('MENU_SEARCH_OPTION_CALENDAR_TYPE', ["period", "single"])
    .constant('MENU_SEARCH_OPTION_ADD_TYPE', ["select", "text", "mstr"]);

/**
 * App config run & block
 */
App.config(function($urlRouterProvider, $stateProvider, $httpProvider, $logProvider,
                    DEFAULT_URL, ngDialogProvider, blockUIConfig) {
    // log for debug
    $logProvider.debugEnabled(true);

    // layer popup 초기 설정.
    ngDialogProvider.setDefaults({
        className: 'ngdialog-theme-default',
        showClose: true,
        closeByDocument: true,
        closeByEscape: true
    });
    //ajax loading block ui 자동 표시 기능 disable.
    blockUIConfig.autoBlock = false;
    $httpProvider.defaults.headers.common['Cache-Control'] = 'no-cache';
    $httpProvider.defaults.headers.common.Pragma = 'no-cache';
    $httpProvider.defaults.headers.common.Expires = '0';

    // url 베이스로 ui-view를 구성한다.
    $stateProvider
        .state('main', {
            url: '',
            templateUrl: 'page/main.html'
        })
        .state('main.index', {
            url: '/index',
            templateUrl: 'page/main.index.html'
        })
        .state('main.signin', {
            url: '/signin',
            templateUrl: 'page/main.signin.html'
        })
        .state('main.dss', {
            url: '/dss',
            templateUrl: 'page/dss/dss.html',
            controller: 'dssCtrl'
        })
        .state('main.dss.detail', {
            url: '/detail/:dssId',
            templateUrl: 'page/dss/dss.detail.html'
        })
        .state('main.dss.update', {
            url: '/update/:dssId',
            templateUrl: 'page/dss/dss.update.html'
        })
        .state('main.dss.menu', {
            url: '/:menu',
            templateUrl: function ($stateParams){
                return '/page/dss/dss.' + $stateParams.menu + '.html';
            }
        })
        .state('main.reports', {
            url: '/reports/:serviceCode',
            templateUrl: 'page/main.reports.html',
            controller: 'reportCtrl'
        })
        .state('main.helpdesk', {
            url: '/helpdesk/:categoryCode/:menuCode',
            templateUrl: 'page/main.helpdesk.html',
            controller:'helpdeskCtrl'
        })
        .state('main.admin', {
            url: '/admin/:serviceCode',
            templateUrl: 'page/main.admin.html',
            controller: 'adminCtrl'
        })
        .state('main.dashboard', {
            //url: '/dashboard/:categoryCode/:menuCode',
            url: '/dashboard/:serviceCode',
            templateUrl: 'page/main.dashboard.html',
            controller: 'dashboardCtrl'
        })
        .state('error', {
            url: '/error/:errorCode',
            templateUrl: function ($stateParams) {
                return 'page/error/error.' + $stateParams.errorCode + '.html';
            }
        });

    /**
     * ajax response를 hooking 하기 위한 interceptor
     * @param $rootScope
     * @param $q
     * @returns {Function}
     */
    var interceptor = function ($rootScope, $q) {
        function success(response) {
            return response;
        }

        function error(response) {
            var status = response.status;
            var config = response.config;
            var method = config.method;
            var url = config.url;

            // 공통 에러 처리 예외 추가
            // DSS에서는 DSS에서 처리하도록 한다.
            if (url && url.indexOf('/dss') > -1) {
                return false;
            }

            $rootScope.error = method + " on " + url + " failed with status " + status;
            // handling error page
            switch (status) {
                case 404:
                    location.href = '/#/error/404';
                    break;
                case 500:
                    location.href = '/#/error/500';
                    break;
            }

            return $q.reject(response);
        }

        return function (promise) {
            return promise.then(success, error);
        };
    };

    $httpProvider.responseInterceptors.push(interceptor);
    $urlRouterProvider.otherwise(DEFAULT_URL); // 어떤 처리도 되지 못한 경우.
}).run(function ($rootScope, $stateParams, $location, $cookies, $window, userSvc, accessSvc, menuSvc, $log) {
    //20150701 set the main page notice show or not.
    $rootScope.showMainPageNotice = false;
    try {
        var previousLocation = $location.url();
        if (userSvc.isLogin()) {
            var voyagerUser = accessSvc.getVoyagerCookie();
            if (!angular.isUndefined(voyagerUser) && voyagerUser != null) {
                userSvc.setUser(voyagerUser);
                if (userSvc.isLoginRefer()) {
                    previousLocation = decodeURIComponent(userSvc.getLoginReferer());
                    // 브라우저 호환성 및 angular data, event binding과 상관이 없어서 jquery.cookie 모듈을 사용.
                    $.removeCookie('voyager_login_referer');
                }
                if (previousLocation) {
                    $location.url(previousLocation);
                } else {
                    $location.path('/index');
                }
            } else {
                $window.location.href = '/login?returnUrl=' + encodeURIComponent(previousLocation);
            }
        } else {
            $window.location.href = '/login?returnUrl=' + encodeURIComponent(previousLocation);
        }
    } catch(err) {
       $log.debug(err);
       $window.location.href = '/login?returnUrl=';
    }

    /* Reset error and remove cache when a new view is loaded */
    $rootScope.$on('$viewContentLoaded', function () {
        if($location.path() === '/index'){
            $rootScope.mainCssState = true;
        } else {
            $rootScope.mainCssState = false;
        }
        delete $rootScope.error;
    });

    // 작성 중에 옮기는 경우를 방지하기 위해 이벤트 핸들러 등록
    $rootScope.$on('$stateChangeStart', function(event) {
        // dss 자료 작성 중 다른 메뉴로 옮겨가는 경우 한번 더 확인하기
//        if (fromParams && fromParams.menu && fromParams.menu === 'add') {
        if ($rootScope.updating) {
            if (!confirm('작성중인 내용이 있습니다. 정말 다른 메뉴로 이동하겠습니까?')) {
                event.preventDefault();
                return false;
            }
        }
//        }
    });

    // 페이지 새로고침, 페이지닫기시 작성중 상태면 한번 더 확인한다.
    $window.onbeforeunload = function () {
        var message = '작성중인 내용이 있습니다.';
        if ($rootScope.updating) {
            return message;
        }
    };

    // 상태전환이 완료되면 작성중 표시는 해지한다.
    $rootScope.$on('$stateChangeSuccess', function() {
        $rootScope.updating = false;
    });
});
