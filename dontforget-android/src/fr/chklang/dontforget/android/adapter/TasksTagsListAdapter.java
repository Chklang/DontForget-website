package fr.chklang.dontforget.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.chklang.dontforget.android.R;
import fr.chklang.dontforget.android.business.Tag;

/**
 * 
 * @author Chklang
 *
 */
public abstract class TasksTagsListAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<Tag> tags;

	public TasksTagsListAdapter(Context pContext, List<Tag> pTags) {
		context = pContext;
		tags = pTags;
	}
	
	protected abstract void onRefreshTagsList();
	
	protected abstract void onRefreshTasksList();
	
	protected abstract void onSelectTag(Tag pTag);
	
	protected abstract void onDeleteTag(Tag pTag);
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final LinearLayout lParent;
		final TextView lText;
		final ImageButton lButtonDelete;
		final Tag lTag;

		if (convertView == null) {
			lParent = (LinearLayout) mInflater.inflate(R.layout.activity_tasks_leftmenu_tags_entry, parent, false);
		} else {
			lParent = (LinearLayout) convertView;
		}

		lText = (TextView) lParent.findViewById(R.id.tasks_leftmenu_tags_entry_text);
		lButtonDelete = (ImageButton) lParent.findViewById(R.id.tasks_leftmenu_tags_entry_delete);

		lTag = getCurrentTag(position);

		lText.setText(lTag.getName());
		lButtonDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onDeleteTag(lTag);
			}
		});
		lParent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSelectTag(lTag);
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
}
