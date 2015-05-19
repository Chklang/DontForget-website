package fr.chklang.dontforget.android.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.chklang.dontforget.android.R;
import fr.chklang.dontforget.android.business.Category;
import fr.chklang.dontforget.android.business.Place;
import fr.chklang.dontforget.android.business.Tag;
import fr.chklang.dontforget.android.business.Task;
import fr.chklang.dontforget.android.dao.CategoryDAO;
import fr.chklang.dontforget.android.dao.TaskDAO;
import fr.chklang.dontforget.android.dto.TaskStatus;
import fr.chklang.dontforget.android.helpers.ConfigurationHelper;

@SuppressWarnings("deprecation")
public class TasksActivity extends Activity {

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

	private CategoryDAO categoryDAO = new CategoryDAO();
	private TaskDAO taskDAO = new TaskDAO();
	
	private LeftMenuSection leftMenuSectionSelected = LeftMenuSection.CATEGORIES;
	
	private static enum LeftMenuSection {
		CATEGORIES, TAGS, PLACES
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);

		categories_ALL = getResources().getString(R.string.categories_all);
		currentStatus = TaskStatus.OPENED;
		
		//Attach left menu to navigation drawer
		ViewGroup lLeftMenuContainer = (ViewGroup) this.findViewById(R.id.tasks_leftmenu);
		LayoutInflater mInflater = (LayoutInflater) TasksActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		lLeftMenuContainer.addView(mInflater.inflate(R.layout.activity_tasks_left_menu, lLeftMenuContainer, false));

		initializeLeftMenu();
		initializeActionsButtons();
		initializeCategoriesAdapter();
		initializeTagsAdapter();
		initializePlacesAdapter();
		initializeTasksAdapter();
		refreshCategories();
		actualiseTasksList();
	}

	private void refreshCategories() {
		categories.clear();
		categories.addAll(categoryDAO.getAll());
		tasks.clear();
		tasks.addAll(taskDAO.getAll());
		tags.clear();
		tags.addAll(Tag.dao.getAll());
		places.clear();
		places.addAll(Place.dao.getAll());
		categoriesAdapter.notifyDataSetChanged();
		tagsAdapter.notifyDataSetChanged();
		placesAdapter.notifyDataSetChanged();
	}

	private void initializeActionsButtons() {
		View lButtonInprogress = findViewById(R.id.tasks_inprogress);
		View lButtonFinished = findViewById(R.id.tasks_finished);
		View lButtonDeleted = findViewById(R.id.tasks_deleted);
		View lButtonAll = findViewById(R.id.tasks_all);

		final EditText tasks_new_text = (EditText) this.findViewById(R.id.tasks_new_text);
		View tasks_new_button = this.findViewById(R.id.tasks_new_button);
		
		//Buttons of left menu
		View lButtonLeftMenuViewCategories = this.findViewById(R.id.tasks_leftmenu_view_categories);
		View lButtonLeftMenuViewTags = this.findViewById(R.id.tasks_leftmenu_view_tags);
		View lButtonLeftMenuViewPlaces = this.findViewById(R.id.tasks_leftmenu_view_places);

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
				String lTaskText = tasks_new_text.getText().toString();
				if (lTaskText == null || lTaskText.isEmpty()) {
					return;
				}
				
				if (currentCategory == null) {
					Toast.makeText(TasksActivity.this, R.string.tasks_tasks_create_no_category_selected, Toast.LENGTH_LONG).show();

					final LinearLayout leftmenu = (LinearLayout) findViewById(R.id.tasks_leftmenu);
					final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.tasks_menu_layout);
					mDrawerLayout.openDrawer(leftmenu);	
					return;
				}

				Set<Tag> lTags = new HashSet<Tag>();
				Set<Place> lPlaces = new HashSet<Place>();
				Set<Category> lCategories = new HashSet<Category>();
				
				Pattern lPatternTags = Pattern.compile(" #([^ ]+)");
				Matcher lMatcher = lPatternTags.matcher(" " + lTaskText);
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
					lTaskText = lTaskText.replace("#" + lTagString, "");
				}
				
				Pattern lPatternPlaces = Pattern.compile(" @([^ ]+)");
				lMatcher = lPatternPlaces.matcher(" " + lTaskText);
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
					lTaskText = lTaskText.replace("@" + lPlaceString, "");
				}
				
				while (lTaskText.contains("  ")) {
					lTaskText = lTaskText.replaceAll("  ",	" ");
				}
				
				lTaskText = lTaskText.trim();

				Task lTask = new Task();
				lTask.setName(lTaskText);
				lTask.setIdCategory(currentCategory.getIdCategory());
				lTask.setStatus(TaskStatus.OPENED);
				lTask.setLastUpdate(System.currentTimeMillis());
				taskDAO.save(lTask);
				
				for (Tag lTag : lTags) {
					Task.dao.addTagToTask(lTask, lTag);
				}
				
				for (Place lPlace : lPlaces) {
					Task.dao.addPlaceToTask(lTask, lPlace);
				}

				tasks.add(lTask);
				actualiseTasksList();
				
				//Reset input text
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
		View lContainerCategories = this.findViewById(R.id.tasks_leftmenu_menu_categories);
		View lContainerTags = this.findViewById(R.id.tasks_leftmenu_menu_tags);
		View lContainerPlaces = this.findViewById(R.id.tasks_leftmenu_menu_places);
		
		int lCategoriesVisibility = leftMenuSectionSelected==LeftMenuSection.CATEGORIES?View.VISIBLE:View.GONE;
		int lTagsVisibility = leftMenuSectionSelected==LeftMenuSection.TAGS?View.VISIBLE:View.GONE;
		int lPlacesVisibility = leftMenuSectionSelected==LeftMenuSection.PLACES?View.VISIBLE:View.GONE;
		
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
		TextView lTextCategorySelected = (TextView) findViewById(R.id.tasks_category_selected);
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

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(categories_view);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
	private static class Badge extends TextView {
		public Badge(Context context) {
			super(context);
		}
	}

	private void initializeTasksAdapter() {
		tasksAdapter = new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater mInflater = (LayoutInflater) TasksActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ViewGroup linearLayout;
				ViewGroup lLayoutBadges = null;
				TextView title = null;
				ImageButton lValidateButton = null;
				ImageButton lTrashButton = null;
				ImageButton lDeleteButton = null;

				final Task lCurrentTask = currentTasks.get(position);

				if (convertView == null) {
					View view = mInflater.inflate(R.layout.activity_tasks_entry, parent, false);
					linearLayout = (ViewGroup) view;
				} else {
					View view = convertView;
					linearLayout = (ViewGroup) view;
				}

				try {
					// Otherwise, find the TextView field within the layout
					title = (TextView) linearLayout.findViewById(R.id.title_task);
					lValidateButton = (ImageButton) linearLayout.findViewById(R.id.validate);
					lTrashButton = (ImageButton) linearLayout.findViewById(R.id.trash);
					lDeleteButton = (ImageButton) linearLayout.findViewById(R.id.delete);
					lLayoutBadges = (ViewGroup) linearLayout.findViewById(R.id.task_badges);
				} catch (ClassCastException e) {
					Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
					throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
				}

				title.setText(lCurrentTask.getName());

				// Enable buttons
				lValidateButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						lCurrentTask.setStatus(TaskStatus.FINISHED);
						taskDAO.save(lCurrentTask);
						actualiseTasksList();
					}
				});
				lTrashButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						lCurrentTask.setStatus(TaskStatus.DELETED);
						taskDAO.save(lCurrentTask);
						actualiseTasksList();
					}
				});
				lDeleteButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						tasks.remove(lCurrentTask);
						taskDAO.delete(lCurrentTask.getIdTask());
						actualiseTasksList();
					}
				});
				
//				int lNbChildren = linearLayout.getChildCount();
//				Collection<View> lChildren = new ArrayList<View>();
//				for (int i =0; i<lNbChildren; i++) {
//					View lChild = linearLayout.getChildAt(i);
//					if (lChild instanceof Badge) {
//						lChildren.add(lChild);
//					}
//				}
//				for (View lChild : lChildren) {
//					linearLayout.removeView(lChild);
//				}
				lLayoutBadges.removeAllViews();
				//Add badges for tags
				Collection<Tag> lTags = Tag.dao.getTagsOfTask(lCurrentTask);
				for (Tag lTag : lTags) {
					View lTextLayout = mInflater.inflate(R.layout.activity_tasks_entry_badge, parent, false);
					TextView lTextView = (TextView) lTextLayout.findViewById(R.id.badge);
					lTextView.setText("#" + lTag.getName());
					lLayoutBadges.addView(lTextLayout);
				}
				
				Collection<Place> lPlaces = Place.dao.getPlacesOfTask(lCurrentTask);
				for (Place lPlace : lPlaces) {
					View lTextLayout = mInflater.inflate(R.layout.activity_tasks_entry_badge, parent, false);
					TextView lTextView = (TextView) lTextLayout.findViewById(R.id.badge);
					lTextView.setText("@" + lPlace.getName());
					lLayoutBadges.addView(lTextLayout);
				}
				return linearLayout;
			}

			@Override
			public long getItemId(int position) {
				return currentTasks.get(position).getIdTask();
			}

			@Override
			public String getItem(int position) {
				return currentTasks.get(position).getName();
			}

			@Override
			public int getCount() {
				return currentTasks.size();
			}
		};
		ListView tasks_view = (ListView) findViewById(R.id.tasks_elements);
		tasks_view.setAdapter(tasksAdapter);
	}

	private void initializeCategoriesAdapter() {
		categoriesAdapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater mInflater = (LayoutInflater) TasksActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				LinearLayout lParent;
				TextView lText;
				ImageButton lButtonDelete;

				if (convertView == null) {
					lParent = (LinearLayout) mInflater.inflate(R.layout.activity_tasks_leftmenu_categories_entry, parent, false);
				} else {
					lParent = (LinearLayout) convertView;
				}

				try {
					// Otherwise, find the TextView field within the layout
					lText = (TextView) lParent.findViewById(R.id.tasks_leftmenu_categories_entry_text);
					lButtonDelete = (ImageButton) lParent.findViewById(R.id.tasks_leftmenu_categories_entry_delete);
				} catch (ClassCastException e) {
					Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
					throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
				}

				final Category lCategory = getCurrentCategory(position);

				if (lCategory == null) {
					lText.setText(categories_ALL);
					lButtonDelete.setVisibility(View.INVISIBLE);
				} else {
					lText.setText(lCategory.getName());
					lButtonDelete.setVisibility(View.VISIBLE);
					lButtonDelete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Collection<Task> lTasksOfThisCategory = taskDAO.findByCategory(lCategory);
							if (!lTasksOfThisCategory.isEmpty()) {
								Toast.makeText(TasksActivity.this, R.string.tasks_categories_delete_tasks_associated, Toast.LENGTH_LONG).show();
								return;
							}
							if (currentCategory != null && currentCategory.getIdCategory() == lCategory.getIdCategory()) {
								//Move to "All tasks"
								currentCategory = null;
							}
							categoryDAO.delete(lCategory.getIdCategory());
							categories.remove(lCategory);
							actualiseCategoriesList();
							actualiseTasksList();
						}
					});
				}
				lParent.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						changeCategory(lCategory);
					}
				});
				return lParent;
			}

			@Override
			public long getItemId(int position) {
				if (position == 0) {
					return -1;
				}
				return getCurrentCategory(position).getIdCategory();
			}

			@Override
			public String getItem(int position) {
				if (position == 0) {
					return categories_ALL;
				}
				return getCurrentCategory(position).getName();
			}
			
			private Category getCurrentCategory(int position) {
				if (position == 0) {
					return null;
				}
				return categories.get(position - 1);
			}

			@Override
			public int getCount() {
				return categories.size() + 1;
			}
		};
		final ListView categories_view = (ListView) findViewById(R.id.tasks_leftmenu_categories_list);
		categories_view.setAdapter(categoriesAdapter);
	}

	private void initializeTagsAdapter() {
		tagsAdapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater mInflater = (LayoutInflater) TasksActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				LinearLayout lParent;
				TextView lText;
				ImageButton lButtonDelete;

				if (convertView == null) {
					lParent = (LinearLayout) mInflater.inflate(R.layout.activity_tasks_leftmenu_tags_entry, parent, false);
				} else {
					lParent = (LinearLayout) convertView;
				}

				try {
					// Otherwise, find the TextView field within the layout
					lText = (TextView) lParent.findViewById(R.id.tasks_leftmenu_tags_entry_text);
					lButtonDelete = (ImageButton) lParent.findViewById(R.id.tasks_leftmenu_tags_entry_delete);
				} catch (ClassCastException e) {
					Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
					throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
				}

				final Tag lTag = getCurrentTag(position);

				lText.setText(lTag.getName());
				lButtonDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Collection<Task> lTasksOfThisTag = taskDAO.findByTag(lTag);
						if (!lTasksOfThisTag.isEmpty()) {
							new AlertDialog.Builder(TasksActivity.this)
								.setMessage(R.string.tasks_tags_create_name_already_used)
								.setCancelable(true)
								.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										Collection<Task> lTasksOfThisTag = taskDAO.findByTag(lTag);
										for (Task lTask : lTasksOfThisTag) {
											Task.dao.removeTagFromTask(lTask, lTag);
										}
										Tag.dao.delete(lTag.getIdTag());
										tags.remove(lTag);
										actualiseTagsList();
										actualiseTasksList();
									}
								})
								.setNegativeButton(R.string.button_no, null)
								.show();
							return;
						}
						//TODO if filter is enabled on this tag, remove it
						Tag.dao.delete(lTag.getIdTag());
						tags.remove(lTag);
						actualiseTagsList();
						actualiseTasksList();
					}
				});
				lParent.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//TODO action on selection
					}
				});
				return lParent;
			}

			@Override
			public long getItemId(int position) {
				return getCurrentTag(position).getIdTag();
			}

			@Override
			public String getItem(int position) {
				return getCurrentTag(position).getName();
			}
			
			private Tag getCurrentTag(int position) {
				return tags.get(position);
			}

			@Override
			public int getCount() {
				return tags.size();
			}
		};
		final ListView tags_view = (ListView) findViewById(R.id.tasks_leftmenu_tags_list);
		tags_view.setAdapter(tagsAdapter);
	}
	
	private void initializePlacesAdapter() {
		placesAdapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater mInflater = (LayoutInflater) TasksActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				LinearLayout lParent;
				TextView lText;
				ImageButton lButtonDelete;
				
				if (convertView == null) {
					lParent = (LinearLayout) mInflater.inflate(R.layout.activity_tasks_leftmenu_places_entry, parent, false);
				} else {
					lParent = (LinearLayout) convertView;
				}
				
				try {
					// Otherwise, find the TextView field within the layout
					lText = (TextView) lParent.findViewById(R.id.tasks_leftmenu_places_entry_text);
					lButtonDelete = (ImageButton) lParent.findViewById(R.id.tasks_leftmenu_places_entry_delete);
				} catch (ClassCastException e) {
					Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
					throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
				}
				
				final Place lPlace = getCurrentPlace(position);
				
				lText.setText(lPlace.getName());
				lButtonDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Collection<Task> lTasksOfThisPlace = taskDAO.findByPlace(lPlace);
						if (!lTasksOfThisPlace.isEmpty()) {
							new AlertDialog.Builder(TasksActivity.this)
								.setMessage(R.string.tasks_places_create_name_already_used)
								.setCancelable(true)
								.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										Collection<Task> lTasksOfThisPlace = taskDAO.findByPlace(lPlace);
										for (Task lTask : lTasksOfThisPlace) {
											Task.dao.removePlaceFromTask(lTask, lPlace);
										}
										Place.dao.delete(lPlace.getIdPlace());
										places.remove(lPlace);
										actualisePlacesList();
										actualiseTasksList();
									}
								})
								.setNegativeButton(R.string.button_no, null)
								.show();
							return;
						}
						//TODO if filter is enabled on this place, remove it
						Place.dao.delete(lPlace.getIdPlace());
						places.remove(lPlace);
						actualiseTagsList();
						actualiseTasksList();
					}
				});
				lParent.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//TODO action on selection
					}
				});
				return lParent;
			}
			
			@Override
			public long getItemId(int position) {
				return getCurrentPlace(position).getIdPlace();
			}
			
			@Override
			public String getItem(int position) {
				return getCurrentPlace(position).getName();
			}
			
			private Place getCurrentPlace(int position) {
				return places.get(position);
			}
			
			@Override
			public int getCount() {
				return places.size();
			}
		};
		final ListView places_view = (ListView) findViewById(R.id.tasks_leftmenu_places_list);
		places_view.setAdapter(placesAdapter);
	}

	private void initializeLeftMenu() {
		
		final LinearLayout lLeftmenu = (LinearLayout) findViewById(R.id.tasks_leftmenu);
		final DrawerLayout lDrawerLayout = (DrawerLayout) findViewById(R.id.tasks_menu_layout);
		
		((ImageButton)this.findViewById(R.id.tasks_open_menu)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lDrawerLayout.openDrawer(lLeftmenu);				
			}
		});
		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, lDrawerLayout, R.drawable.menu, R.string.drawer_open, R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				actualizeLeftMenuSelection();
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		lDrawerLayout.setDrawerListener(mDrawerToggle);
		
		//Enable button to create a category
		final EditText categories_new_text = (EditText) this.findViewById(R.id.tasks_leftmenu_categories_new_text);
		final ImageButton categories_new_button = (ImageButton) this.findViewById(R.id.tasks_leftmenu_categories_new_button);
		categories_new_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String lCategoryText = categories_new_text.getText().toString();
				if (lCategoryText == null || lCategoryText.isEmpty()) {
					return;
				}

				if (categoryDAO.getByName(lCategoryText) != null) {
					Toast.makeText(TasksActivity.this, R.string.tasks_categories_create_name_already_used, Toast.LENGTH_LONG).show();
					return;
				}
				Category lCategory = new Category();
				lCategory.setLastUpdate(System.currentTimeMillis());
				lCategory.setName(lCategoryText);
				categoryDAO.save(lCategory);
				lCategory.setUuid(ConfigurationHelper.getDeviceId() + "_" + lCategory.getIdCategory());
				categoryDAO.save(lCategory);

				categories.add(lCategory);
				
				actualiseCategoriesList();
			}
		});
	}

	private void changeCategory(Category lNewCategory) {
		if (lNewCategory == null) {
			currentCategory = null;
			currentTasks.addAll(tasks);
		} else {
			currentCategory = lNewCategory;
		}
		actualiseCategoriesList();
		actualiseTasksList();
	}

	public void onBackPressed() {
		finish();
		return;
	}
}