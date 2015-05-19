package fr.chklang.dontforget.android.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import fr.chklang.dontforget.android.R;
import fr.chklang.dontforget.android.controller.TasksController;
import fr.chklang.dontforget.android.layout.TasksActivityLayout;
import fr.chklang.dontforget.android.layout.TasksActivityLeftMenuLayout;

public class TasksActivity extends Activity {
	
	private TasksActivityLayout mainLayout;
	private TasksActivityLeftMenuLayout leftMenuLayout;
	
	private TasksController tasksController;
	
	public TasksActivity() {
		super();
		tasksController = new TasksController(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);
		
		mainLayout = new TasksActivityLayout(this.findViewById(R.id.tasks_menu_layout));
		
		ViewGroup lLeftMenuContainer = mainLayout.getLeftMenuContainer();
		
		//Attach left menu to navigation drawer
		LayoutInflater mInflater = (LayoutInflater) TasksActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		leftMenuLayout = new TasksActivityLeftMenuLayout(mInflater.inflate(R.layout.activity_tasks_left_menu, lLeftMenuContainer, false));
		lLeftMenuContainer.addView(leftMenuLayout.getMainView());
		
		//Enable controller
		tasksController.start();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}
	

	@Override
	public void onBackPressed() {
		finish();
		return;
	}

	/**
	 * @return the mainLayout
	 */
	public TasksActivityLayout getMainLayout() {
		return mainLayout;
	}

	/**
	 * @return the leftMenuLayout
	 */
	public TasksActivityLeftMenuLayout getLeftMenuLayout() {
		return leftMenuLayout;
	}

	/**
	 * @return the tasksController
	 */
	public TasksController getTasksController() {
		return tasksController;
	}
}