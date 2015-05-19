package fr.chklang.dontforget.android.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.chklang.dontforget.android.R;
import fr.chklang.dontforget.android.activity.TasksActivity;
import fr.chklang.dontforget.android.adapter.TasksCategoriesListAdapter;
import fr.chklang.dontforget.android.adapter.TasksPlacesListAdapter;
import fr.chklang.dontforget.android.adapter.TasksTagsListAdapter;
import fr.chklang.dontforget.android.adapter.TasksTaskListAdapter;
import fr.chklang.dontforget.android.business.Category;
import fr.chklang.dontforget.android.business.Place;
import fr.chklang.dontforget.android.business.Tag;
import fr.chklang.dontforget.android.business.Task;
import fr.chklang.dontforget.android.database.DatabaseManager;
import fr.chklang.dontforget.android.dto.TaskStatus;
import fr.chklang.dontforget.android.helpers.ConfigurationHelper;

@SuppressWarnings("deprecation")
public class TasksController {

	private final TasksActivity tasksActivity;

	private String categories_ALL;

	private List<Category> categories = new ArrayList<Category>();
	private List<Tag> tags = new ArrayList<Tag>();
	private List<Place> places = new ArrayList<Place>();
	private List<Task> tasks = new ArrayList<Task>();
	private List<Task> currentTasks = new ArrayList<Task>();

	private BaseAdapter tasksAdapter;
	private BaseAdapter categoriesAdapter;
	private BaseAdapter tagsAdapter;
	private BaseAdapter placesAdapter;
	private TaskStatus currentStatus;
	private Category currentCategory;

	private LeftMenuSection leftMenuSectionSelected = LeftMenuSection.CATEGORIES;

	public TasksController(TasksActivity pTasksActivity) {
		tasksActivity = pTasksActivity;
	}

	private static enum LeftMenuSection {
		CATEGORIES, TAGS, PLACES
	}

	public void start() {

		categories_ALL = tasksActivity.getResources().getString(R.string.categories_all);
		currentStatus = TaskStatus.OPENED;

		initializeLeftMenu();
		
		initializeActionsButtons();
		
		initializeCategoriesAdapter();
		initializeTagsAdapter();
		initializePlacesAdapter();
		initializeTasksAdapter();
		
		loadData();
		actualiseTasksList();
	}

	private void loadData() {
		DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
			@Override
			public void execute() {
				categories.clear();
				categories.addAll(Category.dao.getAll());
		
				tasks.clear();
				tasks.addAll(Task.dao.getAll());
		
				tags.clear();
				tags.addAll(Tag.dao.getAll());
		
				places.clear();
				places.addAll(Place.dao.getAll());
		
				categoriesAdapter.notifyDataSetChanged();
				tagsAdapter.notifyDataSetChanged();
				placesAdapter.notifyDataSetChanged();
			}
		});
	}

	private void initializeActionsButtons() {
		View lButtonInprogress = tasksActivity.getMainLayout().getButtonInprogress();
		View lButtonFinished = tasksActivity.getMainLayout().getButtonFinished();
		View lButtonDeleted = tasksActivity.getMainLayout().getButtonDeleted();
		View lButtonAll = tasksActivity.getMainLayout().getButtonAll();

		final EditText tasks_new_text = this.tasksActivity.getMainLayout().getTasks_new_text();
		View tasks_new_button = this.tasksActivity.getMainLayout().getTasks_new_button();

		// Buttons of left menu
		View lButtonLeftMenuViewCategories = this.tasksActivity.getLeftMenuLayout().getButtonLeftMenuViewCategories();
		View lButtonLeftMenuViewTags = this.tasksActivity.getLeftMenuLayout().getButtonLeftMenuViewTags();
		View lButtonLeftMenuViewPlaces = this.tasksActivity.getLeftMenuLayout().getButtonLeftMenuViewPlaces();

		lButtonInprogress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentStatus = TaskStatus.OPENED;
				actualiseTasksList();
			}
		});
		lButtonFinished.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentStatus = TaskStatus.FINISHED;
				actualiseTasksList();
			}
		});
		lButtonDeleted.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentStatus = TaskStatus.DELETED;
				actualiseTasksList();
			}
		});
		lButtonAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentStatus = null;
				actualiseTasksList();
			}
		});

		tasks_new_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String lTaskText = tasks_new_text.getText().toString();
				if (lTaskText == null || lTaskText.isEmpty()) {
					return;
				}

				if (currentCategory == null) {
					Toast.makeText(TasksController.this.tasksActivity, R.string.tasks_tasks_create_no_category_selected, Toast.LENGTH_LONG).show();

					final LinearLayout leftmenu = (LinearLayout) tasksActivity.getMainLayout().getLeftMenuContainer();
					final DrawerLayout mDrawerLayout = (DrawerLayout) tasksActivity.getMainLayout().getMainView();
					mDrawerLayout.openDrawer(leftmenu);
					return;
				}

				final Set<Tag> lTags = new HashSet<Tag>();
				final Set<Place> lPlaces = new HashSet<Place>();

				DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
					@Override
					public void execute() {
						String lTaskTextTemp = lTaskText;
						Pattern lPatternTags = Pattern.compile(" #([^ ]+)");
						Matcher lMatcher = lPatternTags.matcher(" " + lTaskTextTemp);
						while (lMatcher.find()) {
							String lTagString = lMatcher.group(1);
							Tag lTag = Tag.dao.getByName(lTagString);
							if (lTag == null) {
								lTag = new Tag();
								lTag.setName(lTagString);
								lTag.setLastUpdate(System.currentTimeMillis());
								Tag.dao.save(lTag);
							}
							lTags.add(lTag);
							lTaskTextTemp = lTaskTextTemp.replace("#" + lTagString, "");
						}
		
						Pattern lPatternPlaces = Pattern.compile(" @([^ ]+)");
						lMatcher = lPatternPlaces.matcher(" " + lTaskTextTemp);
						while (lMatcher.find()) {
							String lPlaceString = lMatcher.group(1);
							Place lPlace = Place.dao.getByName(lPlaceString);
							if (lPlace == null) {
								lPlace = new Place();
								lPlace.setName(lPlaceString);
								lPlace.setLastUpdate(System.currentTimeMillis());
								Place.dao.save(lPlace);
							}
							lPlaces.add(lPlace);
							lTaskTextTemp = lTaskTextTemp.replace("@" + lPlaceString, "");
						}
		
						while (lTaskTextTemp.contains("  ")) {
							lTaskTextTemp = lTaskTextTemp.replaceAll("  ", " ");
						}
		
						lTaskTextTemp = lTaskTextTemp.trim();
		
						Task lTask = new Task();
						lTask.setName(lTaskTextTemp);
						lTask.setIdCategory(currentCategory.getIdCategory());
						lTask.setStatus(TaskStatus.OPENED);
						lTask.setLastUpdate(System.currentTimeMillis());
						Task.dao.save(lTask);
		
						for (Tag lTag : lTags) {
							Task.dao.addTagToTask(lTask, lTag);
						}
		
						for (Place lPlace : lPlaces) {
							Task.dao.addPlaceToTask(lTask, lPlace);
						}
						tasks.add(lTask);
					}
				});

				actualiseTasksList();

				// Reset input text
				tasks_new_text.setText("");
			}
		});

		lButtonLeftMenuViewCategories.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				leftMenuSectionSelected = LeftMenuSection.CATEGORIES;
				actualizeLeftMenuSelection();
			}
		});

		lButtonLeftMenuViewTags.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				leftMenuSectionSelected = LeftMenuSection.TAGS;
				actualizeLeftMenuSelection();
			}
		});

		lButtonLeftMenuViewPlaces.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				leftMenuSectionSelected = LeftMenuSection.PLACES;
				actualizeLeftMenuSelection();
			}
		});
	}

	private void actualizeLeftMenuSelection() {
		View lContainerCategories = this.tasksActivity.getLeftMenuLayout().getContainerCategories();
		View lContainerTags = this.tasksActivity.getLeftMenuLayout().getContainerTags();
		View lContainerPlaces = this.tasksActivity.getLeftMenuLayout().getContainerPlaces();

		int lCategoriesVisibility = leftMenuSectionSelected == LeftMenuSection.CATEGORIES ? View.VISIBLE : View.GONE;
		int lTagsVisibility = leftMenuSectionSelected == LeftMenuSection.TAGS ? View.VISIBLE : View.GONE;
		int lPlacesVisibility = leftMenuSectionSelected == LeftMenuSection.PLACES ? View.VISIBLE : View.GONE;

		lContainerCategories.setVisibility(lCategoriesVisibility);
		lContainerTags.setVisibility(lTagsVisibility);
		lContainerPlaces.setVisibility(lPlacesVisibility);
	}

	private void actualiseTasksList() {
		currentTasks.clear();
		for (Task lTask : tasks) {
			if (currentCategory == null || currentCategory.getIdCategory() == lTask.getIdCategory()) {
				if (currentStatus == null) {
					currentTasks.add(lTask);
					continue;
				}
				if (currentStatus == lTask.getStatus()) {
					currentTasks.add(lTask);
					continue;
				}
			}
		}
		tasksAdapter.notifyDataSetChanged();
	}

	private void actualiseCategoriesList() {
		TextView lTextCategorySelected = (TextView) tasksActivity.getMainLayout().getTextCategorySelected();
		if (currentCategory == null) {
			lTextCategorySelected.setText(R.string.tasks_allcategories);
		} else {
			lTextCategorySelected.setText(currentCategory.getName());
		}
		categoriesAdapter.notifyDataSetChanged();
	}

	private void actualiseTagsList() {
		tagsAdapter.notifyDataSetChanged();
	}

	private void actualisePlacesList() {
		placesAdapter.notifyDataSetChanged();
	}

	private void initializeTasksAdapter() {
		tasksAdapter = new TasksTaskListAdapter(tasksActivity, currentTasks) {
			@Override
			protected void onRefreshTasksList() {
				actualiseTasksList();
			}

			@Override
			protected void onValidateTask(final Task pTask) {
				pTask.setStatus(TaskStatus.FINISHED);

				DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
					@Override
					public void execute() {
						Task.dao.save(pTask);
					}
				});
				onRefreshTasksList();
			}

			@Override
			protected void onTrashTask(final Task pTask) {
				pTask.setStatus(TaskStatus.DELETED);

				DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
					@Override
					public void execute() {
						Task.dao.save(pTask);
					}
				});
				onRefreshTasksList();
			}

			@Override
			protected void onDeleteTask(final Task pTask) {
				tasks.remove(pTask);

				DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
					@Override
					public void execute() {
						Task.dao.delete(pTask.getIdTask());
					}
				});
				onRefreshTasksList();
			}
		};
		ListView tasks_view = (ListView) tasksActivity.getMainLayout().getTasks_view();
		tasks_view.setAdapter(tasksAdapter);
	}

	private void initializeCategoriesAdapter() {
		categoriesAdapter = new TasksCategoriesListAdapter(tasksActivity, categories) {

			@Override
			protected void onRefreshTasksList() {
				actualiseTasksList();
			}

			@Override
			protected void onRefreshCategoriesList() {
				actualiseCategoriesList();
			}

			@Override
			protected void onDeleteCategory(final Category pCategory) {
				DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
					@Override
					public void execute() {
						Collection<Task> lTasksOfThisCategory = Task.dao.findByCategory(pCategory);
						if (!lTasksOfThisCategory.isEmpty()) {
							Toast.makeText(tasksActivity, R.string.tasks_categories_delete_tasks_associated, Toast.LENGTH_LONG).show();
							return;
						}
						if (currentCategory != null && currentCategory.getIdCategory() == pCategory.getIdCategory()) {
							// Move to "All tasks"
							currentCategory = null;
						}
						Category.dao.delete(pCategory.getIdCategory());
						categories.remove(pCategory);
						onRefreshCategoriesList();
						onRefreshTasksList();
					}
				});
			}

			@Override
			protected void onChangeCategory(Category pCategory) {
				currentCategory = pCategory;
				actualiseCategoriesList();
				actualiseTasksList();
			}

			@Override
			protected String getLabelAllCategories() {
				return categories_ALL;
			}
		};
		final ListView categories_view = tasksActivity.getLeftMenuLayout().getCategories_view();
		categories_view.setAdapter(categoriesAdapter);
	}

	private void initializeTagsAdapter() {
		tagsAdapter = new TasksTagsListAdapter(tasksActivity, tags) {

			@Override
			protected void onRefreshTasksList() {
				actualiseTasksList();
			}

			@Override
			protected void onRefreshTagsList() {
				actualiseTagsList();
			}

			@Override
			protected void onSelectTag(Tag pTag) {
				// TODO
			}

			@Override
			protected void onDeleteTag(final Tag pTag) {
				final Collection<Task> lTasksOfThisTag = new ArrayList<Task>();
				DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
					@Override
					public void execute() {
						lTasksOfThisTag.addAll(Task.dao.findByTag(pTag));
					}
				});
				
				if (!lTasksOfThisTag.isEmpty()) {
					new AlertDialog.Builder(tasksActivity).setMessage(R.string.tasks_tags_create_name_already_used).setCancelable(true)
							.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {


									DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
										@Override
										public void execute() {
											Collection<Task> lTasksOfThisTag = Task.dao.findByTag(pTag);
											for (Task lTask : lTasksOfThisTag) {
												Task.dao.removeTagFromTask(lTask, pTag);
											}
											effectiveDelete(pTag);
										}
									});
								}
							}).setNegativeButton(R.string.button_no, null).show();
					return;
				}
				
				DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
					@Override
					public void execute() {
						effectiveDelete(pTag);
					}
				});
			}

			private void effectiveDelete(Tag pTag) {
				// TODO if filter is enabled on this tag, remove it
				Tag.dao.delete(pTag.getIdTag());
				tags.remove(pTag);
				onRefreshTagsList();
				onRefreshTasksList();
			}
		};
		final ListView tags_view = tasksActivity.getLeftMenuLayout().getTags_view();
		tags_view.setAdapter(tagsAdapter);
	}

	private void initializePlacesAdapter() {
		placesAdapter = new TasksPlacesListAdapter(tasksActivity, places) {

			@Override
			protected void onRefreshTasksList() {
				actualiseTasksList();
			}

			@Override
			protected void onRefreshPlacesList() {
				actualisePlacesList();
			}

			@Override
			protected void onSelectPlace(Place pPlace) {
				// TODO
			}

			@Override
			protected void onDeletePlace(final Place pPlace) {
				final Collection<Task> lTasksOfThisPlace = new ArrayList<Task>();
				DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
					@Override
					public void execute() {
						lTasksOfThisPlace.addAll(Task.dao.findByPlace(pPlace));
					}
				});
				if (!lTasksOfThisPlace.isEmpty()) {
					new AlertDialog.Builder(tasksActivity)
						.setMessage(R.string.tasks_places_create_name_already_used)
						.setCancelable(true)
						.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
									@Override
									public void execute() {
										Collection<Task> lTasksOfThisPlace = Task.dao.findByPlace(pPlace);
										for (Task lTask : lTasksOfThisPlace) {
											Task.dao.removePlaceFromTask(lTask, pPlace);
										}
										effectiveDelete(pPlace);
									}
								});
							}
						})
						.setNegativeButton(R.string.button_no, null)
						.show();
					return;
				}
				DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
					@Override
					public void execute() {
						effectiveDelete(pPlace);
					}
				});
			}

			private void effectiveDelete(Place pPlace) {
				//TODO if filter is enabled on this place, remove it
				Place.dao.delete(pPlace.getIdPlace());
				places.remove(pPlace);
				onRefreshPlacesList();
				onRefreshTasksList();
			}
		};
		final ListView places_view = tasksActivity.getLeftMenuLayout().getPlaces_view();
		places_view.setAdapter(placesAdapter);
	}

	private void initializeLeftMenu() {

		final View lLeftmenu = tasksActivity.getMainLayout().getLeftMenuContainer();
		final DrawerLayout lDrawerLayout = tasksActivity.getMainLayout().getMainView();

		tasksActivity.getMainLayout().getButtonOpenLeftMenu().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lDrawerLayout.openDrawer(lLeftmenu);
			}
		});
		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(tasksActivity, lDrawerLayout, R.drawable.menu, R.string.drawer_open, R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				tasksActivity.invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				actualizeLeftMenuSelection();
				tasksActivity.invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		lDrawerLayout.setDrawerListener(mDrawerToggle);

		// Enable button to create a category
		final EditText categories_new_text = tasksActivity.getLeftMenuLayout().getCategories_new_text();
		final ImageButton categories_new_button = tasksActivity.getLeftMenuLayout().getCategories_new_button();
		categories_new_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String lCategoryText = categories_new_text.getText().toString();
				if (lCategoryText == null || lCategoryText.isEmpty()) {
					return;
				}

				DatabaseManager.transaction(tasksActivity, new DatabaseManager.Transaction() {
					@Override
					public void execute() {
						if (Category.dao.getByName(lCategoryText) != null) {
							Toast.makeText(tasksActivity, R.string.tasks_categories_create_name_already_used, Toast.LENGTH_LONG).show();
							return;
						}
						Category lCategory = new Category();
						lCategory.setLastUpdate(System.currentTimeMillis());
						lCategory.setName(lCategoryText);
						Category.dao.save(lCategory);
						lCategory.setUuid(ConfigurationHelper.getDeviceId() + "_" + lCategory.getIdCategory());
						Category.dao.save(lCategory);
						categories.add(lCategory);
					}
				});


				actualiseCategoriesList();
			}
		});
	}
}
