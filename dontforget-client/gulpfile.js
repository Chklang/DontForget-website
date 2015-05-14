'use strict';

var gulp = require('gulp');
var gutil = require('gulp-util');
var wrench = require('wrench');
var revReplace = require('gulp-rev-replace');

var options = {
  src: 'app',
  dist: 'dist',
  tmp: '.tmp',
  e2e: 'e2e',
  errorHandler: function(title) {
    return function(err) {
      gutil.log(gutil.colors.red('[' + title + ']'), err.toString());
      this.emit('end');
    };
  },
  wiredep: {
    directory: 'bower_components',
    exclude: [/jquery/, /bootstrap\.js/]
  }
};

wrench.readdirSyncRecursive('./gulp').filter(function(file) {
  return (/\.(js|coffee)$/i).test(file);
}).map(function(file) {
  require('./gulp/' + file)(options);
});


/*
gulp.task('clean', function (done) {
	$.del([options.dist + '/', options.tmp + '/'], done);
});


gulp.task('build', ['html', 'fonts', 'other']);
*/

gulp.task('default', ['clean', 'imClean'], function () {
    gulp.start('build');
});