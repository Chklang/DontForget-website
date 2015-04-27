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
		this.create = function(pName, pSuccessCallback) {
			return restRequest.post({
				url : '/categories',
				data : pName,
				contentType : 'text/plain',
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},'409' : function(pData) {
						alert("Le nom " + pNom + " existe déjà.");// TODO TR
					},
					'default' : function(pData) {
						alert("Impossible de créer la catégorie. Erreur inconnue : " + pData);// TODO
						// TR
					}
				}
			});
		};
		this.update = function(pName, pNewName, pSuccessCallback) {
			return restRequest.put({
				url : '/categories/'+pName,
				data : pNewName,
				contentType : 'text/plain',
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},'409' : function(pData) {
						alert("Le nom " + pNom + " existe déjà.");// TODO TR
					},
					'default' : function(pData) {
						alert("Impossible de modifier la catégorie. Erreur inconnue : " + pData);// TODO
						// TR
					}
				}
			});
		};
		this.delete = function(pName, pSuccessCallback) {
			return restRequest.delete({
				url : '/categories/'+pName,
				data : pName,
				contentType : 'text/plain',
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},
					'default' : function(pData) {
						alert("Impossible de supprimer la catégorie. Erreur inconnue : " + pData);// TODO
						// TR
					}
				}
			});
		};
	}]);
})()