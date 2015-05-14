'use strict';

(function() {
	/**
	 * @ngdoc function
	 * @name dontforgetApp.controller:IndexCtrl
	 * @description # IndexCtrl Controller of the dontforgetApp
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.controller('IndexCtrl', [ '$scope', '$rootScope', '$modal', '$state', '$translate','$cookieStore',
			'Connection', 'User',
			function($scope, $rootScope, $modal, $state, $translate, $cookieStore, Connection, User) {
				$rootScope.isConnected = null;
				
				//Add translations and language chosen
				$scope.countries = [];
				var lCodeLangWasFound = false;
				for (var code in globalLangs) {
					$scope.countries.push(code);
				}
				
				$scope.language = $cookieStore.get("lang");
				if (!$scope.language) {
					var lBrowserLangs = window.navigator.languages || ['en-US'];
					var lUsedLang = null;
					angular.forEach(lBrowserLangs, function (pElement) {
						if (lUsedLang) {
							return;
						}
						
						var lCurrentLang = transformBrowserLanguage(pElement);
						angular.forEach($scope.countries, function (pSupportedLang) {
							if (pSupportedLang == lCurrentLang) {
								lUsedLang = lCurrentLang;
							}
						});
					});
					
					if (!lUsedLang) {
						//No language supported, use default language
						lUsedLang = 'us';
					}
					
					$scope.language = lUsedLang;
					$cookieStore.put("lang", $scope.language);
				}

				$translate.use($scope.language);
				$scope.changeLanguage = function (pCountry) {
					$scope.language = pCountry;
					$cookieStore.put("lang", $scope.language);
					$translate.use(pCountry);
					
					//To update code lang
					User.update();
				};

				var connect = function() {
					window.modalInstance = $modal.open({
						templateUrl : 'modals/views/login.html',
						controller : 'LoginCtrl'
					});
					window.modalInstance.result.then(function(connectionIsOk) {
						$rootScope.isConnected = true;
						document.getElementById('login_form').submit();
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
					if (pResults != null) {
						$rootScope.isConnected = true;
						if (pResults.codelang) {
							$cookieStore.put("lang", pResults.codelang);
							$scope.language = pResults.codelang;
						}
					} else {
						$rootScope.isConnected = false;
					}
				});
			} ]);
	
	function transformBrowserLanguage(pCodeLang) {
		var lSplitted = pCodeLang.split(/-(.*)/);
		if (lSplitted > 1) {
			return lSplitted[1];
		}
		return null;
	}
	
	function getFirstBrowserLanguage () {
		if (window.navigator.languages) {
			return window.navigator.languages;
		}
		
		return ['en-US'];
	};
})();