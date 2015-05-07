'use strict';

(function() {
	/**
	 * @ngdoc service
	 * @name dontforgetApp.task
	 * @description # task Service in the dontforgetApp.
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.service('Tasks', [ '$translate', 'restRequest', function Tasks($translate, restRequest) {
		this.getAll = function(pSuccessCallback) {
			return restRequest.get({
				url : '/tasks',
				success : pSuccessCallback,
				errorsCodes : {
					'default' : {
						'title' : 'dontforget.services.Tasks.getAll.title'
					}
				}
			});
		};
		this.getAllByCategory = function(pCategoryName, pSuccessCallback) {
			return restRequest.get({
				url : '/tasks/' + pCategoryName,
				success : pSuccessCallback,
				errorsCodes : {
					'404' : {
						'text' : $translate.instant('dontforget.services.Tasks.getAllByCategory.404', {name:pCategoryName})
					},
					'default' : {
						'title' : 'dontforget.services.Tasks.getAllByCategory.title'
					}
				}
			});
		};
		this.get = function(pId, pSuccessCallback) {
			return restRequest.get({
				url : '/tasks/' + pId,
				success : pSuccessCallback,
				errorsCodes : {
					'404' : {
						'text' : $translate.instant('dontforget.services.Tasks.delete.404', {id:pId})
					},
					'default' : {
						'title' : 'dontforget.services.Tasks.get.title'
					}
				}
			});
		};
		this.create = function(pCategoryName, pText, pSuccessCallback) {
			return restRequest.post({
				url : '/tasks/' + pCategoryName,
				contentType : "text/plain",
				data : pText,
				success : pSuccessCallback,
				errorsCodes : {
					'default' : {
						'title' : 'dontforget.services.Tasks.create.title'
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
					'404' : {
						'text' : $translate.instant('dontforget.services.Tasks.delete.404', {id:pId})
					},
					'default' : {
						'title' : 'dontforget.services.Tasks.update.title'
					}
				}
			});
		};
		this.delete = function(pId, pSuccessCallback) {
			return restRequest.delete({
				url : '/tasks/'+pId,
				success : pSuccessCallback,
				errorsCodes : {
					'404' : {
						'text' : $translate.instant('dontforget.services.Tasks.delete.404', {id:pId})
					},
					'default' : {
						'title' : 'dontforget.services.Tasks.delete.title'
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