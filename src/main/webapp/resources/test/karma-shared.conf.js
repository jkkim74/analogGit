// Karma configuration
// Generated on Fri Nov 07 2014 17:48:56 GMT+0900 (KST)

module.exports = function() {
  return {

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '../',


    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine'],


    // list of files / patterns to load in the browser
    files: [
	'js/html5shiv.js',
	'js/respond.min.js',
        'js/jquery-1.10.2.min.js',
        'js/jquery.cookie.js',
        'js/jquery-ui/jquery-ui-1.10.1.custom.min.js',
        'js/bootstrap.min.js',
        'js/jquery.dcjqaccordion.2.7.js',
        'js/jquery.scrollTo.min.js',
        'js/jquery.slimscroll.min.js',
        'js/jquery.nicescroll.js',
        'js/bootstrap-datepicker.js',
        'js/voyager.extends.js',
        'js/moment-with-locales.min.js',
        'angularjs/angular.min.js',
        'angularjs/angular-touch.min.js',
        'angularjs/Scope.SafeApply.js',
	"carousel/dist/requestAnimationFrame.min.js",
	"carousel/dist/angular-carousel.min.js",
        'angularjs/angular-sanitize.min.js',
        'angularjs/angular-ui/angular-ui-router.min.js',
        'angularjs/angular-ui/sortable.js',
        'angularjs/angular-ui/ui-bootstrap-tpls-0.11.0.min.js',
        'angularjs/angular-ui/angular-ui-tree.min.js',
        'angularjs/angular-cookies.min.js',
        'carousel/*/*.js',
        'd3/*/*.js',
        'jqgrid/*/*.js',
        'app/common/controllers/common-controllers.js',
        'app/common/services/common-factory.js',
        'app/common/services/dashboard-factory.js',
        'app/common/services/report-factory.js',
        'app/common/services/favorite-factory.js',
        'app/common/services/admin-factory.js',
        'app/common/services/helpdesk-factory.js',
        'app/common/directives/common-directive.js',
        'app/common/utils/helpers.js',
        'ngtable/ng-table.js',
        'summernote/*/*.js',
        'angular-file-upload/angular-file-upload.js',
        'truncate/truncate.js',
        'app/app.js',
        'ngDialog/js/ngDialog.min.js',
        'ng-infinite-scroll/ng-infinite-scroll.min.js',
        'angular-block-ui/angular-block-ui.min.js',
        'ellipsis/jquery.ellipsis.js' ,
        'underscore/underscore-min.js',
        'underscore/angular-underscore.min.js'
    ],


    // list of files to exclude
    exclude: [
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
    },


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress'],


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    //logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: ['Chrome'],

    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: false
  }
};
