'use strict';

/**
 * @ngdoc service
 * @name spacesimperiumApp.Connection
 * @description # Connection Service in the spacesimperiumApp.
 */
angular.module('dontforgetApp').service('Connection', ['$translate', 'restRequest', function Connection($translate, restRequest) {
	// AngularJS will instantiate a singleton by calling "new" on this
	// function
	this.connect = function (pPseudo, pPassword, pSuccessCallback) {
		return restRequest.post({
			url : '/users/login',
			data : {
				'pseudo' : pPseudo,
				'password' : pPassword
			},
			success : pSuccessCallback,
			errorsCodes : {
				'401' : {
					'text' : 'dontforget.services.Connection.connect.401'
				},
				'default' : {
					'title' : 'dontforget.services.Connection.connect.title'
				}
			}
		});
	};
	this.disconnect = function (pSuccessCallback) {
		return restRequest.post({
			url : '/users/disconnect',
			success : pSuccessCallback,
			errorsCodes : {
				'default' : {
					'title' : 'dontforget.services.Connection.disconnect.title'
				}
			}
		});
	};
	this.me = function (pSuccessCallback) {
		return restRequest.get({
			url : '/users/me',
			success : pSuccessCallback,
			errorsCodes : {
				'401' : function (pData) {
					pSuccessCallback(null);
				},
				'default' : {
					'title' : 'dontforget.services.Connection.me.title'
				}
			}
		});
	};
}]);
