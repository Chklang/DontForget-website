package fr.chklang.dontforget.android.adapter;

import java.util.ArrayList;
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
import android.widget.TextView;
import fr.chklang.dontforget.android.R;
import fr.chklang.dontforget.android.business.Place;
import fr.chklang.dontforget.android.business.Tag;
import fr.chklang.dontforget.android.business.Task;
import fr.chklang.dontforget.android.database.DatabaseManager;

/**
 * 
 * @author Chklang
 *
 */
public abstract class TasksTaskListAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<Task> tasks;

	public TasksTaskListAdapter(Context pContext, List<Task> pTasks) {
		context = pContext;
		tasks = pTasks;
	}
	
	protected abstract void onRefreshTasksList();
	
	protected abstract void onValidateTask(Task pTask);
	
	protected abstract void onTrashTask(Task pTask);
	
	protected abstract void onDeleteTask(Task pTask);
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup linearLayout;
		ViewGroup lLayoutBadges = null;
		TextView title = null;
		ImageButton lValidateButton = null;
		ImageButton lTrashButton = null;
		ImageButton lDeleteButton = null;

		final Task lCurrentTask = tasks.get(position);

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
				onValidateTask(lCurrentTask);
			}
		});
		lTrashButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onTrashTask(lCurrentTask);
			}
		});
		lDeleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onDeleteTask(lCurrentTask);
			}
		});
		
		lLayoutBadges.removeAllViews();
		
		final Collection<Tag> lTags = new ArrayList<Tag>();
		final Collection<Place> lPlaces = new ArrayList<Place>();

		DatabaseManager.transaction(context, new DatabaseManager.Transaction() {
			@Override
			public void execute() {
				lTags.addAll(Tag.dao.getTagsOfTask(lCurrentTask));
				lPlaces.addAll(Place.dao.getPlacesOfTask(lCurrentTask));
			}
		});
		
		//Add badges for tags
		for (Tag lTag : lTags) {
			View lTextLayout = mInflater.inflate(R.layout.activity_tasks_entry_badge, parent, false);
			TextView lTextView = (TextView) lTextLayout.findViewById(R.id.badge);
			lTextView.setText("#" + lTag.getName());
			lLayoutBadges.addView(lTextLayout);
		}
		
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
		return tasks.get(position).getIdTask();
	}

	@Override
	public String getItem(int position) {
		return tasks.get(position).getName();
	}

	@Override
	public int getCount() {
		return tasks.size();
	}
}
