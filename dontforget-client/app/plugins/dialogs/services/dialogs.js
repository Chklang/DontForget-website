'use strict';

/**
 * @ngdoc service
 * @name rest.user
 * @description # user Service in the spacesimperiumApp.
 */
angular.module('DialogsModule').service('Dialog', [ '$rootScope', '$modal', 'dialogConfig', function User($rootScope, $modal, dialogConfig) {
	
	//Generate default buttons to close popup
	var generateDefaultButtons = function () {
		return [{
			title : dialogConfig.textButtonClose,
			action : function (pScope) {
				pScope.close();
			}
		}];
	};
	
	var internal = function (opts) {
		
		//Extract options
		var lTitle = opts.title || "";
		var lTextContent = opts.textContent || "";
		var lHtmlContent = opts.htmlContent || "";
		var lButtons = opts.buttons || generateDefaultButtons();
		var lTemplate = opts.template || dialogConfig.template;
		var lSubScope = opts.scope || {};
		
		//Create scope
		var lScope = $rootScope.$new(true);
		
		//Create modal
		var lModal = $modal.open({
			templateUrl : 'plugins/dialogs/views/template.html',
			controller : 'DialogCtrl',
			scope : lScope
		});
		
		//Add modal to scope
		lScope.modal = lModal;

		//Add datas to scope
		lScope.title = lTitle;
		lScope.textContent = lTextContent;
		lScope.htmlContent = lHtmlContent;
		lScope.buttons = lButtons;
		
		console.log(lSubScope);
		for (var lAttr in lSubScope) {
			lScope[lAttr] = lSubScope[lAttr];
		}
		
		return lScope;
	};
	
	this.alert = function (pTitle, pMessage) {
		return new Promise(function (resolve, reject) {
			internal({
				title : pTitle,
				textContent : pMessage,
				buttons : [{
					title : dialogConfig.textButtonClose,
					action : function (pScope) {
						pScope.close();
					}
				}]
			});
		});
	};
	
	this.confirm = function (pTitle, pMessage) {
		return new Promise(function (resolve, reject) {
			internal({
				title : pTitle,
				textContent : pMessage,
				buttons : [{
					title : 'Ok',
					action : function (pScope) {
						pScope.close();
						resolve(true);
					}
				}, {
					title : 'Cancel',
					action : function (pScope) {
						pScope.close();
						reject(false);
					}
				}]
			});
		});
	};
	
	this.prompt = function (pTitle, pMessage, pPlaceholder, pDefaultValue) {
		var lContent = '<span translate>{{question}}</span> : <input type="text" ng-model="value" placeholder="{{placeholder}}" />';
		return new Promise(function (resolve, reject) {
			internal({
				title : pTitle,
				htmlContent : lContent,
				scope : {
					question : pMessage,
					value : pDefaultValue || "",
					placeholder : pPlaceholder
				},
				buttons : [{
					title : 'Ok',
					action : function (pScope) {
						pScope.close();
						resolve(pScope.value);
					}
				}, {
					title : 'Cancel',
					action : function (pScope) {
						pScope.close();
						reject(false);
					}
				}]
			});
		});
	};
	
	this.dialog = function (opts) {
		return internal(opts);
	}
} ]);
