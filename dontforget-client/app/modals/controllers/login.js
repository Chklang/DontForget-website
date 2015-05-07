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
			'Connection', 'Dialog',
			function($scope, $state, User, Connection, Dialog) {
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
						Dialog.alert("Erreur d'identification", "Veuillez entrer un pseudonyme");
						return;
					}
					if ($scope.password == "") {
						Dialog.alert("Erreur d'identification", "Veuillez entrer un mot de passe").then(function () {
							angular.element("#login_password").focus();
						});
						return;
					}
					Connection.connect($scope.login, $scope.password,
							function() {
								// Connexion OK
								lLoginIsOK = true;
								angular.element("#fake_login_login").val($scope.login);
								angular.element("#fake_login_password").val($scope.password);
								angular.element("#fake_login_form").submit();
								window.modalInstance.close(true);
							});
					return false;
				}

				$scope.newcompte_submit = function() {
					if ($scope.login == "") {
						Dialog.alert("Erreur de création de compte", "Veuillez entrer un pseudonyme");
						return;
					}
					if ($scope.password == "") {
						Dialog.alert("Erreur de création de compte", "Veuillez entrer un mot de passe");
						return;
					}
					if ($scope.password2 == "") {
						Dialog.alert("Erreur de création de compte", "Veuillez confirmez votre mot de passe");
						return;
					}
					if ($scope.email == "") {
						Dialog.alert("Erreur de création de compte", "Veuillez entrer une adresse e-mail");
						return;
					}
					if ($scope.password != $scope.password2) {
						Dialog.alert("Erreur de création de compte", "La confirmation du mot de passe est erroné");
						return;
					}
					User.create($scope.login, $scope.password, $scope.email,
							function(pData) {
								lLoginIsOK = true;
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