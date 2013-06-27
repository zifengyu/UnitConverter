package com.bbpp.unitconverter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DrawerListFrame extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.drawer_list, null);		
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
		String[] items = getResources().getStringArray(R.array.planets_array);

		for (int i = 0; i < items.length; i++) {
			adapter.add(new DrawListItem(items[i]));
		}

		setListAdapter(adapter);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (MainActivity.mainActivity != null) {
			MainActivity.mainActivity.selectItem(position);
		}
		super.onListItemClick(l, v, position, id);
	}

	private class DrawListItem {
		public String tag;		
		public DrawListItem(String tag) {
			this.tag = tag;

		}
	}

	public class SampleAdapter extends ArrayAdapter<DrawListItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item, null);
			}			
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);
			return convertView;
		}


	}

}
