'use strict';

(function() {
	/**
	 * @ngdoc service
	 * @name dontforgetApp.tag
	 * @description # tag Service in the dontforgetApp.
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.service('Tags', [ 'restRequest', function Tags(restRequest) {
		this.getAll = function(pSuccessCallback) {
			return restRequest.get({
				url : '/tags',
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
				url : '/tags/'+pId,
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},
					'default' : function(pData) {
						alert("Impossible de supprimer le tag. Erreur inconnue : " + pData);// TODO
						// TR
					}
				}
			});
		};
	} ]);
})();