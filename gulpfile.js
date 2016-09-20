'use strict';

var gulp = require('gulp');
var inject = require('gulp-inject');
var sort = require('gulp-sort');
var angularFilesort = require('gulp-angular-filesort');
var eslint = require('gulp-eslint');
var csslint = require('gulp-csslint');
var mainBowerFiles = require('main-bower-files');


// Inject .js, .css to index.html
gulp.task('inject', function () {
    var target = gulp.src('src/main/webapp/index.html');

    var jsSources = gulp.src(['src/main/webapp/partials/**/*.js', '!src/main/webapp/partials/**/*_test.js']);
    // It's not necessary to read the files (will speed up things), we're only after their paths: 
    var cssSources = gulp.src(['src/main/webapp/partials/**/*.css', 'src/main/webapp/styles/**/*.css'], { read: false });
    var bowerComponents = gulp.src(mainBowerFiles(), { read: false });

    return target.pipe(inject(jsSources.pipe(angularFilesort()), { ignorePath: 'src/main/webapp', addRootSlash: false }))
        .pipe(inject(cssSources.pipe(sort()), { ignorePath: 'src/main/webapp', addRootSlash: false }))
        .pipe(inject(bowerComponents, { name: 'bower', ignorePath: 'src/main/webapp', addRootSlash: false }))
        .pipe(gulp.dest('src/main/webapp'));
});

// Lint Javascript
gulp.task('jslint', function () {
    return gulp.src('src/main/webapp/partials/**/*.js')
        .pipe(eslint())
        .pipe(eslint.format());
});

// Lint CSS
gulp.task('csslint', function () {
    return gulp.src('src/main/webapp/partials/**/*.css')
        .pipe(csslint())
        .pipe(csslint.formatter());
});

gulp.task('default', ['jslint', 'csslint', 'inject']);