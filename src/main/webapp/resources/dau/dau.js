var DAU = angular.module('DAU', ['DAU.Directives', 'DAU.Factory', 'dau.constants', 'dau.controller', 'nvd3ChartDirectives','Scope.safeApply']);
var directives = angular.module('DAU.Directives', []);
var factories = angular.module('DAU.Factory', []);
factories.factory('apiSvc', function ($http, $q) {
    return {
        getCommonApi: function (url) {
            return $http.get(url).then(function (response) {
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
        }
    };
});

var controllers = angular.module('dau.controller', [])
    .controller('common', function ($scope) {
        //To use common controller, main DAU's controller defined in dauCtrl.js
    });

/**
 * DAU constants
 */
angular.module('dau.constants', []).constant('VERSION', '1.0');

/**
 * DAU config run & block
 */
DAU.config(function ($httpProvider) {
    $httpProvider.defaults.headers.common['Cache-Control'] = 'no-cache';
    $httpProvider.defaults.headers.common.Pragma = 'no-cache';
    $httpProvider.defaults.headers.common.Expires = '0';
    //something for config()

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


            $rootScope.error = method + " on " + url + " failed with status " + status;
            return $q.reject(response);
        }

        return function (promise) {
            return promise.then(success, error);
        };
    }
}).run(function ($rootScope) {
    //something for run()
    /* Reset error and remove cache when a new view is loaded */
    $rootScope.$on('$viewContentLoaded', function () {
        delete $rootScope.error;
    });
});
