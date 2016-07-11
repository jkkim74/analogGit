var sharedConfig = require('./karma-shared.conf');

module.exports = function(config) {
    var conf = sharedConfig();

    conf.files = conf.files.concat([
        //test files
        'angularjs/angular-mocks.js',
        'app/dss/controller/dssAllCtrl.js',
        './test/unit/**/*.js'
    ]);

    config.set(conf);
};
