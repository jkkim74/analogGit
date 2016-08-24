'use strict';

var App = angular.module('App', [
  'ngAnimate',
  'ngMessages',
  'ngSanitize',
  'ngFileUpload',
  'ui.bootstrap',
  'ui.router',
  'ui.grid',
  'ui.grid.resizeColumns',
  'ui.grid.selection',
  'ui.grid.exporter',
  'ui.grid.infiniteScroll',
  'ui.validate',
  'angular-loading-bar',
  'toastr'
]);

App.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

  $urlRouterProvider.otherwise('/');

  $stateProvider
    .state('index', {
      url: '',
      views: {
        '@': { templateUrl: 'partials/layout/layout.html' },
        'header@index': { templateUrl: 'partials/layout/header.html', },
        'footer@index': { templateUrl: 'partials/layout/footer.html', },
      },
    })
    .state('index.page', {
      url: '/page/:pageId',
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

}]);