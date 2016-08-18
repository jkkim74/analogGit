'use strict';

var gulp = require('gulp');
var inject = require('gulp-inject');
var mainBowerFiles = require('main-bower-files');

gulp.task('inject', function () {
    var target = gulp.src('src/main/webapp/index.html');
    // It's not necessary to read the files (will speed up things), we're only after their paths: 
    var sources = gulp.src(['src/main/webapp/partials/**/*.js', 'src/main/webapp/partials/**/*.css', '!src/main/webapp/partials/**/*_test.js'], { read: false });
    var bowerComponents = gulp.src(mainBowerFiles(), { read: false });

    return target.pipe(inject(sources, { ignorePath: 'src/main/webapp', addRootSlash: false }))
        .pipe(inject(bowerComponents, { name: 'bower', ignorePath: 'src/main/webapp', addRootSlash: false }))
        .pipe(gulp.dest('src/main/webapp'));
});

gulp.task('default', ['inject']);