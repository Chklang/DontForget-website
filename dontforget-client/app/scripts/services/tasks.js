'use strict';

(function() {
	/**
	 * @ngdoc service
	 * @name dontforgetApp.task
	 * @description # task Service in the dontforgetApp.
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.service('Tasks', [ 'restRequest', function Tasks(restRequest) {
		this.getAll = function(pSuccessCallback) {
			return restRequest.get({
				url : '/tasks',
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
		this.get = function(pId, pSuccessCallback) {
			return restRequest.get({
				url : '/tasks/' + pId,
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},
					'default' : function(pData) {
						alert("Impossible de récupérer la tâche. Erreur inconnue : " + pData);// TODO
																								// TR
					}
				}
			});
		};
		this.create = function(pText, pSuccessCallback) {
			return restRequest.post({
				url : '/tasks',
				contentType : "text/plain",
				data : pText,
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},
					'default' : function(pData) {
						alert("Impossible de créer la tâche. Erreur inconnue : " + pData);// TODO
																								// TR
					}
				}
			});
		};
		this.update = function(pId, pData, pSuccessCallback) {
			return restRequest.put({
				url : '/tasks/'+pId,
				data : pData,
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},
					'default' : function(pData) {
						alert("Impossible de mettre à jour la tâche. Erreur inconnue : " + pData);// TODO
																								// TR
					}
				}
			});
		};
		this.delete = function(pId, pSuccessCallback) {
			return restRequest.delete({
				url : '/tasks/'+pId,
				success : pSuccessCallback,
				errorsCodes : {
					'400' : function(pData) {
						alert("Problème de paramètres : " + pData);// TODO TR
					},
					'default' : function(pData) {
						alert("Impossible de supprimer la tâche. Erreur inconnue : " + pData);// TODO
						// TR
					}
				}
			});
		};
		this.setFinished = function (pId, pSuccessCallback) {
			return this.update(pId, {
				status : 'FINISHED'
			}, pSuccessCallback);
		};
		this.setOpened = function (pId, pSuccessCallback) {
			return this.update(pId, {
				status : 'OPENED'
			}, pSuccessCallback);
		};
		this.setDeleted = function (pId, pSuccessCallback) {
			return this.update(pId, {
				status : 'DELETED'
			}, pSuccessCallback);
		};
	} ]);
})();