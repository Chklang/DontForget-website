/**
 * 
 */
package fr.chklang.dontforget.android.layout;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import fr.chklang.dontforget.android.R;

/**
 * @author Chklang
 *
 */
public class TasksActivityLayout {

	private final DrawerLayout mainView;
	
	private final ViewGroup leftMenuContainer;
	private final TextView textCategorySelected;
	private final ImageButton buttonOpenLeftMenu;
	
	private final View buttonInprogress;
	private final View buttonFinished;
	private final View buttonDeleted;
	private final View buttonAll;

	private final EditText tasks_search_text;
	private final View tasks_search_button;
	private final EditText tasks_new_text;
	private final View tasks_new_button;
	private final ListView tasks_view;
	
	public TasksActivityLayout(View pView) {
		mainView = (DrawerLayout) pView;

		leftMenuContainer = (ViewGroup) mainView.findViewById(R.id.tasks_leftmenu);

		textCategorySelected = (TextView) mainView.findViewById(R.id.tasks_category_selected);
		buttonOpenLeftMenu = (ImageButton) mainView.findViewById(R.id.tasks_open_menu);
		
		buttonInprogress = mainView.findViewById(R.id.tasks_inprogress);
		buttonFinished = mainView.findViewById(R.id.tasks_finished);
		buttonDeleted = mainView.findViewById(R.id.tasks_deleted);
		buttonAll = mainView.findViewById(R.id.tasks_all);

		tasks_search_text = (EditText) mainView.findViewById(R.id.tasks_search_text);
		tasks_search_button = mainView.findViewById(R.id.tasks_search_button);
		tasks_new_text = (EditText) mainView.findViewById(R.id.tasks_new_text);
		tasks_new_button = mainView.findViewById(R.id.tasks_new_button);
		tasks_view = (ListView) mainView.findViewById(R.id.tasks_elements);
	}

	/**
	 * @return the mainView
	 */
	public DrawerLayout getMainView() {
		return mainView;
	}

	/**
	 * @return the leftMenuContainer
	 */
	public ViewGroup getLeftMenuContainer() {
		return leftMenuContainer;
	}

	/**
	 * @return the textCategorySelected
	 */
	public TextView getTextCategorySelected() {
		return textCategorySelected;
	}

	/**
	 * @return the buttonOpenLeftMenu
	 */
	public ImageButton getButtonOpenLeftMenu() {
		return buttonOpenLeftMenu;
	}

	/**
	 * @return the buttonInprogress
	 */
	public View getButtonInprogress() {
		return buttonInprogress;
	}

	/**
	 * @return the buttonFinished
	 */
	public View getButtonFinished() {
		return buttonFinished;
	}

	/**
	 * @return the buttonDeleted
	 */
	public View getButtonDeleted() {
		return buttonDeleted;
	}

	/**
	 * @return the buttonAll
	 */
	public View getButtonAll() {
		return buttonAll;
	}

	/**
	 * @return the tasks_search_text
	 */
	public EditText getTasks_search_text() {
		return tasks_search_text;
	}

	/**
	 * @return the tasks_search_button
	 */
	public View getTasks_search_button() {
		return tasks_search_button;
	}

	/**
	 * @return the tasks_new_text
	 */
	public EditText getTasks_new_text() {
		return tasks_new_text;
	}

	/**
	 * @return the tasks_new_button
	 */
	public View getTasks_new_button() {
		return tasks_new_button;
	}

	/**
	 * @return the tasks_view
	 */
	public ListView getTasks_view() {
		return tasks_view;
	}
}
