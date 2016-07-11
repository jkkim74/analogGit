var directives = angular.module('app.commonDirectives');

/**
 * directives > favoriteBar
 */
directives.lazy.directive('favoriteBar', function ($rootScope, $location, $timeout, $window, $log, favoriteSvc) {
    var scope, element;

    function bindEvents() {
        EventBindingHelper.initFavoriteBar(element);
    }

    function init() {
        scope.myFavoritesMap = {};
        favoriteSvc.getFavoritesByUsername(function (result) {
            angular.forEach(result.data, function (obj) {
                var serviceCode = obj.serviceCode;
                if (!this[serviceCode]) this[serviceCode] = [];
                this[serviceCode].push(obj);
            }, scope.myFavoritesMap);
            $timeout(function () {
                bindEvents();
            });
        }, function () {
            $log.debug('Cannot load favorite list.');
        });
    }

    /**
     * 즐겨찾기바 갱신 이벤트 핸들러
     * @param event
     * @param serviceId
     */
    $rootScope.$on('handleUpdateFavoriteBar', function (event, serviceCode) {
        favoriteSvc.getFavoritesByUsername(function (result) {
            // serviceId에 해당하는 부분만 갱신한다.
            scope.myFavoritesMap[serviceCode] = [];
            angular.forEach(result.data, function (obj) {
                if (obj.serviceCode == serviceCode) {
                    scope.myFavoritesMap[serviceCode].push(obj);
                }
            });
            if (scope.myFavoritesMap[serviceCode].length == 0) {
                delete scope.myFavoritesMap[serviceCode];
            }
            $timeout(function () {
                bindEvents();
            });
        }, function () {
            $log.debug('Cannot load favorite list.');
        });
    });

    /**
     * 리포트탭에 들어갈 리포트 메뉴인지 확인한다.
     * @param favorite
     * @returns {boolean|$scope.menuContext.service|*}
     */
    function isReportMenuForTab(favorite) {
        // 현재 보고 있는 페이지의 서비스코드가 동일하면 true
        return favorite.commonCodeName == 'RM' && scope.menuContext.service &&
            scope.menuContext.service.code == favorite.serviceCode;
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/favoriteBar.tpl.html?_=" + DateHelper.timestamp(),
        link: function (s, e) {
            scope = s;
            element = e;

            // 즐겨찾기 초기화
            init();

            /**
             * url을 이동한다.
             * @param favorite
             */
            scope.goUrl = function (favorite) {
                if (isReportMenuForTab(favorite)) {
                    var category = {code: favorite.categoryCode, name: favorite.categoryName};
                    var menu = {code: favorite.menuCode, name: favorite.menuName};

                    if (favorite.categoryCode == 'summary') {
                        category.summaryReportYn = 'Y';
                    }

                    // 리포트 탭 선택
                    scope.selectMenu(category, menu);
                }
                // 해결: Error: inprog Action Already In Progress
                // define the bookmark status
                //$timeout(function() {
                //    angular.element('.bookmark-active').click();
                //});

                /** for favorite dashboard refresh */
                if (favorite.serviceCode == 'bs') {
                    scope.$broadcast('clickFavoriteDashboardItem', favorite);
                }

                // go to url
                //$window.location.href = favoriteSvc.generateMenuUrl(favorite);
                $location.url(favoriteSvc.generateMenuUrl(favorite));
           };

            scope.getFullMenuName = function (favorite) {
                if (favorite.categoryCode == 'summary') {
                    return favorite.categoryName;
                } else {
                    return favorite.categoryName + ' > ' + favorite.menuName;
                }
            };

            /**
             * 즐겨찾기 제거
             * @param favorite
             * @param serviceCode
             */
            scope.delete = function (favorite, serviceCode) {
                favoriteSvc.deleteFavorite(favorite.id,
                    function () {
                        // onSuccess
                        $rootScope.$broadcast('handleUpdateFavoriteBar', serviceCode);
                        $rootScope.$broadcast('handleUpdateFavoriteButton');
                    }, function () {
                        // onError
                        alert('Fail to delete');
                    });
            };

            /**
             * 즐겨찾기 css 변경
             * 2014.11.11 cookatrice
             */
            var w = angular.element($window);
            scope.wHeight = w.height();
            scope.favoriteState = false;

            scope.favoriteCheck = function (state) {
                if (scope.favoriteState) {
                    if (state === 'height') {
                        return {'height': scope.wHeight};
                    } else if (state === 'display') {
                        return {'display': 'block'};
                    }
                } else {
                    if (state === 'height') {
                        return {'height': '50px'};
                    } else if (state === 'display') {
                        return {'display': 'none'};
                    }
                }
            };
            scope.getFavoriteWindow = function () {
                return {
                    'h': w.height(),
                    'w': w.width()
                };
            };

            scope.favoriteStateChange = function(){
                scope.wHeight = w.height() - 50;
                var $$ = angular.element('.bookmark-area .bookmark-active');
                if (scope.favoriteState) {
                    if (w.width() >= 768) {
                        $$.parent().css('margin-top', '-95px');
                    } else {
                        $$.parent().css('margin-top', '-40px');
                    }
                    $$.find('.fa-angle-up').removeClass('fa-angle-down');

                } else {
                    $$.parent().css('margin-top', '-' + scope.wHeight + 'px');
                    $$.find('.fa-angle-up').addClass('fa-angle-down');
                }

                scope.favoriteState = !scope.favoriteState;
            };

            scope.$watch(scope.getFavoriteWindow, function () {
                var bookmarkArea = angular.element('.bookmark-area');
                if (scope.favoriteState) {
                    if (w.width() >= 768) {
                        scope.wHeight = w.height() - 50;
                    } else {
                        scope.wHeight = 40;
                    }
                    bookmarkArea.css('margin-top', '-' + scope.wHeight + 'px');
                } else {
                    if (w.width() >= 768) {
                        bookmarkArea.css('margin-top', '-95px');
                    } else {
                        bookmarkArea.css('margin-top', '-40px');
                    }
                }
            }, true);

        }
    };
});

/**
 * directives > favoriteModal
 */
directives.lazy.directive('favoriteModal', function ($rootScope, $timeout, userSvc, favoriteSvc) {
    var scope, element;
    var $bookmarkModal = null;

    function bindEvents() {
        $bookmarkModal = element.find(".bookmark-modal");
        $bookmarkModal.bookmarkModal();
    }

    function clear() {
        scope.selectedService = null;
        scope.selectedFavorites = [];
        if ($bookmarkModal) {
            $bookmarkModal.modal('hide');
        }
    }

    return {
        restrict: 'E',
        templateUrl: "page/templates/favoriteModal.tpl.html?_=" + DateHelper.timestamp(),
        scope: {
            eventHandler: '&ngClick'
        },
        link: function (s, e) {
            scope = s;
            element = e;

            scope.loading = false;
            scope.selectedService = null;
            scope.selectedFavorites = [];
            scope.services = favoriteSvc.getServices();
            //scope.isReportAdmin = userSvc.isReportAdmin();

            /**
             * 즐겨찾기 추가
             */
            scope.addFavorites = function () {
                if (!scope.selectedFavorites.length) {
                    alert('추가할 즐겨찾기를 선택해주세요.');
                    return;
                }

                scope.loading = true;
                favoriteSvc.addFavorites(scope.selectedFavorites,
                    function () {
                        $rootScope.$broadcast('handleUpdateFavoriteBar', scope.selectedService.code);
                        $rootScope.$broadcast('handleUpdateFavoriteButton');
                        scope.loading = false;
                        clear();
                    }, function () {
                        alert('Error occurs.');
                        scope.loading = false;
                    });
            };

            /**
             * 서비스 변경
             */
            scope.changeService = function () {
                scope.selectedFavorites = [];
            };

            /**
             * 메뉴 아이템 선택
             */
            scope.selectMenuItem = function (category, menu) {
                var isNewItem = true;
                angular.forEach(scope.selectedFavorites, function (obj, index) {
                    // 이미 저장된 아이템은 제거한다.
                    if (category.code == obj.categoryCode && menu.code == obj.menuCode) {
                        isNewItem = false;
                        scope.selectedFavorites.splice(index, 1);
                        return;
                    }
                });
                if (isNewItem) {
                    var menuItem = {
                        service: scope.selectedService,
                        category: category,
                        menu: menu
                    };
                    var favorite = favoriteSvc.newFavorite(menuItem);
                    scope.selectedFavorites.push(favorite);
                }
            };

            scope.doneRender = function () {
                $timeout(function () {
                    bindEvents();
                });
            };

            scope.cancelFavoriteModal = function () {
                clear();
            };
        }
    };
});

/**
 *  directives > favoriteButton
 */
directives.lazy.directive('favoriteButton', function ($rootScope, $state, $stateParams, userSvc, reportSvc, favoriteSvc, bmDashboardSvc) {
    /**
     * 초기화
     * @param $scope
     */
    function init($scope) {
        $scope.favorite = {};
        $scope.addable = true;  // 즐겨찾기 추가 가능 여부 flag
        $scope.addableState = {
            true: {'text': '즐겨찾기추가', 'icon': 'fa-plus'},
            false: {'text': '즐겨찾기해제', 'icon': 'fa-minus'}
        };

        var menuItem = {};
        if ($state.is('main.dashboard')) {
            menuItem = bmDashboardSvc.urlToMenuItem();
        } else {
            menuItem = reportSvc.urlToMenuItem();
        }
        $scope.service = menuItem.service;
        $scope.favorite = favoriteSvc.newFavorite(menuItem);
    }

    /**
     * 버튼 토글
     * @param $scope
     */
    function toggle($scope) {
        if ($scope.addable) {
            favoriteSvc.addFavorite($scope.favorite, function (result) {
                $scope.addable = false;
                $scope.favorite.id = result.data.id;
                $rootScope.$broadcast('handleUpdateFavoriteBar', $scope.service.code); // 즐겨찾기바에 갱신 여부를 알려준다.
            }, null);
        } else {
            favoriteSvc.deleteFavorite($scope.favorite.id, function () {
                $scope.addable = true;
                $rootScope.$broadcast('handleUpdateFavoriteBar', $scope.service.code);
            }, null);
        }
    }

    /**
     * 즐겨찾기 상태 체크
     * @param $scope
     * @param callback
     */
    function checkStatus($scope, callback) {
        favoriteSvc.getFavorite($scope.favorite, function (result) {
            if (result.data.id != null) {
                $scope.favorite.id = result.data.id;
                $scope.addable = false;
            } else {
                $scope.addable = true;
            }

            if ($.isFunction(callback)) {
                callback();
            }
        });
    }

    return {
        restrict: 'E',
        template: '<button class="btn btn-white" data-ng-click="toggle()"><i class="fa {{addableState[addable].icon}}">&nbsp;</i>{{addableState[addable].text}}</button>',
        controller: function ($scope, $element) {
            $element.hide();

            // TODO 임시 권한 체크
//            if (!userSvc.isReportAdmin()) {
//                return;
//            }

            // 초기화
            init($scope);

            // 즐겨찾기 상태 체크
            checkStatus($scope, function () {
                $element.show();
            });

            /**
             * toggle button
             */
            $scope.toggle = function () {
                toggle($scope);
            };

            /**
             * 즐겨찾기버튼 갱신 이벤트 핸들러
             * @param event
             */
            $scope.$on('handleUpdateFavoriteButton', function () {
                checkStatus($scope, null);
            });

            $scope.$on('bmDashboardSearchBoxRefrash', function () {
                var menuItem = bmDashboardSvc.urlToMenuItem();
                $scope.service = menuItem.service;
                $scope.favorite = favoriteSvc.newFavorite(menuItem);
                checkStatus($scope, null);
            });
        }
    };
});
