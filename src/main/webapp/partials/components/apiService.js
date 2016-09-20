'use strict';

angular.module('App')
    .service('apiService', ["$log", "$q", "$http", "$stateParams", "toastr", function ($log, $q, $http, $stateParams, toastr) {

        function ApiGet(command) {
            return function (params) {
                var deferred = $q.defer();

                $http.get('/api/' + command, {
                    params: angular.extend(params, { pageId: $stateParams.pageId, username: 'test2' })
                }).then(function (resp) {
                    deferred.resolve(resp.data);
                }, function (resp) {
                    $log.error(resp);
                    toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
                    deferred.reject(resp.data);
                });

                return deferred.promise;
            };
        }

        function ApiPost(command) {
            return function (params) {
                var deferred = $q.defer();

                $http.post('/api/' + command, angular.extend(params, { pageId: $stateParams.pageId, username: 'test2' }), {
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    transformRequest: function (obj) {
                        var str = [];
                        for (var p in obj)
                            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                        return str.join("&");
                    }
                }).then(function (resp) {
                    toastr.success((resp.data && resp.data.message) || resp.config.url);
                    deferred.resolve();
                }, function (resp) {
                    $log.error(resp);
                    toastr.error((resp.data && resp.data.message) || resp.config.url, (resp.data && resp.data.code) || (resp.status + ' ' + resp.statusText));
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

    }]);