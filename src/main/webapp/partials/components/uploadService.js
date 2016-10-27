'use strict';

angular.module('App').service('uploadSvc', ['$log', '$q', '$http', '$stateParams', 'toastr', 'Upload', '$interval', 'authSvc',
    function ($log, $q, $http, $stateParams, toastr, Upload, $interval, authSvc) {

        this.upload = function (params) {
            var deferred = $q.defer();

            if (params.file) {
                Upload.upload({
                    url: params.url || 'api/files',
                    data: angular.extend({ pageId: $stateParams.pageId.toUpperCase() }, params),
                    headers: { 'Authorization': 'Bearer ' + authSvc.getAccessToken() }
                }).then(function (resp) {
                    toastr.success(resp.config.data.file.name, resp.data.message);
                    deferred.resolve(resp.data);
                }, function (resp) {
                    $log.error(resp);

                    if (resp.status === 401) {
                        toastr.error('로그인이 필요합니다');
                        authSvc.logout();
                    } else {
                        toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
                    }

                    deferred.reject(resp.data);
                }, function (event) {
                    var progressPercentage = parseInt(100.0 * event.loaded / event.total);
                    // $log.debug('progress: ' + progressPercentage + '% ' + event.config.data.file.name);
                    deferred.notify(progressPercentage);
                });
            } else {
                toastr.error('유효하지 않은 파일입니다');
                deferred.reject();
            }

            return deferred.promise;
        };

        this.getUploadedPreview = function () {
            var deferred = $q.defer();
            var canceler = $interval(uploadedPreview, 500);

            function uploadedPreview() {
                $http.get('api/files', {
                    params: { pageId: $stateParams.pageId.toUpperCase() },
                    headers: { 'Authorization': 'Bearer ' + authSvc.getAccessToken() }
                }).then(function (resp) {
                    if (resp.data.value.length > 0) {
                        $interval.cancel(canceler);
                        deferred.resolve(resp.data);
                    }
                }).catch(function (resp) {
                    $interval.cancel(canceler);
                    $log.error(resp);

                    if (resp.status === 401) {
                        toastr.error('로그인이 필요합니다');
                        authSvc.logout();
                    } else {
                        toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
                    }

                    deferred.reject(resp.data);
                });
            }

            return deferred.promise;
        };

        this.getUploadProgress = function () {
            var deferred = $q.defer();
            var canceler = $interval(uploadedPreview, 500);

            function uploadedPreview() {
                $http.get('api/files', {
                    params: { pageId: $stateParams.pageId.toUpperCase(), countOnly: true },
                    headers: { 'Authorization': 'Bearer ' + authSvc.getAccessToken() }
                }).then(function (resp) {
                    deferred.notify(resp.data.totalItems);

                    if (resp.data.code == 910) {
                        $interval.cancel(canceler);
                        deferred.resolve();
                    }
                }).catch(function (resp) {
                    $interval.cancel(canceler);
                    $log.debug(resp);

                    if (resp.status === 401) {
                        toastr.error('로그인이 필요합니다');
                        authSvc.logout();
                    } else {
                        toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
                    }

                    deferred.reject();
                });
            }

            return deferred.promise;
        };

    }]);