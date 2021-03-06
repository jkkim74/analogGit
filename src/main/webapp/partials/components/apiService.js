'use strict';

angular.module('app').service('apiSvc', ['$log', '$q', '$http', '$httpParamSerializer', '$stateParams', 'toastr', 'authSvc', 'Upload',
    function ($log, $q, $http, $httpParamSerializer, $stateParams, toastr, authSvc, Upload) {

        function ApiGet(command) {
            return function (params) {
                var deferred = $q.defer();
                if (!params) {
                    params = {};
                }

                $http.get(params.url || 'api/' + command, {
                    params: angular.extend({ menuId: $stateParams.menuId.toUpperCase() }, params),
                    headers: { 'Authorization': 'Bearer ' + authSvc.getAccessToken() }
                }).then(function (resp) {
                    deferred.resolve(resp.data);
                }).catch(function (resp) {
                    $log.error(resp);

                    if (resp.status === 401) {
                        toastr.error('로그인이 필요합니다');
                        authSvc.logout();
                    } else {
                        toastr.error((resp.data && resp.data.message) || resp.statusText);
                        // toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
                    }

                    deferred.reject(resp.data);
                });

                return deferred.promise;
            };
        }

        function ApiPost(command, hiddenMethod) {
            return function (params) {
                var deferred = $q.defer();

                var args = angular.extend({ menuId: $stateParams.menuId.toUpperCase() }, params);
                if (hiddenMethod) {
                    args._method = hiddenMethod;
                }

                $http.post('api/' + command, $httpParamSerializer(args), {
                    headers: {
                        'Authorization': 'Bearer ' + authSvc.getAccessToken(),
                        'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'
                    }
                }).then(function (resp) {
                    if (resp.data && resp.data.message) {
                        toastr.success((resp.data && resp.data.message) || resp.statusText);
                    }
                    deferred.resolve(resp.data);
                }).catch(function (resp) {
                    $log.error(resp);

                    if (resp.status === 401) {
                        toastr.error('로그인이 필요합니다');
                        authSvc.logout();
                    } else {
                        toastr.error((resp.data && resp.data.message) || resp.statusText);
                        // toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
                    }

                    deferred.reject();
                });

                return deferred.promise;
            };
        }

        // COMMON
        this.createUser = new ApiPost('users');
        this.getUsers = new ApiGet('users');
        this.saveUser = new ApiPost('users/info');
        this.saveAdmin = new ApiPost('users/admin');
        this.saveAccess = new ApiPost('users/access');
        this.saveMasking = new ApiPost('users/masking');

        //pan0102
        this.getMemberInfo = new ApiGet('memberInfo');
        this.getLastestUsageInfo = new ApiGet('lastestUsageInfo');
        this.getMarketingMemberInfo = new ApiGet('marketingMemberInfo');
        this.getMarketingMemberInfoHistory = new ApiGet('marketingMemberInfoHistory');
        this.getThirdPartyProvideHistory = new ApiGet('thirdPartyProvideHistory');
        this.getCardList = new ApiGet('cardList');
        this.getClphnNoDup = new ApiGet('clphnNoDup');
        this.getEmailAddrDup = new ApiGet('emailAddrDup');

        //pan0104
        this.getExtinctionSummary = new ApiGet('extinctionSummary');
        this.getExtinctionTargets = new ApiGet('extinctionTargets');
        this.noticeExtinction = new ApiPost('noticeExtinction');

        //pan0105
        this.extractMemberInfo = new ApiPost('extractMemberInfo');

        //pan0107
        this.getAgreementInfo = new ApiGet('agreementInfo');
        this.getJoinInfoOcbapp = new ApiGet('joinInfoOcbapp');
        this.getJoinInfoOcbcom = new ApiGet('joinInfoOcbcom');
        this.getEmailSendHistory = new ApiGet('emailSendHistory');
        this.getAppPushHistory = new ApiGet('appPushHistory');
        this.getTransactionHistory = new ApiGet('transactionHistory');

        //pan0108
        this.getMemberLedger= new ApiGet('memberLedger');
        this.getMarketingLedger= new ApiGet('marketingLedger');
        this.getMarketingHistory= new ApiGet('marketingHistory');

        // PANDORA all page
        this.getMembers = new ApiGet('members');
        this.sendPts = new ApiPost('sendPts');
        this.getPandoraLog= new ApiGet('pandoraLog');
        this.pandoraLog = new ApiPost('pandoraLog');
        this.getUploaded = new ApiGet('files');
        this.getQueryCacheTest = new ApiGet('queryCacheTest');
        this.upload = function (params) {
            var deferred = $q.defer();

            if (params.file) {
                Upload.upload({
                    url: params.url || 'api/files',
                    data: angular.extend({ menuId: $stateParams.menuId.toUpperCase() }, params),
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
                        toastr.error((resp.data && resp.data.message) || resp.statusText);
                        // toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
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

    }]);