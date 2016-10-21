'use strict';

angular.module('App').component('ctas0103Modal', {
    templateUrl: 'partials/ctas0103/ctas0103.html',
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    controller: function (apiSvc, $filter) {
        var self = this;


        self.$onInit = function () {
            self.campaign = self.resolve.campaign;
            self.options = angular.extend({ ocbMktngAgrmtYn: '1' }, self.campaign.targetingInfo);
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
            
            apiSvc.getRegions().then(function (data) {
                self.regions = data;
            });
        };

        self.ok = function () {
            var params = angular.extend({}, self.campaign, self.options);
            params.mergeDt = $filter('date')(params.mergeDt, 'yyyyMMdd');

            self.okPromise = apiSvc.saveCampaignTargetingInfo(params);
            self.okPromise.then(function (data) {
                self.close({ $value: data.value });
            });
        };

        self.selectRegion = function (prefix) {
            var params = {
                hrnkDtlCd: self.options[prefix + 'HjdLgrpCd']
            };

            apiSvc.getRegions(params).then(function (data) {
                self[prefix + 'HjdMgrpCdList'] = data;
            });
        };

    }
});