'use strict';

angular.module('app').value('cgBusyDefaults', {
    message: 'Waiting...'
}).config(['$stateProvider', '$urlRouterProvider', '$provide', '$translateProvider', 'uibDatepickerConfig', 'uibDatepickerPopupConfig', 'IdleProvider',
    function ($stateProvider, $urlRouterProvider, $provide, $translateProvider, uibDatepickerConfig, uibDatepickerPopupConfig, IdleProvider) {

        $urlRouterProvider.otherwise('/');

        $urlRouterProvider.rule(function ($i, $location) {
            var path = $location.path()
            var normalized = path.toLowerCase();
            if (path != normalized) return normalized;
        });

        $stateProvider
            .state('index', {
                url: '',
                abstract: true,
                views: {
                    '@': { templateUrl: 'partials/layout/layout.html' },
                    'header@index': { templateUrl: 'partials/layout/header.html', },
                    'footer@index': { templateUrl: 'partials/layout/footer.html', },
                },
            })
            .state('index.home', {
                url: '/',
                templateUrl: 'partials/home/home.html',
                controller: 'HomeCtrl'
            })
            .state('index.page', {
                url: '/:pageId',
                params: {
                    pageParams: {}
                },
                templateUrl: function ($stateParams) {
                    // 화면ID로 디렉터리, .html, .js 만들어서 하나의 페이지를 구성하는 구조.
                    var pageId = $stateParams.pageId.toLowerCase();
                    return 'partials/' + pageId + '/' + pageId + '.html';
                },
                controllerProvider: function ($stateParams) {
                    return $stateParams.pageId.toUpperCase() + "Ctrl";
                }
            });

        // 화면 전환 시 스크롤 초기화
        $provide.decorator('$uiViewScroll', function () {
            return function (/*uiViewElement*/) {
                window.scrollTo(0, 0);
            };
        });

        // angular-ui-grid default
        $provide.decorator('GridOptions', function ($delegate) {
            var gridOptions;
            gridOptions = angular.copy($delegate);
            gridOptions.initialize = function (options) {
                var initOptions;
                initOptions = $delegate.initialize(options);
                initOptions.enableColumnMenus = false;
                return initOptions;
            };
            return gridOptions;
        });

        // angular-ui-grid-translate
        $translateProvider.preferredLanguage('ko');

        // angular-ui-bootstrap default
        uibDatepickerConfig.showWeeks = false;
        uibDatepickerPopupConfig.clearText = '초기화';
        uibDatepickerPopupConfig.closeText = '닫기';
        uibDatepickerPopupConfig.currentText = '오늘';

        IdleProvider.idle(3600); // 1시간
        IdleProvider.timeout(10);

    }
]).run(['$rootScope', '$state', 'authSvc', 'confirmationPopoverDefaults', '$window', '$uibModal',
    function ($rootScope, $state, authSvc, confirmationPopoverDefaults, $window, $uibModal) {

        $rootScope.$on('$stateChangeStart', function (event, toState, toParams/*, fromState, fromParams, options*/) {
            var needAuth = true;
            if (toState.name === 'index.home') {
                needAuth = false;
            }

            if (needAuth && !authSvc.isAuthenticated()) {
                $state.transitionTo('index.home');
                event.preventDefault();
            }

            if (needAuth && toState.name === 'index.page' && !authSvc.isAllowedPage(toParams.pageId)) {
                $state.transitionTo('index.home');
                event.preventDefault();
            }
        });

        $rootScope.$on('IdleStart', function () {
            if ($rootScope.warningIdle) {
                $rootScope.warningIdle.close();
            }

            $rootScope.warningIdle = $uibModal.open({
                templateUrl: 'partials/common/idle-modal-tpl.html'
            });
        });

        $rootScope.$on('IdleEnd', function () {
            $rootScope.warningIdle.close();
        });

        $rootScope.$on('IdleTimeout', function () {
            authSvc.logout();
        });

        // logout event log
        $window.onbeforeunload = function () {
            // authSvc.logout();
        };

        // check token validity when application started
        authSvc.userInfo();

        // angular-bootstrap-confirm default
        confirmationPopoverDefaults.confirmText = '확인';
        confirmationPopoverDefaults.cancelText = '취소';
        confirmationPopoverDefaults.confirmButtonType = 'primary';

    }
]);