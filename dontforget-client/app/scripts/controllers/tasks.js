'use strict';

(function() {
	/**
	 * @ngdoc function
	 * @name dontforgetApp.controller:TasksCtrl
	 * @description # TasksCtrl Controller of the dontforgetApp
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.controller('TasksCtrl', ['$scope', '$state', function($scope, $state) {
		if (!$scope.isConnected) {
			$state.transitionTo('main');
		}
	}]);
})();