'use strict';

App.service('uploadService', ["$log", "$q", "$http", "$stateParams", "toastr", "Upload", function ($log, $q, $http, $stateParams, toastr, Upload) {

    this.upload = function (file, columnName) {
        var deferred = $q.defer();

        if (file) {
            Upload.upload({
                url: '/api/upload',
                data: {
                    file: file,
                    pageId: $stateParams.pageId,
                    username: 'test2',
                    columnName: columnName
                }
            }).then(function (resp) {
                toastr.success(resp.config.data.file.name, '업로드 성공');
                deferred.resolve();
            }, function (resp) {
                $log.error(resp);
                toastr.error(resp.statusText, resp.status);
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

}]);