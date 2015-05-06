'use strict';

(function() {
	/**
	 * @ngdoc function
	 * @name dontforgetApp.controller:IndexCtrl
	 * @description # IndexCtrl Controller of the dontforgetApp
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.controller('IndexCtrl', [ '$scope', '$rootScope', '$modal', '$state', '$translate',
			'Connection',
			function($scope, $rootScope, $modal, $state, $translate, Connection) {
				$rootScope.isConnected = null;

				var connect = function() {
					window.modalInstance = $modal.open({
						templateUrl : 'modals/views/login.html',
						controller : 'LoginCtrl'
					});
					window.modalInstance.result.then(function(connectionIsOk) {
						$rootScope.isConnected = true;
						angular.element('#login_form').submit();
					});
				};

				var disconnect = function() {
					Connection.disconnect(function() {
						$rootScope.isConnected = false;
						$state.transitionTo('main');
					});
				};
				$scope.login = function() {
					if (!$rootScope.isConnected) {
						connect();
					} else {
						disconnect();
					}
				};

				Connection.me(function(pResults) {
					$rootScope.isConnected = pResults != null;
				});

				
				//Add translations and language chosen
				$scope.countries = [];
				$scope.language = null;
				for (var code in globalLangs) {
					$scope.countries.push(code);
					if ($scope.language == null) {
						$scope.language = code;
					}
				}
				
				$translate.use($scope.language);
				$scope.changeLanguage = function (pCountry) {
					$scope.language = pCountry;
					$translate.use(pCountry);
				};
			} ]);
})();