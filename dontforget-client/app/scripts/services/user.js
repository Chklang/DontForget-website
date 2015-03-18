'use strict';

(function() {
	/**
	 * @ngdoc service
	 * @name spacesimperiumApp.user
	 * @description # user Service in the spacesimperiumApp.
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.service('User', [ 'restRequest', function User(restRequest) {
		this.create = function(pLogin, pPassword, pEmail, pSuccessCallback) {
			return restRequest.post({
				url : '/users/create',
				data : {
					pseudo : pLogin,
					password : pPassword,
					mail : pEmail
				},
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},
					'409' : function() {
						alert("Le pseudonyme " + pLogin + " est déjà utilisé.");// TODO
																				// TR
					},
					'default' : function(pData) {
						alert("Impossible de créer l'utilisateur. Erreur inconnue : " + pData);// TODO
																								// TR
					}
				}
			});
		};
	} ]);
})();