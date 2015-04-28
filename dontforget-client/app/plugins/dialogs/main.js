'use strict';

/**
 * @ngdoc overview
 * @name spacesimperiumApp
 * @description # spacesimperiumApp
 * 
 * Main module of the application.
 */
(function() {

	var module = angular.module('DialogsModule', [ 'ui.bootstrap' ], [
			'$compileProvider', function($compileProvider) {
				$compileProvider.directive('compile', function($compile) {
					// directive factory creates a link function
					return function(scope, element, attrs) {
						scope.$watch(function(scope) {
							// watch the 'compile' expression for changes
							return scope.$eval(attrs.compile);
						}, function(value) {
							// when the 'compile' expression changes
							// assign it into the current DOM
							element.html(value);

							// compile the new DOM and link it to the current
							// scope.
							// NOTE: we only compile .childNodes so that
							// we don't get into infinite loop compiling
							// ourselves
							$compile(element.contents())(scope);
						});
					};
				});
			} ]);

	module
			.provider(
					'dialogConfig',
					function () {
						var lTextButtonClose = "Close";
						var lTemplate = '<fieldset>'
								+ '<legend translate>{{title}}</legend>'
								+ '<div translate>{{textContent}}</div>'
								+ '<div compile="htmlContent"></div>'
								+ '<button ng-repeat="button in buttons" ng-click="button.action(api)" translate>{{button.title}}</button>'
								+ '</fieldset>';

						this.setTextButtonClose = function(pValue) {
							lTextButtonClose = pValue;
						};
						this.setTemplate = function(pValue) {
							lTemplate = pValue;
						};

						this.$get = [ function() {
							return {
								textButtonClose : lTextButtonClose,
								template : lTemplate
							};
						} ];
					});

})();