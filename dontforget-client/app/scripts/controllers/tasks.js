'use strict';

(function() {
	/**
	 * @ngdoc function
	 * @name dontforgetApp.controller:TasksCtrl
	 * @description # TasksCtrl Controller of the dontforgetApp
	 */
	var myApp = angular.module('dontforgetApp');
	myApp.controller('TasksCtrl', [ '$scope', '$state', '$translate', 'Dialog', 'Tags', 'Places', 'Categories', 'Tasks', function($scope, $state, $translate, Dialog, Tags, Places, Categories, Tasks) {
		
		//Verify user connection
		if (!$scope.isConnected) {
			$state.transitionTo('main');
		}
		
		//Helper because browser don't like to wrap functions in few cases
		var execute = function (f) { f();};

		//All tags
		var lAllTags = [];
		Tags.getAll(function (pResults) {
			lAllTags = pResults;
		});
		
		//All places
		var lAllPlaces = [];
		Places.getAll(function (pResults) {
			lAllPlaces = pResults;
		});
		
		//Drop down with tags or places when user create a new task
		$scope.addTask_openDropdown = false;

		//Load tasks
		$scope.allTasks = [];
		Tasks.getAll(function (pResults) {
			$scope.allTasks = pResults;
		});
		
		$scope.allTasksFilter = "";
		$scope.allTasksModeView = "OPENED";
		$scope.addTaskText = "";
		$scope.addTaskDropdownValue = [];
		$scope.addTaskDropdownIndexSelected = -1;
		$scope.currentWord = null;
		
		
		/* ************************************************************************************ */
		/*											Categories									*/
		
		execute(function () {
			
			//Initialize categories
			$scope.categories = [];
			$scope.currentCategory = null;
			Categories.getAll(function (pResults) {
				$scope.categories = pResults;
				angular.forEach(pResults, function (pEntry) {
					pEntry.active = false;
				});
				$scope.currentCategory = pResults[0].name;
				pResults[0].active = true;
			});
			
			//Know if category "all" is selected or not
			$scope.category_all_is_chosen = false;
			
			$scope.ajouterCategorie = function () {
				Dialog.prompt("dontforget.tasks.modals.create_category.title", "dontforget.tasks.modals.create_category.text", "dontforget.tasks.modals.create_category.placeholder").then(function (pNom) {
					//Création de la catégorie et ajout
					if (pNom === "" || pNom === null || pNom == undefined) {
						Dialog.alert("dontforget.tasks.modals.create_category_empty_name.title", "dontforget.tasks.modals.create_category_empty_name.text");
					} else {
						Categories.create(pNom, function (pCategoryDto) {
							$scope.categories.push(pCategoryDto);
						});
					}
				});
			};
			
			$scope.updateCategogy = function (pCategory) {
				Dialog.prompt("dontforget.tasks.modals.update_category.title", "dontforget.tasks.modals.update_category.text", "dontforget.tasks.modals.update_category.placeholder", pCategory.name).then(function (pNom) {
					//Modification de la catégorie et ajout
					if (pNom === "" || pNom === null || pNom == undefined) {
						Dialog.alert("dontforget.tasks.modals.create_category_empty_name.title", "dontforget.tasks.modals.create_category_empty_name.text");
					} else {
						Categories.update(pCategory.name, pNom, function (pCategoryDto) {
							var lOldName = pCategory.name;
							pCategory.name = pCategoryDto.name;
							//Update all tasks which use this category
							angular.forEach($scope.allTasks, function (element) {
								if (element.category.uuid == pCategoryDto.uuid) {
									element.category = pCategoryDto;
								}
							});
							
							if ($scope.currentCategory != null && $scope.currentCategory == lOldName) {
								$scope.currentCategory = pCategoryDto.name;
							}
						});
					}
				});
			};
			
			$scope.deleteCategogy = function (pCategory) {
				var lText = $translate.instant("dontforget.tasks.modals.delete_category.text", {name:pCategory.name});
				Dialog.confirm("dontforget.tasks.modals.delete_category.title", lText).then(function (pValue) {
		        	Categories.delete(pCategory.name, function (pCategoryDto) {
						var lNewCategoriesList = [];
						angular.forEach($scope.categories, function (element) {
							if (element.name != pCategory.name) {
								lNewCategoriesList.push(element);
							}
						});
						$scope.categories = lNewCategoriesList;
					});
				});
			};
			
			$scope.changeCurrentCategory = function (pCategory) {
				if (pCategory == null) {
					$scope.currentCategory = null;
					$scope.category_all_is_chosen = true;
					return;
				} else {
					$scope.category_all_is_chosen = false;
				}
				$scope.currentCategory = pCategory.name;
				angular.forEach($scope.categories, function (pEntry) {
					pEntry.active = $scope.currentCategory == pEntry.name;
				});
			};
		});

		/* ************************************************************************************ */
		/*									Alert box about an action							*/
		
		var setActionDone = function (){};
		execute(function () {
			$scope.alerts = [];
			setActionDone = function (pMessage, pType, pCancelFunction) {
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
		});
		
		
		/* ************************************************************************************ */
		/*											Add task									*/
		
		
		execute(function () {
			var lCurrentPrefix = null;
			var addTaskElement = document.getElementById("addTask");
	
			//Get current word where user is
			function getCurrentWord() {
				var lCurrentPosition = getCursorPosition(addTaskElement);
				var lText = $scope.addTaskText;
	
				var lCurrentWord = "";
				var lStartText = lCurrentPosition;
				if (lStartText > lText.length) {
					// Isn't on a word
					return "";
				}
				if (lStartText > 0 && lText[lStartText] == " ") {
					lStartText--;
				}
				if (lText[lStartText] == " ") {
					// Isn't on a word
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
			
			//Open or close the dropdown on add task
			function addTaskDropdownToggle() {
				var lWord = getCurrentWord();
				if (lWord.length == 0) {
					$scope.addTask_openDropdown = false;
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
					$scope.addTask_openDropdown = true;
					lElementsList = lAllTags;
					lCurrentPrefix = "#";
					break;
				case '@':
					$scope.addTask_openDropdown = true;
					lElementsList = lAllPlaces;
					lCurrentPrefix = "@";
					break;
				default:
					$scope.addTask_openDropdown = false;
					$scope.addTaskDropdownIndexSelected = -1;
					break;
				}
	
				if ($scope.addTask_openDropdown) {
					$scope.addTaskDropdownValue = lElementsList;
				}
			}
			
			//Actions on DOM of add task
			$scope.addTaskClick = function() {
				addTaskDropdownToggle();
			}
			
			$scope.addTaskKeyUp = function(pEvent) {
				addTaskDropdownToggle();
			};
			
			$scope.addTaskKeyDown = function(pEvent) {
				window.here = $scope;
				if (!$scope.addTask_openDropdown) {
					switch (pEvent.keyCode) {
					case 13 :
						//Save the task
						if ($scope.addTaskText.trim() == "") {
							return;
						}
						if ($scope.currentCategory == null) {
							Dialog.alert("dontforget.tasks.modals.no_category_selected.title", "dontforget.tasks.modals.no_category_selected.text");
							return;
						}
						Tasks.create($scope.currentCategory, $scope.addTaskText, function (pResult) {
							$scope.allTasks.push(pResult);
							$scope.addTaskText = "";
							
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
						if ($scope.addTaskDropdownIndexSelected < $scope.addTaskDropdownValue.length) {
							replaceCurrentWord(addTaskElement, lCurrentPrefix + $scope.addTaskDropdownValue[$scope.addTaskDropdownIndexSelected].name);
						} else {
							replaceCurrentWord(addTaskElement, null);
						}
					} else {
						replaceCurrentWord(addTaskElement, null);
					}
					break;
				case 38://up
					if ($scope.addTaskDropdownIndexSelected > -1) {
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
				default :
					//Another key was pressed, refresh addTaskDropdownIndexSelected
					console.log($scope.currentWord);
					break;
				}
			};
	
			//Calculate what is the position of the cursor into add task input
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
			
			//Modifier the cursor position into add task input
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

			
			//Replace the current word to another word
			function replaceCurrentWord(pElement, pNewWord) {
				var lCurrentPosition = getCursorPosition(addTaskElement);
				var lText = $scope.addTaskText;

				var lCurrentWord = "";
				var lStartText = lCurrentPosition;
				if (lStartText > lText.length) {
					// Isn't on a word
					return "";
				}
				if (lStartText > 0 && lText[lStartText] == " ") {
					lStartText--;
				}
				if (lText[lStartText] == " ") {
					// Isn't on a word
					return "";
				}
				while (lStartText > 0 && lText[lStartText] != " ") {
					lStartText--;
				}
				
				if (lText[lStartText] == " ") {
					lStartText++;
				}
				var lEndText = lStartText;
				var lOriginalWord = "";
				while (lText[lEndText] != " " && lEndText < lText.length) {
					lOriginalWord += lText[lEndText];
					lEndText++;
				}
				
				var lNewText = "";
				if (lStartText > 0) {
					lNewText += $scope.addTaskText.slice(0, lStartText);
				}
				lNewText += pNewWord==null?lOriginalWord:pNewWord;
				if (lEndText < $scope.addTaskText.length) {
					lNewText += $scope.addTaskText.slice(lEndText);
				} else {
					lNewText += " ";
				}
				$scope.addTaskText = lNewText;
				lEndText = lStartText + lNewText.length + 1;
				setCursorPosition(pElement, lEndText);
				addTaskDropdownToggle();
			}
		});
		
		
		
		//Know if the the parameter pIndex is the value selected into dropdown
		$scope.addTaskDropdownIndexIsSelected = function (pIndex) {
			return pIndex==$scope.addTaskDropdownIndexSelected || ($scope.addTaskDropdownIndexSelected>$scope.addTaskDropdownValue.length && pIndex == ($scope.addTaskDropdownValue.length-1));
		}
		
		
		
		//Autocomplete list for add tag/place/...
		execute(function () {
			var lLastRegexp = null;
			var lWordOfRegexp = null;
			$scope.addTaskDropdownIndexIsShowed = function(pValue, pIndex) {
				if ($scope.currentWord == null || $scope.currentWord == "") {
					return true;
				}
				if (lLastRegexp == null || lWordOfRegexp != $scope.currentWord) {
					lWordOfRegexp = $scope.currentWord;
					lLastRegexp = new RegExp($scope.currentWord);
				}
				var lIsShown = lLastRegexp.test(pValue.name);
				if (!lIsShown && pIndex == $scope.addTaskDropdownIndexSelected) {
					$scope.addTaskDropdownIndexSelected++;
					if ($scope.addTaskDropdownIndexSelected >= $scope.addTaskDropdownValue.length) {
						$scope.addTaskDropdownIndexSelected = -1;
					}
				}
				return lIsShown;
			};
		});

		
		
		
		
		
		
		/* ************************************************************************************ */
		/*										List of tasks									*/
		
		//Test if a task must be hidded or not into all tasks list
		execute(function () {
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
		});
		
		//Add a tag or place to current filter
		$scope.allTasksAddToFilters = function (pElement) {
			$scope.allTasksFilter += " " + pElement;
		};
		
		
		//Change task status
		execute(function () {
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
				Tasks.setFinished(pTask.uuid, function (pResult) {
					var lNbElements = $scope.allTasks.length;
					for (var i=0; i<lNbElements; i++) {
						if ($scope.allTasks[i].uuid == pResult.uuid) {
							$scope.allTasks[i] = pResult;
						}
					}
					var lCancelFunction = function() {
						cancel(lOrigin, pResult);
					};
					var lMsg = "dontforget.tasks.changeState.to_finish";
					var lType = "success";
					setActionDone(lMsg, lType, lCancelFunction);
				});
			};
			$scope.allTasksStatusOpened = function (pTask) {
				var lOrigin = pTask.status;
				Tasks.setOpened(pTask.uuid, function (pResult) {
					var lNbElements = $scope.allTasks.length;
					for (var i=0; i<lNbElements; i++) {
						if ($scope.allTasks[i].uuid == pResult.uuid) {
							$scope.allTasks[i] = pResult;
						}
					}
					var lCancelFunction = function() {
						cancel(lOrigin, pResult);
					};
					var lMsg = "dontforget.tasks.changeState.to_open";
					var lType = "success";
					setActionDone(lMsg, lType, lCancelFunction);
				});
			};
			$scope.allTasksStatusDeleted = function (pTask) {
				var lOrigin = pTask.status;
				Tasks.setDeleted(pTask.uuid, function (pResult) {
					var lNbElements = $scope.allTasks.length;
					for (var i=0; i<lNbElements; i++) {
						if ($scope.allTasks[i].uuid == pResult.uuid) {
							$scope.allTasks[i] = pResult;
						}
					}
					var lCancelFunction = function() {
						cancel(lOrigin, pResult);
					};
					var lMsg = "dontforget.tasks.changeState.to_delete";
					var lType = "success";
					setActionDone(lMsg, lType, lCancelFunction);
				});
			};
			$scope.allTasksStatusDeletedDefinitivly = function (pTask) {
				var lOrigin = pTask.status;
				Tasks.delete(pTask.uuid, function (pResult) {
					var lNbElements = $scope.allTasks.length;
					var lNewTaskList = [];
					angular.forEach($scope.allTasks, function (element) {
						if (element.uuid != pTask.uuid) {
							lNewTaskList.push(element);
						}
					});
					$scope.allTasks = lNewTaskList;
					var lMsg = "dontforget.tasks.changeState.to_trash";
					var lType = "success";
					setActionDone(lMsg, lType, null);
				});
			};
		});
	} ]);
})();