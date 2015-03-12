'use strict';

/**
 * @ngdoc function
 * @name dontforgetApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the dontforgetApp
 */
angular.module('dontforgetApp')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
