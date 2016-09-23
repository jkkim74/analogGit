'use strict';

angular.module('App')
    .service('authService', ['$log', '$q', '$http', '$timeout', '$state', 'toastr', function ($log, $q, $http, $timeout, $state, toastr) {

        var self = this;

        this.authenticate = function (username, password) {
            var deferred = $q.defer();

            var clientId = '50e81484-2b00-43df-a5cd-37538ec422ef';
            var clientSecret = '52245357-e466-4957-a4c6-5c2ddaa46f6f';

            $http.post('/oauth/token',
                {
                    username: username,
                    password: password,
                    grant_type: 'password',
                    scope: 'all'
                }, {
                    headers: {
                        'Authorization': 'Basic ' + btoa(clientId + ':' + clientSecret),
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + '=' + encodeURIComponent(obj[p]));
                        return str.join('&');
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
                    toastr.error('등록된 사용자가 아닙니다', '로그인 실패');
                } else {
                    toastr.error('로그인 정보가 틀렸습니다', '로그인 실패');
                }

                deferred.reject();
            });

            return deferred.promise;
        };

        this.logout = function () {
            sessionStorage.removeItem('access_token');
            sessionStorage.removeItem('user_info');
            $state.reload();
        };

        this.isAuthenticated = function () {
            return sessionStorage.getItem('access_token') != null;
        };

        this.getAccessToken = function () {
            return sessionStorage.getItem('access_token');
        };

        this.userInfo = function () {
            var deferred = $q.defer();

            $http.get('/auth/me', {
                headers: { 'Authorization': 'Bearer ' + self.getAccessToken() }
            }).then(function (resp) {
                sessionStorage.setItem('user_info', JSON.stringify(resp.data));
                deferred.resolve(resp);
            }, function (resp) {
                self.logout();
                deferred.reject(resp);
            });

            return deferred.promise;
        };

        this.getUsername = function () {
            var deferred = $q.defer();

            $timeout(function () {
                if (self.isAuthenticated()) {
                    var userInfo = JSON.parse(sessionStorage.getItem('user_info'));
                    deferred.resolve(userInfo.username);
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

            var userInfo = JSON.parse(sessionStorage.getItem('user_info'));
            var hasAdmin = false;

            userInfo.authorities.forEach((obj) => {
                if (obj.authority === 'ROLE_ADMIN')
                    hasAdmin = true;
            });

            return hasAdmin;
        };

    }]);