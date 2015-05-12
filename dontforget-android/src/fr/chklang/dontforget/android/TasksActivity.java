package fr.chklang.dontforget.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.chklang.dontforget.android.business.Category;
import fr.chklang.dontforget.android.business.Task;
import fr.chklang.dontforget.android.dao.CategoryDAO;
import fr.chklang.dontforget.android.dao.TaskDAO;
import fr.chklang.dontforget.android.dto.TaskStatus;

@SuppressWarnings("deprecation")
public class TasksActivity extends Activity {
	private String categories_ALL;

	private List<Category> categories = new ArrayList<Category>();
	private List<Task> tasks = new ArrayList<Task>();
	private List<Task> currentTasks = new ArrayList<Task>();

	private BaseAdapter tasksAdapter;
	private BaseAdapter categoriesAdapter;
	private TaskStatus currentStatus;
	private Category currentCategory;
	
	private CategoryDAO lCategoryDAO = new CategoryDAO();
	private TaskDAO lTaskDAO = new TaskDAO();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);

		categories_ALL = getResources().getString(R.string.categories_all);
		currentStatus = TaskStatus.OPENED;

		initializeActionsButtons();
		initializeCategoriesAdapter();
		initializeTasksAdapter();
		refreshCategories();
		actualiseTasksList();
	}

	private void refreshCategories() {
		categories.clear();
		categories.addAll(lCategoryDAO.getAll());
		tasks.clear();
		tasks.addAll(lTaskDAO.getAll());
		categoriesAdapter.notifyDataSetChanged();
	}
	
	private void initializeActionsButtons() {
		ImageView lButtonInprogress = (ImageView) findViewById(R.id.tasks_inprogress);
		ImageView lButtonFinished = (ImageView) findViewById(R.id.tasks_finished);
		ImageView lButtonDeleted = (ImageView) findViewById(R.id.tasks_deleted);
		ImageView lButtonAll = (ImageView) findViewById(R.id.tasks_all);
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

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(categories_view);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	private void initializeTasksAdapter() {
		tasksAdapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater mInflater = (LayoutInflater)TasksActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        LinearLayout linearLayout;
		        TextView title = null;
		        ImageButton lValidateButton = null;
		        ImageButton lTrashButton = null;
		        ImageButton lDeleteButton = null;

		        if (convertView == null) {
		        	View view = mInflater.inflate(R.layout.activity_tasks_entry, parent, false);
		        	linearLayout = (LinearLayout) view;
		        } else {
		            View view = convertView;
		            linearLayout = (LinearLayout) view;
		        }

		        try {
	                //  Otherwise, find the TextView field within the layout
		        	for (int i=0; i<linearLayout.getChildCount(); i++) {
		        		View lChild = linearLayout.getChildAt(i);
		        		if (lChild.getId() == R.id.title_task) {
		        			title = (TextView) lChild;
		        		} else if (lChild.getId() == R.id.validate) {
		        			lValidateButton = (ImageButton) lChild;
		        		} else if (lChild.getId() == R.id.trash) {
		        			lTrashButton = (ImageButton) lChild;
		        		} else if (lChild.getId() == R.id.delete) {
		        			lDeleteButton = (ImageButton) lChild;
		        		}
		        	}
		        } catch (ClassCastException e) {
		            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
		            throw new IllegalStateException(
		                    "ArrayAdapter requires the resource ID to be a TextView", e);
		        }

		        String item = getItem(position);
	            title.setText(item);
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
				LayoutInflater mInflater = (LayoutInflater)TasksActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        View view;
		        TextView text;

		        if (convertView == null) {
		            view = mInflater.inflate(R.layout.activity_tasks_menu_entry, parent, false);
		        } else {
		            view = convertView;
		        }

		        try {
	                //  Otherwise, find the TextView field within the layout
	                text = (TextView) view;
		        } catch (ClassCastException e) {
		            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
		            throw new IllegalStateException(
		                    "ArrayAdapter requires the resource ID to be a TextView", e);
		        }

		        String item = getItem(position);
	            text.setText(item);
				return text;
			}

			@Override
			public long getItemId(int position) {
				if (position == 0) {
					return -1;
				}
				return categories.get(position - 1).getIdCategory();
			}

			@Override
			public String getItem(int position) {
				if (position == 0) {
					return categories_ALL;
				}
				return categories.get(position - 1).getName();
			}

			@Override
			public int getCount() {
				return categories.size() + 1;
			}
		};
		ListView categories_view = (ListView) findViewById(R.id.tasks_menu_list);
		DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.tasks_menu_layout);
		categories_view.setAdapter(categoriesAdapter);

		// Set the list's click listener
		categories_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				changeCategory(position);
			}
		});
		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				Toast.makeText(TasksActivity.this, "close", Toast.LENGTH_LONG).show();
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				Toast.makeText(TasksActivity.this, "open", Toast.LENGTH_LONG).show();
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void changeCategory(int pIndex) {
		if (pIndex == 0) {
			currentCategory = null;
			currentTasks.addAll(tasks);
		} else {
			Category lCategory = categories.get(pIndex - 1);
			currentCategory = lCategory;
		}
		actualiseTasksList();
	}
	
	public void onBackPressed() {
	    finish();
	    return;  
	}
}