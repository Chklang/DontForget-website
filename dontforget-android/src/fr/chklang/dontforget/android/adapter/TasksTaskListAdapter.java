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
	
	protected abstract void onRestoreTask(Task pTask);
	
	protected abstract void onValidateTask(Task pTask);
	
	protected abstract void onTrashTask(Task pTask);
	
	protected abstract void onDeleteTask(Task pTask);
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewGroup linearLayout;
		final ViewGroup lLayoutBadges;
		final TextView title;
		final ImageButton lRestoreButton;
		final ImageButton lValidateButton;
		final ImageButton lTrashButton;
		final ImageButton lDeleteButton;

		final Task lCurrentTask = tasks.get(position);

		if (convertView == null) {
			View view = mInflater.inflate(R.layout.activity_tasks_entry, parent, false);
			linearLayout = (ViewGroup) view;
		} else {
			View view = convertView;
			linearLayout = (ViewGroup) view;
		}

		title = (TextView) linearLayout.findViewById(R.id.title_task);
		lRestoreButton = (ImageButton) linearLayout.findViewById(R.id.restore);
		lValidateButton = (ImageButton) linearLayout.findViewById(R.id.validate);
		lTrashButton = (ImageButton) linearLayout.findViewById(R.id.trash);
		lDeleteButton = (ImageButton) linearLayout.findViewById(R.id.delete);
		lLayoutBadges = (ViewGroup) linearLayout.findViewById(R.id.task_badges);
		
		final Runnable lSetButtonsVisibility = new Runnable() {
			@Override
			public void run() {
				switch (lCurrentTask.getStatus()) {
				case OPENED:
					lRestoreButton.setVisibility(View.GONE);
					lValidateButton.setVisibility(View.VISIBLE);
					lTrashButton.setVisibility(View.GONE);
					lDeleteButton.setVisibility(View.GONE);
					break;
				case FINISHED:
					lRestoreButton.setVisibility(View.VISIBLE);
					lValidateButton.setVisibility(View.GONE);
					lTrashButton.setVisibility(View.VISIBLE);
					lDeleteButton.setVisibility(View.GONE);
					break;
				case DELETED:
					lRestoreButton.setVisibility(View.GONE);
					lValidateButton.setVisibility(View.VISIBLE);
					lTrashButton.setVisibility(View.GONE);
					lDeleteButton.setVisibility(View.VISIBLE);
					break;
				}
			}
		};

		title.setText(lCurrentTask.getName());

		// Enable buttons
		lRestoreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onRestoreTask(lCurrentTask);
				lSetButtonsVisibility.run();
			}
		});
		lValidateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onValidateTask(lCurrentTask);
				lSetButtonsVisibility.run();
			}
		});
		lTrashButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onTrashTask(lCurrentTask);
				lSetButtonsVisibility.run();
			}
		});
		lDeleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lSetButtonsVisibility.run();
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
		lSetButtonsVisibility.run();
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
