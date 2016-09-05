'use strict';

App.service('apiService', ["$log", "$q", "$http", "$stateParams", "toastr", function ($log, $q, $http, $stateParams, toastr) {

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
            toastr.error(resp.config.url, ((resp.data && resp.data.code) || resp.status) + ' ' + resp.statusText);
            deferred.reject(resp.data);
        });

        return deferred.promise;
    };

    function ApiGet(command) {
        return function (params) {
            var deferred = $q.defer();

            $http.get('/api/' + command, {
                params: angular.extend(params, { pageId: $stateParams.pageId })
            }).then(function (resp) {
                deferred.resolve(resp.data);
            }, function (resp) {
                $log.error(resp);
                toastr.error(resp.config.url, ((resp.data && resp.data.code) || resp.status) + ' ' + resp.statusText);
                deferred.reject(resp.data);
            });

            return deferred.promise;
        };
    }

    this.getMemberInfo = new ApiGet('memberInfo');
    this.getAgreementInfo = new ApiGet('agreementInfo');
    this.getJoinInfoOcbapp = new ApiGet('joinInfoOcbapp');
    this.getJoinInfoOcbcom = new ApiGet('joinInfoOcbcom');
    this.getLastestUsageInfo = new ApiGet('lastestUsageInfo');
    this.getMarketingMemberInfo = new ApiGet('marketingMemberInfo');
    this.getMarketingMemberInfoHistory = new ApiGet('marketingMemberInfoHistory');
    this.get3rdPartyProvideHistory = new ApiGet('3rdPartyProvideHistory');
    this.getCardList = new ApiGet('cardList');
    this.getTransactionHistory = new ApiGet('transactionHistory');
    this.getEmailSendHistory = new ApiGet('emailSendHistory');
    this.getAppPushHistory = new ApiGet('appPushHistory');

}]);