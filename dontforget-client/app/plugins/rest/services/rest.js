'use strict';

/**
 * @ngdoc service
 * @name rest.user
 * @description # user Service in the spacesimperiumApp.
 */
angular.module('RestModule').service('restRequest', [ '$http', 'rest', function User($http, rest) {
	this.base = function(options) {
		var url = options.url;
		var method = options.method;
		var data = options.data;
		var contentType = options.contentType;
		var success = options.success;
		var error = options.error;
		var errorsCodes = options.errorsCodes;

		var objRequest = {};
		objRequest.method = method;
		objRequest.url = rest.restPath + url;
		if (data) {
			objRequest.data = data;
		}
		if (contentType != null) {
			objRequest.headers = {
					"Content-Type" : contentType
			}
		}
		var result = $http(objRequest);
		result.success(function(data, status) {
			success(data);
		});
		result.error(function(data, status) {
			var lIsShownError = false;
			if (errorsCodes) {
				if (errorsCodes[status]) {
					lIsShownError = true;
					errorsCodes[status](data);
				} else if (errorsCodes['default']) {
					lIsShownError = true;
					errorsCodes['default'](data);
				}
			}
			if (!lIsShownError) {
				if (rest.defaultErrorsCodes[status]) {
					lIsShownError = true;
					rest.defaultErrorsCodes[status](data);
				} else if (rest.defaultErrorsCodes['default']) {
					lIsShownError = true;
					rest.defaultErrorsCodes['default'](data);
				}
			}
			if (error) {
				error(data, status);
			}
		});
		return result;
	};
	this.get = function(options) {
		options.method = 'GET';
		return this.base(options);
	};
	this.post = function(options) {
		options.method = 'POST';
		return this.base(options);
	};
	this.put = function(options) {
		options.method = 'PUT';
		return this.base(options);
	};
	this.delete = function(options) {
		options.method = 'DELETE';
		return this.base(options);
	};
} ]);
