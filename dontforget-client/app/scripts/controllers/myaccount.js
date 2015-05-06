'use strict';

(function () {
/**
 * @ngdoc function
 * @name dontforgetApp.controller:MyaccountCtrl
 * @description
 * # MyaccountCtrl
 * Controller of the dontforgetApp
 */
angular.module('dontforgetApp')
  .controller('MyAccountCtrl', ['$scope', '$state', 'Connection', 'User', function ($scope, $state, Connection, User) {
	  $scope.pseudo = "";
	  $scope.pass = "";
	  $scope.mail = "";
	  var lIdUser = null;
	  
	  Connection.me(function (pData) {
		  lIdUser = pData.id;
		  $scope.pseudo = pData.pseudo;
		  $scope.mail = pData.mail;
	  });
	  
	  $scope.submit = function () {
		  User.update($scope.pseudo, $scope.pass, $scope.mail, function () {
			  $state.transitionTo('tasks'); 
		  });
	  };
  }]);
})();