'use strict';

(function() {
	/**
	 * @ngdoc function
	 * @name dontforgetApp.controller:TasksCtrl
	 * @description # TasksCtrl Controller of the dontforgetApp
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.controller('TasksCtrl', [ '$scope', '$state', 'Tags', 'Places', 'Categories', 'Tasks', function($scope, $state, Tags, Places, Categories, Tasks) {
		$scope.openTaskDropdown = false;
		if (!$scope.isConnected) {
			$state.transitionTo('main');
		}
		var lAllTags = [];
		var lAllPlaces = [];
		$scope.categories = [];
		$scope.currentCategory = null;
		$scope.allTasks = [];
		$scope.allTasksFilter = "";
		$scope.allTasksModeView = "OPENED";
		$scope.alerts = [];
		$scope.category_all = false;
		
		Tags.getAll(function (pResults) {
			lAllTags = pResults;
		});
		
		Places.getAll(function (pResults) {
			lAllPlaces = pResults;
		});
		
		Categories.getAll(function (pResults) {
			$scope.categories = pResults;
			angular.forEach(pResults, function (pEntry) {
				pEntry.active = false;
			});
			$scope.currentCategory = pResults[0].name;
			pResults[0].active = true;
		});
		$scope.changeCurrentCategory = function (pCategory) {
			if (pCategory == null) {
				$scope.currentCategory = null;
				$scope.category_all = true;
				return;
			} else {
				$scope.category_all = false;
			}
			$scope.currentCategory = pCategory.name;
			angular.forEach($scope.categories, function (pEntry) {
				pEntry.active = $scope.currentCategory == pEntry.name;
			});
		};
		
		Tasks.getAll(function (pResults) {
			$scope.allTasks = pResults;
		});

		var lCurrentPrefix = null;

		$scope.addTaskValue = "";
		$scope.addTaskDropdownValue = [];
		$scope.addTaskDropdownIndexSelected = -1;
		var addTaskElement = angular.element("#addTask")[0];
		$scope.currentWord = null;
		
		function setActionDone(pMessage, pType, pCancelFunction) {
			$scope.alerts[0] = {
					msg : pMessage,
					type : pType,
					cancel : pCancelFunction
			};
		}
		
		$scope.cancelAction = function (pAction) {
			if (pAction.cancel) {
				pAction.cancel();
			}
		};
		
		$scope.closeAlert = function (pAction) {
			$scope.alerts = [];
		};

		function getCurrentWord() {
			var lCurrentPosition = getCursorPosition(addTaskElement);
			var lText = $scope.addTaskValue;

			var lCurrentWord = "";
			var lStartText = lCurrentPosition;
			if (lStartText > lText.length) {
				// N'est pas sur un texte
				return "";
			}
			if (lStartText > 0 && lText[lStartText] == " ") {
				lStartText--;
			}
			if (lText[lStartText] == " ") {
				// N'est pas sur un texte
				return "";
			}
			while (lStartText > 0 && lText[lStartText] != " ") {
				lStartText--;
			}
			if (lText[lStartText] == " ") {
				lStartText++;
			}
			while (lText[lStartText] != " " && lStartText < lText.length) {
				lCurrentWord += lText[lStartText];
				lStartText++;
			}
			return lCurrentWord;
		}
		function addTaskDropdownToggle() {
			var lWord = getCurrentWord();
			if (lWord.length == 0) {
				$scope.openTaskDropdown = false;
				return;
			}
			var lElementsList = [];
			if (lWord.length > 1) {
				$scope.currentWord = lWord.slice(1);
			} else {
				$scope.currentWord = null;
			}
			switch (lWord[0]) {
			case '#':
				$scope.openTaskDropdown = true;
				lElementsList = lAllTags;
				lCurrentPrefix = "#";
				break;
			case '@':
				$scope.openTaskDropdown = true;
				lElementsList = lAllPlaces;
				lCurrentPrefix = "@";
				break;
			default:
				$scope.openTaskDropdown = false;
				$scope.addTaskDropdownIndexSelected = -1;
				break;
			}

			if ($scope.openTaskDropdown) {
				$scope.addTaskDropdownValue = lElementsList;
				if ($scope.addTaskDropdownIndexSelected == -1) {
					$scope.addTaskDropdownIndexSelected = 0;
				}
			}
		}
		$scope.addTaskClick = function() {
			addTaskDropdownToggle();
		}
		$scope.addTaskKeyUp = function(pEvent) {
			addTaskDropdownToggle();
		};
		$scope.addTaskKeyDown = function(pEvent) {
			window.here = $scope;
			if (!$scope.openTaskDropdown) {
				switch (pEvent.keyCode) {
				case 13 :
					//Save the task
					if ($scope.addTaskValue.trim() == "") {
						return;
					}
					if ($scope.currentCategory == null) {
						alert("Veuillez choisir une catégorie");
						return;
					}
					Tasks.create($scope.currentCategory, $scope.addTaskValue, function (pResult) {
						$scope.allTasks.push(pResult);
						$scope.addTaskValue = "";
						
						angular.forEach(pResult.tags, function (pEntry) {
							var lIsFound = false;
							angular.forEach(lAllTags, function (pTag) {
								if (!lIsFound && pTag.name == pEntry.name) {
									lIsFound = true;
								}
							});
							if (!lIsFound) {
								lAllTags.push(pEntry);
							}
						});
						
						angular.forEach(pResult.places, function (pEntry) {
							var lIsFound = false;
							angular.forEach(lAllPlaces, function (pPlace) {
								if (!lIsFound && pPlace.name == pEntry.name) {
									lIsFound = true;
								}
							});
							if (!lIsFound) {
								lAllPlaces.push(pEntry);
							}
						});
					});
					break;
				}
				return;
			}
			switch (pEvent.keyCode) {
			case 13://Enter
				if ($scope.addTaskDropdownIndexSelected >= 0) {
					//Put the selection into text
					replaceCurrentWord(addTaskElement, lCurrentPrefix + $scope.addTaskDropdownValue[$scope.addTaskDropdownIndexSelected].name);
				}
				break;
			case 38://up
				if ($scope.addTaskDropdownIndexSelected > 0) {
					$scope.addTaskDropdownIndexSelected--;
				}
				pEvent.preventDefault()
				return false;
			case 40://down
				if ($scope.addTaskDropdownIndexSelected < ($scope.addTaskDropdownValue.length-1)) {
					$scope.addTaskDropdownIndexSelected++;
				}
				pEvent.preventDefault();
				return false;
			}
		};

		function getCursorPosition(pElement) {
			var cur_pos = 0;
			if (pElement.selectionStart) {
				cur_pos = pElement.selectionStart;
			} else if (document.selection) {
				pElement.focus();

				var r = document.selection.createRange();
				if (r != null) {
					var re = pElement.createTextRange(), rc = re.duplicate();
					re.moveToBookmark(r.getBookmark());
					rc.setEndPoint('EndToStart', re);

					cur_pos = rc.text.length;
				}
			}
			return cur_pos;
		}
		function setCursorPosition(pElement, pPosition) {

			if (pElement.setSelectionRange) {
				pElement.focus();
				pElement.setSelectionRange(pPosition, pPosition);
			} else if (pElement.createTextRange) {
				var range = pElement.createTextRange();
				range.collapse(true);
				range.moveEnd('character', pPosition);
				range.moveStart('character', pPosition);
				range.select();
			}
		}
		$scope.addTaskDropdownIndexIsSelected = function (pIndex) {
			return pIndex==$scope.addTaskDropdownIndexSelected || ($scope.addTaskDropdownIndexSelected>$scope.addTaskDropdownValue.length && pIndex == ($scope.addTaskDropdownValue.length-1));
		}
		
		var lLastRegexp = null;
		var lWordOfRegexp = null;
		$scope.addTaskDropdownIndexIsShowed = function(pValue) {
			if ($scope.currentWord == null || $scope.currentWord == "") {
				return true;
			}
			if (lLastRegexp == null || lWordOfRegexp != $scope.currentWord) {
				lWordOfRegexp = $scope.currentWord;
				lLastRegexp = new RegExp($scope.currentWord);
			}
			return lLastRegexp.test(pValue);
		}
		
		function replaceCurrentWord(pElement, pNewWord) {
			var lCurrentPosition = getCursorPosition(addTaskElement);
			var lText = $scope.addTaskValue;

			var lCurrentWord = "";
			var lStartText = lCurrentPosition;
			if (lStartText > lText.length) {
				// N'est pas sur un texte
				return "";
			}
			if (lStartText > 0 && lText[lStartText] == " ") {
				lStartText--;
			}
			if (lText[lStartText] == " ") {
				// N'est pas sur un texte
				return "";
			}
			while (lStartText > 0 && lText[lStartText] != " ") {
				lStartText--;
			}
			if (lText[lStartText] == " ") {
				lStartText++;
			}
			var lEndText = lStartText;
			while (lText[lEndText] != " " && lEndText < lText.length) {
				lEndText++;
			}
			
			var lNewText = "";
			if (lStartText > 0) {
				lNewText += $scope.addTaskValue.slice(0, lStartText);
			}
			lNewText += pNewWord;
			if (lEndText < $scope.addTaskValue.length) {
				lNewText += $scope.addTaskValue.slice(lEndText);
			} else {
				lNewText += " ";
			}
			$scope.addTaskValue = lNewText;
			lEndText = lStartText + lNewText.length + 1;
			setCursorPosition(pElement, lEndText);
			addTaskDropdownToggle();
		}
		
		var lRegExpAllTasksFilter = null;
		var lLastValueAllTasksFilter = null;
		
		$scope.allTasksIsShowed = function (pTask) {
			if ($scope.currentCategory != null && $scope.currentCategory != pTask.category.name) {
				return false;
			}
			if ($scope.allTasksModeView != 'ALL' && pTask.status != $scope.allTasksModeView) {
				return false;
			}
			if ($scope.allTasksFilter == null) {
				return true;
			}
			if (lLastValueAllTasksFilter == null || lLastValueAllTasksFilter != $scope.allTasksFilter) {
				lLastValueAllTasksFilter = $scope.allTasksFilter;
				lRegExpAllTasksFilter = new RegExp($scope.allTasksFilter);
			}
			var lTextTask = pTask.text;
			angular.forEach(pTask.tags, function (pEntry) {
				lTextTask += " #" + pEntry.name;
			});
			angular.forEach(pTask.places, function (pEntry) {
				lTextTask += " @" + pEntry.name;
			});
			return lRegExpAllTasksFilter.test(lTextTask);
		};
		
		$scope.allTasksAddToFilters = function (pElement) {
			$scope.allTasksFilter += " " + pElement;
		};
		
		function cancel(pTo, pTask) {
			switch (pTo) {
			case 'OPENED' :
				$scope.allTasksStatusOpened(pTask);
				break;
			case 'FINISHED' :
				$scope.allTasksStatusFinished(pTask);
				break;
			case 'DELETED' :
				$scope.allTasksStatusDeleted(pTask);
				break;
			}
		}
		
		$scope.allTasksStatusFinished = function (pTask) {
			var lOrigin = pTask.status;
			Tasks.setFinished(pTask.id, function (pResult) {
				var lNbElements = $scope.allTasks.length;
				for (var i=0; i<lNbElements; i++) {
					if ($scope.allTasks[i].id == pResult.id) {
						$scope.allTasks[i] = pResult;
					}
				}
				var lCancelFunction = function() {
					cancel(lOrigin, pResult);
				};
				var lMsg = "La tâche est marquée terminée."; //TODO TR
				var lType = "success";
				setActionDone(lMsg, lType, lCancelFunction);
			});
		};
		$scope.allTasksStatusOpened = function (pTask) {
			var lOrigin = pTask.status;
			Tasks.setOpened(pTask.id, function (pResult) {
				var lNbElements = $scope.allTasks.length;
				for (var i=0; i<lNbElements; i++) {
					if ($scope.allTasks[i].id == pResult.id) {
						$scope.allTasks[i] = pResult;
					}
				}
				var lCancelFunction = function() {
					cancel(lOrigin, pResult);
				};
				var lMsg = "La tâche est marquée ouverte."; //TODO TR
				var lType = "success";
				setActionDone(lMsg, lType, lCancelFunction);
			});
		};
		$scope.allTasksStatusDeleted = function (pTask) {
			var lOrigin = pTask.status;
			Tasks.setDeleted(pTask.id, function (pResult) {
				var lNbElements = $scope.allTasks.length;
				for (var i=0; i<lNbElements; i++) {
					if ($scope.allTasks[i].id == pResult.id) {
						$scope.allTasks[i] = pResult;
					}
				}
				var lCancelFunction = function() {
					cancel(lOrigin, pResult);
				};
				var lMsg = "La tâche est supprimée."; //TODO TR
				var lType = "success";
				setActionDone(lMsg, lType, lCancelFunction);
			});
		};
	} ]);
})();