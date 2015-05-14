'use strict';

var gulp = require('gulp');
var chug = require( 'gulp-chug' );
var shell = require('gulp-shell');
var del = require('del');

gulp.task( 'client', function () {
    gulp.src( './dontforget-client/gulpfile.js' )
        .pipe( chug( {
            tasks:  [ 'default' ],
        } ) );
} );

gulp.task('server_cleanPublic', function (done) {
	del('./dontforget-server/public/**/*', done);
});
gulp.task('server_copyPublic', ['client', 'server_cleanPublic'], function (done) {
	return gulp.src(['./dontforget-client/dist/**/*', './dontforget-client/dist/**/.*'])
		.pipe(gulp.dest('./dontforget-server/public'));
});

gulp.task('server_build', ['server_copyPublic'], shell.task('activator test dist', {cwd : 'dontforget-server'}));

gulp.task('server', ['server_build']);

gulp.task('default', ['server']);