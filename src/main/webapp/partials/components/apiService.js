'use strict';

angular.module('App').service('apiSvc', ['$log', '$q', '$interval', '$http', '$httpParamSerializer', '$stateParams', 'toastr', 'authSvc', 'Upload',
    function ($log, $q, $interval, $http, $httpParamSerializer, $stateParams, toastr, authSvc, Upload) {

        function ApiGet(command) {
            return function (params) {
                var deferred = $q.defer();
                if (!params) {
                    params = {};
                }

                $http.get(params.url || 'api/' + command, {
                    params: angular.extend({ pageId: $stateParams.pageId.toUpperCase() }, params),
                    headers: { 'Authorization': 'Bearer ' + authSvc.getAccessToken() }
                }).then(function (resp) {
                    deferred.resolve(resp.data);
                }).catch(function (resp) {
                    $log.error(resp);

                    if (resp.status === 401) {
                        toastr.error('로그인이 필요합니다');
                        authSvc.logout();
                    } else {
                        toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
                    }

                    deferred.reject(resp.data);
                });

                return deferred.promise;
            };
        }

        function ApiPost(command, hiddenMethod) {
            return function (params) {
                var deferred = $q.defer();

                var args = angular.extend({ pageId: $stateParams.pageId.toUpperCase() }, params);
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
                        toastr.success((resp.data && resp.data.message) || resp.config.url);
                    }
                    deferred.resolve(resp.data);
                }).catch(function (resp) {
                    $log.error(resp);

                    if (resp.status === 401) {
                        toastr.error('로그인이 필요합니다');
                        authSvc.logout();
                    } else {
                        toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
                    }

                    deferred.reject();
                });

                return deferred.promise;
            };
        }

        this.getMembers = new ApiGet('members');
        this.getMemberInfo = new ApiGet('memberInfo');
        this.getAgreementInfo = new ApiGet('agreementInfo');
        this.getJoinInfoOcbapp = new ApiGet('joinInfoOcbapp');
        this.getJoinInfoOcbcom = new ApiGet('joinInfoOcbcom');
        this.getLastestUsageInfo = new ApiGet('lastestUsageInfo');
        this.getMarketingMemberInfo = new ApiGet('marketingMemberInfo');
        this.getMarketingMemberInfoHistory = new ApiGet('marketingMemberInfoHistory');
        this.getThirdPartyProvideHistory = new ApiGet('thirdPartyProvideHistory');
        this.getCardList = new ApiGet('cardList');
        this.getTransactionHistory = new ApiGet('transactionHistory');
        this.getEmailSendHistory = new ApiGet('emailSendHistory');
        this.getAppPushHistory = new ApiGet('appPushHistory');
        this.getClphnNoDup = new ApiGet('clphnNoDup');
        this.getEmailAddrDup = new ApiGet('emailAddrDup');
        this.sendPts = new ApiPost('sendPts');
        this.createUser = new ApiPost('users');
        this.getUsers = new ApiGet('users');
        this.saveUser = new ApiPost('users/info');
        this.saveAdmin = new ApiPost('users/admin');
        this.saveAccess = new ApiPost('users/access');
        this.extractMemberInfo = new ApiPost('extractMemberInfo');
        this.getExtinctionSummary = new ApiGet('extinctionSummary');
        this.getExtinctionTargets = new ApiGet('extinctionTargets');
        this.noticeExtinction = new ApiPost('noticeExtinction');

        // CTAS
        this.getCampaigns = new ApiGet('campaigns');
        this.saveCampaign = new ApiPost('campaigns');
        this.deleteCampaign = new ApiPost('campaigns', 'delete');
        this.getCampaignTargetingInfo = new ApiGet('campaigns/targeting');
        this.saveCampaignTargetingInfo = new ApiPost('campaigns/targeting/trgt');
        this.downloadCampaignTargeting = new ApiGet('campaigns/{campaignId}/download/{objRegFgCd}');
        this.getCampaignDetail = new ApiGet('campaigns/detail');
        this.saveCampaignDetail = new ApiPost('campaigns/detail');
        this.deleteCampaignDetail = new ApiPost('campaigns/detail', 'delete');
        this.requestTransmission = new ApiPost('requestTransmission');
        this.getRegions = new ApiGet('regions');

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