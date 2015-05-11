'use strict';

(function() {
	/**
	 * @ngdoc service
	 * @name dontforgetApp.place
	 * @description # place Service in the dontforgetApp.
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.service('Places', [ '$translate', 'restRequest', function Places($translate, restRequest) {
		this.getAll = function(pSuccessCallback) {
			return restRequest.get({
				url : '/places',
				success : pSuccessCallback,
				errorsCodes : {
					'default' :  {
						'title' : 'dontforget.services.Places.getAll.title'
					}
				}
			});
		};
		this.delete = function(pId, pSuccessCallback) {
			return restRequest.delete({
				url : '/places/'+pId,
				success : pSuccessCallback,
				errorsCodes : {
					'404' : {
						'text' : $translate.instant('dontforget.services.Places.delete.404', {id:pId})
					},
					'default' : {
						'title' : 'dontforget.services.Places.delete.title'
					}
				}
			});
		};
	} ]);
})();