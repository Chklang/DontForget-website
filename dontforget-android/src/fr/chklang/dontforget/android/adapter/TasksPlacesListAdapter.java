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
import fr.chklang.dontforget.android.business.Place;

/**
 * 
 * @author Chklang
 *
 */
public abstract class TasksPlacesListAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<Place> places;

	public TasksPlacesListAdapter(Context pContext, List<Place> pPlaces) {
		context = pContext;
		places = pPlaces;
	}
	
	protected abstract void onRefreshPlacesList();
	
	protected abstract void onRefreshTasksList();
	
	protected abstract void onSelectPlace(Place pPlace);
	
	protected abstract void onDeletePlace(Place pPlace);
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final LinearLayout lParent;
		final TextView lText;
		final ImageButton lButtonDelete;
		final Place lPlace;

		if (convertView == null) {
			lParent = (LinearLayout) mInflater.inflate(R.layout.activity_tasks_leftmenu_places_entry, parent, false);
		} else {
			lParent = (LinearLayout) convertView;
		}

		lText = (TextView) lParent.findViewById(R.id.tasks_leftmenu_places_entry_text);
		lButtonDelete = (ImageButton) lParent.findViewById(R.id.tasks_leftmenu_places_entry_delete);

		lPlace = getCurrentPlace(position);

		lText.setText(lPlace.getName());
		lButtonDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onDeletePlace(lPlace);
			}
		});
		lParent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSelectPlace(lPlace);
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
}
