/**
 * 
 */
package fr.chklang.dontforget.android.layout;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import fr.chklang.dontforget.android.R;

/**
 * @author Chklang
 *
 */
public class TasksActivityLeftMenuLayout {

	private final View mainView;
	
	//Buttons of left menu
	private final View buttonLeftMenuViewCategories;
	private final View buttonLeftMenuViewTags;
	private final View buttonLeftMenuViewPlaces;
	private final View buttonLeftMenuSynchronization;
	
	//Categories
	private final EditText categories_new_text;
	private final ImageButton categories_new_button;
	
	private final View containerCategories;
	private final View containerTags;
	private final View containerPlaces;
	
	private final ListView categories_view;
	private final ListView tags_view;
	private final ListView places_view;
	
	public TasksActivityLeftMenuLayout(View pView) {
		mainView = pView;

		//And get buttons of this menu
		containerCategories = mainView.findViewById(R.id.tasks_leftmenu_menu_categories);
		containerTags = mainView.findViewById(R.id.tasks_leftmenu_menu_tags);
		containerPlaces = mainView.findViewById(R.id.tasks_leftmenu_menu_places);
		
		//Buttons of left menu
		buttonLeftMenuViewCategories = mainView.findViewById(R.id.tasks_leftmenu_view_categories);
		buttonLeftMenuViewTags = mainView.findViewById(R.id.tasks_leftmenu_view_tags);
		buttonLeftMenuViewPlaces = mainView.findViewById(R.id.tasks_leftmenu_view_places);
		buttonLeftMenuSynchronization = mainView.findViewById(R.id.tasks_leftmenu_synchronization);

		categories_view = (ListView) mainView.findViewById(R.id.tasks_leftmenu_categories_list);
		tags_view = (ListView) mainView.findViewById(R.id.tasks_leftmenu_tags_list);
		places_view = (ListView) mainView.findViewById(R.id.tasks_leftmenu_places_list);

		categories_new_text = (EditText) mainView.findViewById(R.id.tasks_leftmenu_categories_new_text);
		categories_new_button = (ImageButton) mainView.findViewById(R.id.tasks_leftmenu_categories_new_button);
	}

	/**
	 * @return the mainView
	 */
	public View getMainView() {
		return mainView;
	}

	/**
	 * @return the buttonLeftMenuViewCategories
	 */
	public View getButtonLeftMenuViewCategories() {
		return buttonLeftMenuViewCategories;
	}

	/**
	 * @return the buttonLeftMenuViewTags
	 */
	public View getButtonLeftMenuViewTags() {
		return buttonLeftMenuViewTags;
	}

	/**
	 * @return the buttonLeftMenuViewPlaces
	 */
	public View getButtonLeftMenuViewPlaces() {
		return buttonLeftMenuViewPlaces;
	}

	/**
	 * @return the buttonLeftMenuSynchronization
	 */
	public View getButtonLeftMenuSynchronization() {
		return buttonLeftMenuSynchronization;
	}

	/**
	 * @return the categories_new_text
	 */
	public EditText getCategories_new_text() {
		return categories_new_text;
	}

	/**
	 * @return the categories_new_button
	 */
	public ImageButton getCategories_new_button() {
		return categories_new_button;
	}

	/**
	 * @return the containerCategories
	 */
	public View getContainerCategories() {
		return containerCategories;
	}

	/**
	 * @return the containerTags
	 */
	public View getContainerTags() {
		return containerTags;
	}

	/**
	 * @return the containerPlaces
	 */
	public View getContainerPlaces() {
		return containerPlaces;
	}

	/**
	 * @return the categories_view
	 */
	public ListView getCategories_view() {
		return categories_view;
	}

	/**
	 * @return the tags_view
	 */
	public ListView getTags_view() {
		return tags_view;
	}

	/**
	 * @return the places_view
	 */
	public ListView getPlaces_view() {
		return places_view;
	}
}
