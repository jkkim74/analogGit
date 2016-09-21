'use strict';

angular.module('App')
    .service('authService', ['$log', '$q', '$http', '$stateParams', 'toastr', function ($log, $q, $http, $stateParams, toastr) {

        this.isAuthenticated = function () {
            return sessionStorage.getItem('access_token') != null;
        };

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

                return $http.get('/auth/me', {
                    headers: { 'Authorization': 'Bearer ' + resp.data.access_token }
                });
            }, function (resp) {
                $log.error(resp);
                toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
                deferred.reject();
            }).then(function (resp) {
                sessionStorage.setItem('user_info', JSON.stringify(resp.data));

                deferred.resolve();
            });

            return deferred.promise;
        };

        this.logout = function () {
            sessionStorage.removeItem('access_token');
        };

        this.getAccessToken = function () {
            return sessionStorage.getItem('access_token');
        };

        this.getUsername = function () {
            var userInfo = JSON.parse(sessionStorage.getItem('user_info'));
            return userInfo.username;
        };

    }]);