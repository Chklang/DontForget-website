'use strict';

/**
 * @ngdoc function
 * @name dontforgetApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the dontforgetApp
 */
angular.module('dontforgetApp')
  .controller('MainCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
