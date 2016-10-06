'use strict';

angular.module('App', [
    'ngAnimate',
    'ngMessages',
    'ngResource',
    'ngSanitize',
    'ngFileUpload',
    'ui.bootstrap',
    'ui.grid',
    'ui.grid.autoResize',
    'ui.grid.cellNav',
    'ui.grid.edit',
    'ui.grid.exporter',
    'ui.grid.infiniteScroll',
    'ui.grid.pagination',
    'ui.grid.resizeColumns',
    'ui.grid.rowEdit',
    'ui.grid.selection',
    'ui.router',
    'ui.validate',
    'pascalprecht.translate',
    'angular-ui-grid-translate',
    'toastr',
    'cgBusy',
    'anim-in-out'
]).value('cgBusyDefaults', {
    message: 'Loading...',
});
