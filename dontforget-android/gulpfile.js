'use strict';

var gulp = require('gulp');
var imageResize = require('gulp-image-resize');
var parallel = require("concurrent-transform");
var del = require('del');

gulp.task('imClean', [], function (done){
	del(['./res/drawable*'], done);
});
gulp.task('im-hdpi', ['imClean'], function () {
	return gulp.src('./images.src/**/*')
		.pipe(parallel(imageResize({ 
		  width : 72,
		  crop : false,
		  upscale : false
		})), 4)
		.pipe(gulp.dest('./res/drawable-hdpi/'));
});
gulp.task('im-ldpi', ['imClean'], function () {
	return gulp.src('./images.src/**/*')
		.pipe(parallel(imageResize({ 
		  width : 36,
		  crop : false,
		  upscale : false
		})), 4)
		.pipe(gulp.dest('./res/drawable-ldpi/'));
});
gulp.task('im-mdpi', ['imClean'], function () {
	return gulp.src('./images.src/**/*')
		.pipe(parallel(imageResize({ 
		  width : 48,
		  crop : false,
		  upscale : false
		})), 4)
		.pipe(gulp.dest('./res/drawable-mdpi/'));
});
gulp.task('im-xhdpi', ['imClean'], function () {
	return gulp.src('./images.src/**/*')
		.pipe(parallel(imageResize({ 
		  width : 96,
		  crop : false,
		  upscale : false
		})), 4)
		.pipe(gulp.dest('./res/drawable-xhdpi/'));
});
gulp.task('im-xxhdpi', ['imClean'], function () {
	return gulp.src('./images.src/**/*')
		.pipe(parallel(imageResize({ 
		  width : 180,
		  crop : false,
		  upscale : false
		})), 4)
		.pipe(gulp.dest('./res/drawable-xxhdpi/'));
});

gulp.task('images', ['im-hdpi', 'im-ldpi', 'im-mdpi', 'im-xhdpi', 'im-xxhdpi']);

gulp.task('default', ['images']);