'use strict';

angular.module('app').component('emailAddrDupModal', {
    templateUrl: 'partials/common/grid-modal-tpl.html',
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    controller: function (apiSvc, uiGridConstants) {
        var self = this;

        self.title = '중복 이메일주소 보유자';

        self.gridOptions = {
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true }
            ]
        };

        self.$onInit = function () {
            self.gridPromise = apiSvc.getEmailAddrDup({ mbrId: self.resolve.mbrId });
            self.gridPromise.then(function (data) {
                self.gridOptions.data = data;
            });
        };

    }
});