'use strict';

/**
 * @ngdoc service
 * @name rest.user
 * @description # user Service in the spacesimperiumApp.
 */
angular.module('WaitingModule').service('waitingDialog', [ 'waiting', function waitingDialog(waiting) {
	this.show = function (message, options) {
		// Assigning defaults
		var settings = $.extend({
			dialogSize: 'm',
			progressType: ''
		}, options);
		if (typeof message === 'undefined') {
			message = 'Chargement';
		}
		if (typeof options === 'undefined') {
			options = {};
		}
		// Configuring dialog
		waiting.dialog.find('.modal-dialog').attr('class', 'modal-dialog').addClass('modal-' + settings.dialogSize);
		waiting.dialog.find('.progress-bar').attr('class', 'progress-bar');
		if (settings.progressType) {
			waiting.dialog.find('.progress-bar').addClass('progress-bar-' + settings.progressType);
		}
		waiting.dialog.find('h3').text(message);
		// Opening dialog
		waiting.dialog.modal();
	},
	/**
	 * Closes dialog
	 */
	this.hide = function () {
		waiting.dialog.modal('hide');
	}
} ]);
