module.exports = function (grunt) {
    grunt.loadNpmTasks('grunt-open');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-shell');
    grunt.loadNpmTasks('grunt-protractor-runner');

    grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
        connect: {
            options: {
                base: 'app/'
            },
            e2eserver: {
                options: {
                    port: 9000,
		    hostname: 'localhost',
                    keepalive: true
                }
            },
            webserver: {
                options: {
                    port: 8888,
                    keepalive: true
                }
            },
            devserver: {
                options: {
                    port: 8888
                }
            },
            testserver: {
                options: {
                    port: 9999
                }
            },
            coverage: {
                options: {
                    base: 'coverage/',
                    port: 5555,
                    keepalive: true
                }
            }
        },

        open: {
            devserver: {
                path: 'http://localhost:8888'
            },
            coverage: {
                path: 'http://localhost:5555'
            }
        },

        karma: {
            unit: {
                configFile: './test/karma-unit.conf.js',
                autoWatch: false,
                singleRun: true
            },
            unit_auto: {
                configFile: './test/karma-unit.conf.js'
            },
            midway: {
                configFile: './test/karma-midway.conf.js',
                autoWatch: false,
                singleRun: true
            },
            midway_auto: {
                configFile: './test/karma-midway.conf.js'
            },
            e2e: {
                configFile: './test/karma-e2e.conf.js',
                autoWatch: false,
                singleRun: true
            },
            e2e_auto: {
                configFile: './test/karma-e2e.conf.js'
            }
        },
        protractor: {
            options: {
                keepAlive: true,
                noColor: false,
                args: {}
            },
            e2e: {
                options: {
                    configFile: './test/protractor.conf.js',
                    args: {}
                }
            }
        },
        shell: {
            options: {
                stdout: true
            },
            selenium: {
                command: "node_modules/protractor/bin/webdriver-manager start",
                options: {
                    stdout: false,
                    async: true
                }
            },
            protractor_install: {
                command: "node_modules/protractor/bin/webdriver-manager update"
            }
        }	
    });

    grunt.registerTask('test', ['connect:testserver', 'karma:unit', 'karma:midway', 'karma:e2e']);
    grunt.registerTask('test:unit', ['karma:unit']);
    grunt.registerTask('test:midway', ['connect:testserver', 'karma:midway']);
    //grunt.registerTask('test:e2e', ['connect:testserver', 'karma:e2e']);
    grunt.registerTask('test:e2e', ['connect:e2eserver', 'protractor:e2e']);

    //keeping these around for legacy use
    grunt.registerTask('autotest', ['autotest:unit']);
    grunt.registerTask('autotest:unit', ['connect:testserver', 'karma:unit_auto']);
    grunt.registerTask('autotest:midway', ['connect:testserver', 'karma:midway_auto']);
    grunt.registerTask('autotest:e2e', ['connect:testserver', 'karma:e2e_auto']);

    //defaults
    grunt.registerTask('default', ['dev']);

    //development
    grunt.registerTask('dev', ['install', 'concat', 'connect:devserver', 'open:devserver', 'watch:assets']);

    //server daemon
    grunt.registerTask('server', ['connect:webserver']);
};
