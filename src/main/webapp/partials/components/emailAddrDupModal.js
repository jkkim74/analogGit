'use strict';

App.component('emailAddrDupModal', {
    templateUrl: 'emailAddrDupModal.html',
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    controller: function (apiService, uiGridConstants) {
        var self = this;

        self.$onInit = function () {
            self.emailAddrDupPromise = apiService.getEmailAddrDup({ mbrId: self.resolve.mbrId });
            self.emailAddrDupPromise.then(function (data) {
                self.gridOptionsEmailAddrDup.data = data;
            });
        };

        self.gridOptionsEmailAddrDup = {
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