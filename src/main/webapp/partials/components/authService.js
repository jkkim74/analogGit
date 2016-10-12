'use strict';

angular.module('App')
    .service('authSvc', ['$log', '$q', '$http', '$httpParamSerializer', '$timeout', '$window', '$state', 'toastr', function ($log, $q, $http, $httpParamSerializer, $timeout, $window, $state, toastr) {

        var self = this;

        this.authenticate = function (username, password) {
            var deferred = $q.defer();

            var clientId = '50e81484-2b00-43df-a5cd-37538ec422ef';
            var clientSecret = '52245357-e466-4957-a4c6-5c2ddaa46f6f';

            var params = {
                grant_type: 'password',
                username: username,
                password: password,
                scope: 'all'
            };

            $http.post('/oauth/token', $httpParamSerializer(params),
                {
                    headers: {
                        'Authorization': 'Basic ' + btoa(clientId + ':' + clientSecret),
                        'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'
                    }
                }
            ).then(function (resp) {
                sessionStorage.setItem('access_token', resp.data.access_token);
                return self.userInfo();
            }).then(function () {
                $state.reload();
                deferred.resolve();
            }).catch(function (resp) {
                $log.error(resp);
                if (resp.status == 401) {
                    toastr.error('관리자에게 문의해주세요', '등록되지 않은 사용자');
                } else {
                    toastr.error('로그인 정보를 확인해주세요', '로그인 실패');
                }

                deferred.reject();
            });

            return deferred.promise;
        };

        this.logout = function () {
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

        this.userInfo = function () {
            if (!self.isAuthenticated()) {
                return;
            }

            var deferred = $q.defer();

            $http.get('/auth/me', {
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

        this.isAllowedPage = function (pageId) {
            if (!self.isAuthenticated()) {
                return false;
            }

            // TODO: for CTAS sources. to be removed.
            if (pageId.indexOf("ctas") != -1) {
                return true;
            }

            var userInfo = JSON.parse($window.sessionStorage.getItem('user_info'));

            return userInfo && userInfo.pageList && userInfo.pageList.indexOf(pageId.toUpperCase()) !== -1;
        };

    }]);