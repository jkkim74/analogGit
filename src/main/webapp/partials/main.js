'use strict';

angular.module('App', [
    'ngAnimate',
    'ngMessages',
    'ngSanitize',
    'ngFileUpload',
    'ui.bootstrap',
    'ui.router',
    'ui.grid',
    'ui.grid.autoResize',
    'ui.grid.resizeColumns',
    'ui.grid.selection',
    'ui.grid.exporter',
    'ui.grid.pagination',
    'ui.grid.infiniteScroll',
    'ui.grid.edit',
    'ui.grid.rowEdit',
    'ui.grid.cellNav',
    'ui.validate',
    'toastr',
    'cgBusy',
    'anim-in-out'
]).value('cgBusyDefaults', {
    message: 'Loading...',
});
