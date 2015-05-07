'use strict';
(function() {
	/**
	 * @ngdoc service
	 * @name dontforgetApp.Categories
	 * @description # Categories Service in the dontforgetApp.
	 */
	var lMyApp = angular.module('dontforgetApp');
	lMyApp.service('Categories', [ '$translate', 'restRequest', function Tasks($translate, restRequest) {
		this.getAll = function(pSuccessCallback) {
			return restRequest.get({
				url : '/categories',
				success : pSuccessCallback,
				errorsCodes : {
					'default' : {
						'title' : 'dontforget.services.Categories.getAll.title'
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
					'409' : {
						'text' : $translate.instant('dontforget.services.Categories.create.409', {name:pName})
					},
					'default' : {
						'title' : 'dontforget.services.Categories.create.title'
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
					'409' : {
						'text' : $translate.instant('dontforget.services.Categories.update.409', {name:pNewName})
					},
					'default' : {
						'title' : 'dontforget.services.Categories.update.title'
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
					'default' : {
						'title' : 'dontforget.services.Categories.delete.title'
					}
				}
			});
		};
	}]);
})()