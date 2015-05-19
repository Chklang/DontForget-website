package fr.chklang.dontforget.android.adapter;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import fr.chklang.dontforget.android.R;
import fr.chklang.dontforget.android.business.Category;
import fr.chklang.dontforget.android.business.Task;

/**
 * 
 * @author Chklang
 *
 */
public abstract class TasksCategoriesListAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<Category> categories;

	public TasksCategoriesListAdapter(Context pContext, List<Category> pCategories) {
		context = pContext;
		categories = pCategories;
	}
	
	protected abstract String getLabelAllCategories();
	
	protected abstract void onChangeCategory(Category pCategory);
	
	protected abstract void onDeleteCategory(Category pCategory);
	
	protected abstract void onRefreshCategoriesList();
	
	protected abstract void onRefreshTasksList();
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
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
			lText.setText(getLabelAllCategories());
			lButtonDelete.setVisibility(View.INVISIBLE);
		} else {
			lText.setText(lCategory.getName());
			lButtonDelete.setVisibility(View.VISIBLE);
			lButtonDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Collection<Task> lTasksOfThisCategory = Task.dao.findByCategory(lCategory);
					if (!lTasksOfThisCategory.isEmpty()) {
						Toast.makeText(context, R.string.tasks_categories_delete_tasks_associated, Toast.LENGTH_LONG).show();
						return;
					}
					onDeleteCategory(lCategory);
					Category.dao.delete(lCategory.getIdCategory());
					categories.remove(lCategory);
					onRefreshCategoriesList();
					onRefreshTasksList();
				}
			});
		}
		lParent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onChangeCategory(lCategory);
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
			return getLabelAllCategories();
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
}
