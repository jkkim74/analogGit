var kid = angular.module('kid', ['kid.Directives', 'kid.Factory', 'kid.constants', 'kid.controller', 'nvd3ChartDirectives','Scope.safeApply']);

var directives = angular.module('kid.Directives', []);

var factories = angular.module('kid.Factory', []);
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

var controllers = angular.module('kid.controller', [])
    .controller('common', function ($scope) {
        //To use common controller, main kid's controller defined in kidCtrl.js
    });

/**
 * kid constants
 */
angular.module('kid.constants', []).constant('VERSION', '1.0');

/**
 * kid config run & block
 */
kid.config(function ($httpProvider) {
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
