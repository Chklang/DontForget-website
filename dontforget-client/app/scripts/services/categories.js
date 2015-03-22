'use strict';
(function() {
	/**
	 * @ngdoc service
	 * @name dontforgetApp.Categories
	 * @description # Categories Service in the dontforgetApp.
	 */
	var lMyApp = angular.module('dontforgetApp');
	lMyApp.service('Categories', [ 'restRequest', function Tasks(restRequest) {
		this.getAll = function(pSuccessCallback) {
			return restRequest.get({
				url : '/categories',
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},
					'default' : function(pData) {
						alert("Impossible de récupérer la liste des catégories. Erreur inconnue : " + pData);// TODO
						// TR
					}
				}
			});
		};
	}]);
})()