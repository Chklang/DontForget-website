'use strict';

(function() {
	/**
	 * @ngdoc service
	 * @name dontforgetApp.place
	 * @description # place Service in the dontforgetApp.
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.service('Places', [ 'restRequest', function Places(restRequest) {
		this.getAll = function(pSuccessCallback) {
			return restRequest.get({
				url : '/places',
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},
					'default' : function(pData) {
						alert("Impossible de récupérer la liste des tâches. Erreur inconnue : " + pData);// TODO
																								// TR
					}
				}
			});
		};
		this.delete = function(pId, pSuccessCallback) {
			return restRequest.delete({
				url : '/places/'+pId,
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},
					'default' : function(pData) {
						alert("Impossible de supprimer le place. Erreur inconnue : " + pData);// TODO
						// TR
					}
				}
			});
		};
	} ]);
})();