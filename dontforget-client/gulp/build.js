'use strict';

var gulp = require('gulp');
var imageResize = require('gulp-image-resize');
var parallel = require("concurrent-transform");
var rename = require("gulp-rename");

var $ = require('gulp-load-plugins')({
  pattern: ['gulp-*', 'main-bower-files', 'uglify-save-license', 'del']
});

module.exports = function(options) {
  gulp.task('partials', function () {
    return gulp.src([
      options.src + '/**/*'
    ])
	.pipe($.filter(["views/*.html", "modals/**/*.html"]))
      .pipe($.minifyHtml({
        empty: true,
        spare: true,
        quotes: true
      }))
      .pipe($.angularTemplatecache('templateCacheHtml.js', {
        module: 'dontforgetApp'
      }))
      .pipe(gulp.dest(options.tmp + '/partials/'));
  });

  gulp.task('html', ['inject', 'partials'], function () {
    var partialsInjectFile = gulp.src(options.tmp + '/partials/templateCacheHtml.js', { read: false });
    var partialsInjectOptions = {
      starttag: '<!-- inject:partials -->',
      ignorePath: options.tmp + '/partials',
      addRootSlash: false
    };

    var htmlFilter = $.filter('*.html');
    var jsFilter = $.filter('**/*.js');
    var cssFilter = $.filter('**/*.css');
    var assets;

    return gulp.src(options.tmp + '/serve/*.html')
      .pipe($.inject(partialsInjectFile, partialsInjectOptions))
      .pipe(assets = $.useref.assets())
      .pipe($.rev())
      .pipe(jsFilter)
      .pipe($.ngAnnotate())
      .pipe($.uglify({ preserveComments: $.uglifySaveLicense })).on('error', options.errorHandler('Uglify'))
      .pipe(jsFilter.restore())
      .pipe(cssFilter)
      .pipe($.csso())
      .pipe(cssFilter.restore())
      .pipe(assets.restore())
      .pipe($.useref())
      .pipe($.revReplace())
      .pipe(htmlFilter)
      .pipe($.minifyHtml({
        empty: true,
        spare: true,
        quotes: true,
        conditionals: true
      }))
      .pipe(htmlFilter.restore())
      .pipe(gulp.dest(options.dist + '/'))
      .pipe($.size({ title: options.dist + '/', showFiles: true }));
  });

  // Only applies for fonts from bower dependencies
  // Custom fonts are handled by the "other" task
  gulp.task('fonts', function () {
    return gulp.src($.mainBowerFiles())
      .pipe($.filter('**/*.{eot,svg,ttf,woff,woff2}'))
      .pipe($.flatten())
      .pipe(gulp.dest(options.dist + '/fonts/'));
  });

	gulp.task('images', ['imResponsive'], function () {
		return gulp.src([
			options.src + '/images/**/*'
		])
		.pipe($.rev())
		.pipe(gulp.dest(options.dist + '/images'))
		.pipe($.rev.manifest({
			path: 'rev-manifest.js',
			merge: true
		}))
		.pipe(gulp.dest(options.tmp+'/'));
	});
	
	gulp.task('others', ['imClean'], function () {
		return gulp.src([options.src + '/**/*', options.src + '/**/.*'])
		.pipe($.filter(['favicon.ico', 'robots.txt', '.htaccess']))
		.pipe(gulp.dest(options.dist + '/'));
	});

	gulp.task('clean', function (done) {
		$.del([options.dist + '/', options.tmp + '/'], done);
	});
  
	gulp.task('revAssets', ['imResponsive'], function () {
		var filter = $.filter(['i18n/*.json', 'images/**/*', 'styles/**/*.png']);
		return gulp.src(options.src + '/**/*')
			.pipe(filter)
			.pipe($.rev())
			.pipe(gulp.dest(options.dist + '/'))
			.pipe(filter.restore())
			.pipe($.rev.manifest({
				path: 'rev-manifest.js',
				merge: true
			}))
			.pipe(gulp.dest(options.tmp+'/'));
	});
	
	gulp.task('revReplace', ['revAssets', 'images', 'html'], function () {
		var jsFilter = $.filter('**/*.js');
		var manifest = gulp.src(options.tmp + '/rev-manifest.js');
		return gulp.src(options.dist + '/**/*')
			//.pipe(jsFilter)
			.pipe($.revReplace({manifest : manifest}))
			.pipe(gulp.dest(options.dist+'/'));
	});

	gulp.task('imClean', [], function (done){
		$.del([options.src + '/images'], done);
	});
	gulp.task('imIcon', ['imClean'], function () {
		return gulp.src(options.src + '/images.src/**/*')
			.pipe(parallel(imageResize({ 
			  width : 48,
			  crop : false,
			  upscale : false
			})), 4)
			.pipe(rename(function (path) { path.basename += "-icon"; }))
			.pipe(gulp.dest(options.src + '/images'));
	});
	gulp.task('imSmall', ['imClean'], function () {
		return gulp.src(options.src + '/images.src/**/*')
			.pipe(parallel(imageResize({ 
			  width : 480,
			  crop : false,
			  upscale : false
			})), 4)
			.pipe(rename(function (path) { path.basename += "-small"; }))
			.pipe(gulp.dest(options.src + '/images'));
	});
	gulp.task('imLarge', ['imClean'], function () {
		return gulp.src(options.src + '/images.src/**/*')
			.pipe(parallel(imageResize({ 
			  width : 1024,
			  crop : false,
			  upscale : false
			})), 4)
			.pipe(rename(function (path) { path.basename += "-large"; }))
			.pipe(gulp.dest(options.src + '/images'));
	});
	
	gulp.task('imResponsive', ['imIcon', 'imSmall', 'imLarge']);

  gulp.task('build', ['html', 'revReplace', 'fonts', 'others']);
};
