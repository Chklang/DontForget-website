'use strict';
(function() {
	/**
	 * @ngdoc overview
	 * @name dontforgetApp
	 * @description # dontforgetApp
	 * 
	 * Main module of the application.
	 */
	var myApp = angular.module('dontforgetApp', [ 'ngAnimate', 'ngAria',
			'ngCookies', 'ngMessages', 'ngResource', 'ngRoute', 'ngSanitize',
			'ngTouch', 'ui.bootstrap', 'ui.router', 'RestModule',
			'WaitingModule', 'pascalprecht.translate', 'DialogsModule' ]);
	myApp.config(['$stateProvider', '$urlRouterProvider', 'restProvider', '$translateProvider', function($stateProvider, $urlRouterProvider, restProvider,
			$translateProvider) {

		$translateProvider.useLoader('i18nFilerevLoader');
		$translateProvider.preferredLanguage('fr');

		$urlRouterProvider.when("", "/main");
		$urlRouterProvider.when("/", "/main");

		// For any unmatched url, send to /route1
		$urlRouterProvider.otherwise("/");

		$stateProvider.state('main', {
			url : '/main',
			templateUrl : 'views/main.html',
			controller : 'MainCtrl'
		}).state('tasks', {
			url : '/tasks',
			templateUrl : 'views/tasks.html',
			controller : 'TasksCtrl'
		}).state('myaccount', {
			url : '/myaccount',
			templateUrl : 'views/myaccount.html',
			controller : 'MyAccountCtrl'
		});

		restProvider.restPath('/rest');
	}]);
	myApp.factory('i18nFilerevLoader', ['$http', '$q', function($http, $q) {
		return function(options) {
			var deferred = $q.defer();

			$http({
				method : 'GET',
				url : globalLangs[options.key]
			}).success(function(data) {
				deferred.resolve(data);
			}).error(function() {
				deferred.reject(options.key);
			});

			return deferred.promise;
		}
	}]);
	myApp.filter('time', [function() {
		return function(pInput) {
			var days = parseInt(pInput / 86400, 10);
			pInput -= days * 86400;
			var hours = parseInt(pInput / 3600, 10);
			pInput -= hours * 3600;
			var minutes = parseInt(pInput / 60, 10);
			pInput -= minutes * 60;
			var seconds = pInput;
			var lResult = "";
			if (seconds > 0) {
				lResult = "" + seconds + "s"
						+ (lResult == "" ? "" : (" " + lResult));
			}
			if (minutes > 0) {
				lResult = "" + minutes + "m"
						+ (lResult == "" ? "" : (" " + lResult));
			}
			if (hours > 0) {
				lResult = "" + hours + "h"
						+ (lResult == "" ? "" : (" " + lResult));
			}
			if (days > 0) {
				lResult = "" + days + "j"
						+ (lResult == "" ? "" : (" " + lResult));
			}
			return lResult;
		};
	}]);
})();