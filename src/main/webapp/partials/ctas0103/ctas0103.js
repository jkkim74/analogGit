'use strict';

angular.module('App').component('ctas0103Modal', {
    templateUrl: 'partials/ctas0103/ctas0103.html',
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    controller: function (apiSvc) {
        var self = this;

        self.$onInit = function () {
            self.campaign = self.resolve.campaign;
            self.options = angular.extend({}, self.resolve.targetingInfo);
            self.months = [];

            for (var i = 0; i < 7; i++ , month--) {
                var date = new Date();
                date.setDate(1); // first date of month
                date.setMonth(date.getMonth() - i);
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                var day = date.getDate();

                self.months.push(year + ('0' + month).slice(-2) + ('0' + day).slice(-2));
            }
        };

        self.ok = function () {
            var params = angular.extend({ cmpgnId: self.campaign.cmpgnId }, self.options);

            apiSvc.saveCampaignTargetingInfo(params).then(function () {
                self.close({ $value: self.options });
            });
        };
    }
});