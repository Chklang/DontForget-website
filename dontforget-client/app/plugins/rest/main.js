'use strict';

/**
 * @ngdoc overview
 * @name spacesimperiumApp
 * @description # spacesimperiumApp
 * 
 * Main module of the application.
 */
(function () {
	
var module = angular.module('RestModule', []);

module.provider('rest', function rest() {
	var restPath = '';
	var defaultErrorsCodes = {
		'default' : function (pData) {
			alert('Internal server error : ' + pData);//TODO TR
		}
	};
	
	this.restPath = function (pValue) {
		restPath = pValue;
	};
	this.$get = [function () {
		return {
			restPath : restPath,
			defaultErrorsCodes : defaultErrorsCodes
		};
	}];
});

})();