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
						Dialog.alert("dontforget.login.login_error.login.title", "dontforget.login.login_error.login.text").then(function () {
							angular.element("#login_login").focus();
						});
						return;
					}
					if ($scope.password == "") {
						Dialog.alert("dontforget.login.login_error.password.title", "dontforget.login.login_error.password.text").then(function () {
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
						Dialog.alert("dontforget.login.newaccount_error.login.title", "dontforget.login.newaccount_error.login.text").then(function () {
							angular.element("#login_login").focus();
						});
						return;
					}
					if ($scope.password == "") {
						Dialog.alert("dontforget.login.newaccount_error.password.title", "dontforget.login.newaccount_error.password.text").then(function () {
							angular.element("#login_password").focus();
						});
						return;
					}
					if ($scope.password2 == "") {
						Dialog.alert("dontforget.login.newaccount_error.password2.title", "dontforget.login.newaccount_error.password2.text").then(function () {
							angular.element("#login_password2").focus();
						});
						return;
					}
					if ($scope.email == "") {
						Dialog.alert("dontforget.login.newaccount_error.email.title", "dontforget.login.newaccount_error.email.text").then(function () {
							angular.element("#newcompte_mail").focus();
						});
						return;
					}
					if ($scope.password != $scope.password2) {
						Dialog.alert("dontforget.login.newaccount_error.password_confirm.title", "dontforget.login.newaccount_error.password_confirm.text").then(function () {
							angular.element("#login_password2").focus();
						});
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