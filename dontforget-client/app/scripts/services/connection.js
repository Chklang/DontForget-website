'use strict';

/**
 * @ngdoc service
 * @name spacesimperiumApp.Connection
 * @description # Connection Service in the spacesimperiumApp.
 */
angular.module('dontforgetApp').service('Connection', ['restRequest', function Connection(restRequest) {
	// AngularJS will instantiate a singleton by calling "new" on this
	// function
	this.connect = function (pPseudo, pPassword, pSuccessCallback) {
		return restRequest.post({
			url : '/users/login',
			data : {
				'pseudo' : pPseudo,
				'password' : pPassword
			},
			success : pSuccessCallback,
			errorsCodes : {
				'401' : function (pData) {
					alert("Joueur non trouvé ou mot de passe erroné");//TODO TR
				},
				'default' : function (pData) {
					alert("Connexion impossible. Erreur inconnue : " + pData);//TODO TR
				}
			}
		});
	};
	this.disconnect = function (pSuccessCallback) {
		return restRequest.post({
			url : '/users/disconnect',
			success : pSuccessCallback,
			errorsCodes : {
				'default' : function (pData) {
					alert("Déconnexion impossible. Erreur inconnue : " + pData);//TODO TR
				}
			}
		});
	};
	this.me = function (pSuccessCallback) {
		return restRequest.get({
			url : '/users/me',
			success : pSuccessCallback,
			errorsCodes : {
				'401' : function (pData) {
					pSuccessCallback(null);
				},
				'default' : function (pData) {
					alert("Connexion impossible. Erreur inconnue : " + pData);//TODO TR
				}
			}
		});
	};
}]);
