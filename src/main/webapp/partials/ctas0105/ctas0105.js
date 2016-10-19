'use strict';

angular.module('App').component('ctas0105Modal', {
    templateUrl: 'partials/ctas0105/ctas0105.html',
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    controller: function (apiSvc, authSvc) {
        var self = this;

        self.$onInit = function () {
            self.campaignCell = self.resolve.campaignCell;

            authSvc.getUserInfo().then(function (userInfo) {
                self.ptsUsername = userInfo.ptsUsername;
            });
        };

        self.ok = function () {
            self.okPromise = apiSvc.requestTransmission(self.campaignCell);
            self.okPromise.then(function () {
                self.close();
            });
        };

    }
});