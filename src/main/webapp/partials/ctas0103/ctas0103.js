'use strict';

angular.module('App')
    .component('ctas0103Modal', {
        templateUrl: 'partials/ctas0103/ctas0103.html',
        bindings: {
            resolve: '<',
            close: '&',
            dismiss: '&'
        },
        controller: function () {
            var self = this;

            self.$onInit = function () {
                self.options = {};
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
                self.close({ $value: self.options });
            };
        }
    });