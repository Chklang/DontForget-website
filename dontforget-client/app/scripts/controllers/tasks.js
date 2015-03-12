'use strict';

/**
 * @ngdoc function
 * @name dontforgetApp.controller:TasksCtrl
 * @description
 * # TasksCtrl
 * Controller of the dontforgetApp
 */
angular.module('dontforgetApp')
  .controller('TasksCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
