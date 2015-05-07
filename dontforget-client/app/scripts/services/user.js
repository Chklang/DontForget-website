'use strict';

(function() {
	/**
	 * @ngdoc service
	 * @name spacesimperiumApp.user
	 * @description # user Service in the spacesimperiumApp.
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.service('User', [ '$translate', '$cookieStore', 'restRequest', function User($translate, $cookieStore, restRequest) {
		this.create = function(pLogin, pPassword, pEmail, pSuccessCallback) {
			return restRequest.post({
				url : '/users/create',
				data : {
					pseudo : pLogin,
					password : pPassword,
					mail : pEmail,
					codelang : $cookieStore.get("lang")
				},
				success : pSuccessCallback,
				errorsCodes : {
					'409' : {
						'text' : $translate.instant('dontforget.services.User.create.409', {login:pLogin})
					},
					'default' : {
						'title' : 'dontforget.services.User.create.title'
					}
				}
			});
		};
		this.update = function(pLogin, pPassword, pEmail, pSuccessCallback) {
			return restRequest.put({
				url : '/users/update',
				data : {
					pseudo : pLogin,
					password : pPassword,
					mail : pEmail,
					codelang : $cookieStore.get("lang")
				},
				success : pSuccessCallback,
				errorsCodes : {
					'409' : {
						'text' : $translate.instant('dontforget.services.User.update.409', {login:pLogin})
					},
					'default' : {
						'title' : 'dontforget.services.User.update.title'
					}
				}
			});
		};
	} ]);
})();