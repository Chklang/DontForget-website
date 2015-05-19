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
				url : '/categories/' + pCategoryName + '/tasks',
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
		this.get = function(pUuid, pSuccessCallback) {
			return restRequest.get({
				url : '/tasks/' + pUuid,
				success : pSuccessCallback,
				errorsCodes : {
					'404' : {
						'text' : $translate.instant('dontforget.services.Tasks.delete.404', {id:pUuid})
					},
					'default' : {
						'title' : 'dontforget.services.Tasks.get.title'
					}
				}
			});
		};
		this.create = function(pCategoryName, pText, pSuccessCallback) {
			return restRequest.post({
				url : '/categories/' + pCategoryName + '/tasks',
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
		this.update = function(pUuid, pData, pSuccessCallback) {
			return restRequest.put({
				url : '/tasks/'+pUuid,
				data : pData,
				success : pSuccessCallback,
				errorsCodes : {
					'404' : {
						'text' : $translate.instant('dontforget.services.Tasks.delete.404', {id:pUuid})
					},
					'default' : {
						'title' : 'dontforget.services.Tasks.update.title'
					}
				}
			});
		};
		this.delete = function(pUuid, pSuccessCallback) {
			return restRequest.delete({
				url : '/tasks/'+pUuid,
				success : pSuccessCallback,
				errorsCodes : {
					'404' : {
						'text' : $translate.instant('dontforget.services.Tasks.delete.404', {id:pUuid})
					},
					'default' : {
						'title' : 'dontforget.services.Tasks.delete.title'
					}
				}
			});
		};
		this.setFinished = function (pUuid, pSuccessCallback) {
			return this.update(pUuid, {
				status : 'FINISHED'
			}, pSuccessCallback);
		};
		this.setOpened = function (pUuid, pSuccessCallback) {
			return this.update(pUuid, {
				status : 'OPENED'
			}, pSuccessCallback);
		};
		this.setDeleted = function (pUuid, pSuccessCallback) {
			return this.update(pUuid, {
				status : 'DELETED'
			}, pSuccessCallback);
		};
	} ]);
})();