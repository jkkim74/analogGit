var factories = angular.module('app.commonFactory');

/**
 *  factories > favorite Service
 */
(factories.lazy || factories).factory('favoriteSvc', function ($http, userSvc, menuSvc) {
    var favoriteServices = [];

    /**
     * 대시보드 codeName 인지 확인한다.
     * @param codeName
     * @returns {boolean}
     */
    function isDashboardCodeName(codeName) {
        return codeName.substr(0, 1) == 'B';
    }

    /**
     * 리포트 codeName 인지 확인한다.
     * @param codeName
     * @returns {boolean}
     */
    function isReportCodeName(codeName) {
        return codeName.substr(0, 1) == 'R';
    }

    return {
        /**
         * username으로 즐겨찾기 목록 조회.
         */
        getFavoritesByUsername: function (onSuccess, onError) {
            var url = '/favorite/list/' + userSvc.getUser().username;
            $http.get(url).then(onSuccess, onError);
        },

        /**
         * 즐겨 찾기 조회
         * 2015.10.13 refactoring cookatrice
         * remove params : serviceCode, categoryCode, menuCode
         * add param : menuId
         */
        getFavorite: function (favorite, onSuccess, onError) {
            $http({
                url: '/favorite/get',
                method: "GET",
                params: {
                    'username': userSvc.getUser().username,
                    'menuId': favorite.menuId,
                    'commonCodeName': favorite.commonCodeName
                }
            }).then(onSuccess, onError);
        },

        /**
         * menu url을 생성한다.
         * @param favorite
         * @returns {string}
         */
        generateMenuUrl: function (favorite) {
            var params = '?categoryCode=' + favorite.categoryCode;
            params += '&menuCode=' + favorite.menuCode;

            if (isDashboardCodeName(favorite.commonCodeName)) {
                return '/dashboard/' + favorite.serviceCode + params;
            }else if (isReportCodeName(favorite.commonCodeName)) {
                return '/reports/' + favorite.serviceCode + params;
            }
        },

        /**
         * 즐겨찾기 추가 (여러건)
         * @param favorites
         * @param onSuccess
         * @param onError
         */
        addFavorites: function (favorites, onSuccess, onError) {
            $http.post('/favorite/adds', favorites).then(onSuccess, onError);
        },

        /**
         * 즐겨찾기 추가 (단건)
         * @param favorite
         * @param onSuccess
         * @param onError
         */
        addFavorite: function (favorite, onSuccess, onError) {
            $http.post('/favorite/add', favorite).then(onSuccess, onError);
        },

        /**
         * 즐겨찾기 제거
         * @param id
         * @param onSuccess
         * @param onError
         */
        deleteFavorite: function (id, onSuccess, onError) {
            $http.delete('/favorite/delete/' + id).then(onSuccess, onError);
        },

        /**
         * 즐겨찾기 서비스 목록을 반환한다.
         * @returns {Array}
         */
        getServices: function () {
            favoriteServices.push(menuSvc.getDashboardService());// 대시보드
            var reportServices = menuSvc.getReportService();// 리포트
            angular.forEach(reportServices, function (obj) {
                // TODO visibleYn 제거 처리 - 권한 개념이 생기면 visibleYn이 필요없음.
                if (obj.deleteYn != 'Y' && obj.visibleYn != 'N') {
                    favoriteServices.push(obj);
                }
            });
            return favoriteServices;
        },

        /**
         * favorite object 생성
         * 2015.10.13 refactoring cookatrice
         * remove favorite params : service/category/menu's each code and name, orderIdx - 7columns
         * add favorite param : menuId
         * @param menuItem
         * @returns {*}
         */
        newFavorite: function (menuItem) {
            var favorite = {
                'commonCodeName': menuItem.menu.commonCodeName,
                'username': userSvc.getUser().username,
                'menuId': menuItem.menu.id
            };
            return favorite;
        }
    };
});
