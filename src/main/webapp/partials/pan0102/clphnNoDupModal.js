'use strict';

angular.module('App')
    .component('clphnNoDupModal', {
        templateUrl: 'clphnNoDupModal.html',
        bindings: {
            resolve: '<',
            close: '&',
            dismiss: '&'
        },
        controller: function (apiSvc, uiGridConstants) {
            var self = this;

            self.$onInit = function () {
                self.clphnNoDupPromise = apiSvc.getClphnNoDup({ mbrId: self.resolve.mbrId });
                self.clphnNoDupPromise.then(function (data) {
                    self.gridOptionsClphnNoDup.data = data;
                });
            };

            self.gridOptionsClphnNoDup = {
                enableColumnMenus: false,
                enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
                flatEntityAccess: true,
                columnDefs: [
                    { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                    { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true }
                ]
            };
        }
    });