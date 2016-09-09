'use strict';

var App = angular.module('App', [
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
  'ui.validate',
  'toastr',
  'cgBusy',
  'blockUI',
  'anim-in-out'
]);

App.value('cgBusyDefaults', {
  message: 'Loading...',
});

App.config(['$stateProvider', '$urlRouterProvider', '$provide', 'blockUIConfig', function ($stateProvider, $urlRouterProvider, $provide, blockUIConfig ) {

  $urlRouterProvider.otherwise('/');

  $stateProvider
    .state('index', {
      url: '',
      abstract: true,
      views: {
        '@': { templateUrl: 'partials/layout/layout.html' },
        'header@index': { templateUrl: 'partials/layout/header.html', },
        'footer@index': { templateUrl: 'partials/layout/footer.html', },
      },
    })
    .state('index.home', {
      url: '/',
      templateUrl: 'partials/home/home.html',
      controller: 'HomeCtrl'
    })
    .state('index.page', {
      url: '/:pageId',
      templateUrl: function ($stateParams) {
        // 화면ID로 디렉터리, .html, .js 만들어서 하나의 페이지를 구성하는 구조.
        return 'partials/' + $stateParams.pageId + '/' + $stateParams.pageId + '.html';
      },
      controllerProvider: function ($stateParams) {
        // First letter of Controller's name must be capitalized. (expect CamelCase)
        var ctrlName = $stateParams.pageId.charAt(0).toUpperCase() + $stateParams.pageId.substr(1) + "Ctrl";
        return ctrlName;
      }
    });

  $provide.decorator('$uiViewScroll', function ($delegate) {
    return function (uiViewElement) {
      window.scrollTo(0, 0);
    };
  });

  blockUIConfig.message = 'Waiting...';
  blockUIConfig.autoBlock = false;

}]);
