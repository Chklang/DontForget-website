'use strict';

(function() {
	/**
	 * @ngdoc function
	 * @name spacesimperiumApp.controller:LoginCtrl
	 * @description # LoginCtrl Controller of the spacesimperiumApp
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.controller('LoginCtrl', [
			'$scope',
			'$state',
			'User',
			'Connection',
			function($scope, $state, User, Connection) {
				$scope.isNewCompte = false;
				var lLoginIsOK = false;

				$scope.login = angular.element("#fake_login_login").val();
				$scope.password = angular.element("#fake_login_password").val();
				$scope.password2 = "";
				$scope.email = "";
				$scope.action = "";

				$scope.submit = function() {
					if (lLoginIsOK) {
						return true;
					}
					if ($scope.isNewCompte) {
						return $scope.newcompte_submit();
					} else {
						return $scope.login_submit();
					}
				}

				$scope.login_close = function() {
					window.modalInstance.dismiss('cancel');
				}

				$scope.login_submit = function() {
					// window.modalInstance.close();
					if ($scope.login == "") {
						alert("Veuillez entrer un pseudonyme");
						return;
					}
					if ($scope.password == "") {
						alert("Veuillez entrer un mot de passe");
						return;
					}
					Connection.connect($scope.login, $scope.password,
							function() {
								// Connexion OK
								lLoginIsOK = true;
//								$state.transitionTo('tasks');
								angular.element("#fake_login_login").val($scope.login);
								angular.element("#fake_login_password").val($scope.password);
								angular.element("#fake_login_form").submit();
								window.modalInstance.close(true);
							});
					return false;
				}

				$scope.newcompte_submit = function() {
					if ($scope.login == "") {
						alert("Veuillez entrer un pseudonyme");
						return;
					}
					if ($scope.password == "") {
						alert("Veuillez entrer un mot de passe");
						return;
					}
					if ($scope.password2 == "") {
						alert("Veuillez confirmez votre mot de passe");
						return;
					}
					if ($scope.email == "") {
						alert("Veuillez entrer une adresse e-mail");
						return;
					}
					if ($scope.password != $scope.password2) {
						alert("La confirmation du mot de passe est erroné");
						return;
					}
					User.create($scope.login, $scope.password, $scope.email,
							function(pData) {
								lLoginIsOK = true;
//								$scope.action = "/#/game";
								angular.element("#fake_login_login").val($scope.login);
								angular.element("#fake_login_password").val($scope.password);
								angular.element("#fake_login_form").submit();
								window.modalInstance.close(true);
							});
					return false;
				}
				$scope.newcompte_close = $scope.login_close;
				$scope.login_gotonewcompte = function(pForm) {
					$scope.isNewCompte = true;
				};

				$scope.newcompte_gotologin = function() {
					$scope.isNewCompte = false;
				};

			} ]);
})();