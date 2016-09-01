'use strict';

App.service('apiService', ["$log", "$q", "$http", "$stateParams", "toastr", function ($log, $q, $http, $stateParams, toastr) {

    this.getUploadedPreview = function () {
        var deferred = $q.defer();

        $http.get('/api/upload', {
            params: {
                pageId: $stateParams.pageId,
                username: 'test2'
            }
        }).then(function (resp) {
            deferred.resolve(resp.data);
        }, function (resp) {
            $log.error(resp);
            toastr.error((resp.data && resp.data.message) || resp.statusText, (resp.data && resp.data.code) || resp.status);
            deferred.reject(resp.data);
        });

        return deferred.promise;
    };

    this.getMembers = function (offset, limit) {
        var deferred = $q.defer();

        $http.get('/api/mergedMember', {
            params: {
                pageId: $stateParams.pageId,
                username: 'test2',
                offset: offset,
                limit: limit
            }
        }).then(function (resp) {
            deferred.resolve(resp.data);
        }, function (resp) {
            $log.error(resp);
            toastr.error((resp.data && resp.data.message) || resp.statusText, (resp.data && resp.data.code) || resp.status);
            deferred.reject(resp.data);
        });

        return deferred.promise;
    };

}]);