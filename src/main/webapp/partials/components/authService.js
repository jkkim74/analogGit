'use strict';

angular.module('app').service('authSvc', ['$log', '$q', '$http', '$httpParamSerializer', '$timeout', '$window', '$state', 'toastr', '$uibModalStack', 'appInfo', 'Idle',
    function ($log, $q, $http, $httpParamSerializer, $timeout, $window, $state, toastr, $uibModalStack, appInfo, Idle) {

        var self = this;

        this.authenticate = function (username, password) {
            var deferred = $q.defer();

            var params = {
                grant_type: 'password',
                username: username,
                password: password,
                scope: 'all'
            };

            $http.post('oauth/token', $httpParamSerializer(params),
                {
                    headers: {
                        'Authorization': 'Basic ' + btoa(appInfo.clientId + ':' + appInfo.clientSecret),
                        'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'
                    }
                }
            ).then(function (resp) {
                sessionStorage.setItem('access_token', resp.data.access_token);
                return self.userInfo(true);
            }).then(function () {
                Idle.watch();

                if (appInfo.entryMenu && !self.isAdmin()) {
                    $state.go('index.menu', { menuId: appInfo.entryMenu.toLowerCase() }, { reload: true });
                } else {
                    $state.reload();
                }

                deferred.resolve();
            }).catch(function (resp) {
                // $log.error(resp);
                if (resp.status == 401) {
                    toastr.error((resp.data && resp.data.error_description) || resp.statusText, resp.status);
                } else {
                    toastr.error('로그인 정보 확인 또는 관리자에게 문의해주세요');
                }

                deferred.reject();
            });

            return deferred.promise;
        };

        this.logout = function () {
            Idle.unwatch();

            $uibModalStack.dismissAll();

            $http.get('api/users/logout', {
                headers: { 'Authorization': 'Bearer ' + self.getAccessToken() }
            });

            $window.sessionStorage.removeItem('access_token');
            $window.sessionStorage.removeItem('user_info');

            $state.go('index.home', null, { reload: true });
        };

        this.isAuthenticated = function () {
            return $window.sessionStorage.getItem('access_token') != null;
        };

        this.getAccessToken = function () {
            return $window.sessionStorage.getItem('access_token');
        };

        this.userInfo = function (login) {
            if (!self.isAuthenticated()) {
                return;
            }

            var deferred = $q.defer();

            $http.get('api/users/me', {
                params: { login: login },
                headers: { 'Authorization': 'Bearer ' + self.getAccessToken() }
            }).then(function (resp) {
                $window.sessionStorage.setItem('user_info', JSON.stringify(resp.data));
                deferred.resolve(resp);
            }, function (resp) {
                self.logout();
                deferred.reject(resp);
            });

            return deferred.promise;
        };

        this.getUserInfo = function () {
            var deferred = $q.defer();

            $timeout(function () {
                if (self.isAuthenticated()) {
                    var userInfo = JSON.parse($window.sessionStorage.getItem('user_info'));
                    deferred.resolve(userInfo);
                } else {
                    deferred.reject();
                }
            }, 0);

            return deferred.promise;
        };

        this.isAdmin = function () {
            if (!self.isAuthenticated()) {
                return false;
            }

            var userInfo = JSON.parse($window.sessionStorage.getItem('user_info'));
            var hasAdmin = false;

            userInfo.authorities.forEach(function (obj) {
                if (obj.authority === 'ROLE_ADMIN')
                    hasAdmin = true;
            });

            return hasAdmin;
        };

        this.isAllowedMenu = function (menuId) {
            if (!self.isAuthenticated()) {
                return false;
            }

            var userInfo = JSON.parse($window.sessionStorage.getItem('user_info'));

            return userInfo && userInfo.menuList && userInfo.menuList.indexOf(menuId.toUpperCase()) !== -1;
        };

    }]);