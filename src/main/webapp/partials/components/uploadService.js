'use strict';

angular.module('App')
    .service('uploadService', ['$log', '$q', '$http', '$stateParams', 'toastr', 'Upload', '$interval', 'authService', function ($log, $q, $http, $stateParams, toastr, Upload, $interval, authService) {

        this.upload = function (params) {
            var deferred = $q.defer();

            if (angular.isDefined(params.file)) {
                Upload.upload({
                    url: '/api/upload',
                    data: angular.extend(params, { pageId: $stateParams.pageId }),
                    headers: { 'Authorization': 'Bearer ' + authService.getAccessToken() }
                }).then(function (resp) {
                    toastr.success(resp.config.data.file.name, resp.data.message);
                    deferred.resolve();
                }, function (resp) {
                    $log.error(resp);
                    toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
                    deferred.reject(resp.data);
                }, function (event) {
                    var progressPercentage = parseInt(100.0 * event.loaded / event.total);
                    $log.debug('progress: ' + progressPercentage + '% ' + event.config.data.file.name);
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
                $http.get('/api/upload', {
                    params: { pageId: $stateParams.pageId },
                    headers: { 'Authorization': 'Bearer ' + authService.getAccessToken() }
                }).then(function (resp) {
                    if (resp.data.value.length > 0) {
                        $interval.cancel(canceler);
                        deferred.resolve(resp.data);
                    }
                }, function (resp) {
                    $interval.cancel(canceler);
                    $log.error(resp);
                    toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
                    deferred.reject(resp.data);
                });
            }

            return deferred.promise;
        };

        this.getUploadProgress = function () {
            var deferred = $q.defer();
            var canceler = $interval(uploadedPreview, 500);

            function uploadedPreview() {
                $http.get('/api/upload', {
                    params: { pageId: $stateParams.pageId, countOnly: true },
                    headers: { 'Authorization': 'Bearer ' + authService.getAccessToken() }
                }).then(function (resp) {
                    deferred.notify(resp.data.totalRecords);

                    if (resp.data.code == 910) {
                        $interval.cancel(canceler);
                        deferred.resolve();
                    }
                }, function (resp) {
                    $interval.cancel(canceler);
                    $log.debug(resp);
                    deferred.reject();
                });
            }

            return deferred.promise;
        };

    }]);