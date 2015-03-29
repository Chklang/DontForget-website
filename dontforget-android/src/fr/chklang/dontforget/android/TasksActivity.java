package fr.chklang.dontforget.android;

import java.util.ArrayList;
import java.util.Collection;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.chklang.dontforget.android.dto.CategoryDTO;
import fr.chklang.dontforget.android.dto.TaskDTO;
import fr.chklang.dontforget.android.services.AbstractService.Result;
import fr.chklang.dontforget.android.services.CategoriesService;
import fr.chklang.dontforget.android.services.TasksService;

@SuppressWarnings("deprecation")
public class TasksActivity extends Activity {
	private String categories_ALL;

	private List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
	private List<TaskDTO> tasks = new ArrayList<TaskDTO>();
	private List<TaskDTO> currentTasks = new ArrayList<TaskDTO>();

	private BaseAdapter tasksAdapter;
	private BaseAdapter categoriesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);

		categories_ALL = getResources().getString(R.string.categories_all);

		setCategoriesAdapter();
		setTasksAdapter();
		refreshCategories();
	}

	private void refreshCategories() {
		Result<Collection<CategoryDTO>> lResult = CategoriesService.getAll();
		categories.clear();
		categories.addAll(lResult.get());
		tasks.clear();
		tasks.addAll(TasksService.getAll().get());
		categoriesAdapter.notifyDataSetChanged();
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

	private void setTasksAdapter() {
		tasksAdapter = new BaseAdapter() {
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
				return currentTasks.get(position).getId();
			}

			@Override
			public String getItem(int position) {
				return currentTasks.get(position).getText();
			}

			@Override
			public int getCount() {
				return currentTasks.size();
			}
		};
		ListView tasks_view = (ListView) findViewById(R.id.tasks_elements);
		tasks_view.setAdapter(tasksAdapter);
	}

	private void setCategoriesAdapter() {
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
				return categories.get(position - 1).getId();
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
		currentTasks.clear();
		if (pIndex == 0) {
			currentTasks.addAll(tasks);
		} else {
			CategoryDTO lCategory = categories.get(pIndex - 1);
			for (TaskDTO lTask : tasks) {
				if (lCategory.getName().equals(lTask.getCategoryName())) {
					currentTasks.add(lTask);
				}
			}
		}
		tasksAdapter.notifyDataSetChanged();
	}
}