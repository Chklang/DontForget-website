'use strict';

(function() {
	/**
	 * @ngdoc service
	 * @name dontforgetApp.tag
	 * @description # tag Service in the dontforgetApp.
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.service('Tags', [ '$translate', 'restRequest', function Tags($translate, restRequest) {
		this.getAll = function(pSuccessCallback) {
			return restRequest.get({
				url : '/tags',
				success : pSuccessCallback,
				errorsCodes : {
					'default' : {
						'title' : 'dontforget.services.Tags.getAll.title'
					}
				}
			});
		};
		this.delete = function(pId, pSuccessCallback) {
			return restRequest.delete({
				url : '/tags/'+pId,
				success : pSuccessCallback,
				errorsCodes : {
					'404' : {
						'text' : $translate.instant('dontforget.services.Tags.delete.404', {id:pId})
					},
					'default' : {
						'title' : 'dontforget.services.Tags.delete.title'
					}
				}
			});
		};
	} ]);
})();