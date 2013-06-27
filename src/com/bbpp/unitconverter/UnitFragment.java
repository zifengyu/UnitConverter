package com.bbpp.unitconverter;

import java.util.ArrayList;

import cn.waps.AdView;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class UnitFragment extends Fragment {

	public final static String ARG_UNIT_LIST = "com.bbpp.unitcoverter.UNIT_LIST";

	public final static String ARG_SHOW_AD = "com.bbpp.unitcoverter.SHOW_AD";;

	UnitList<?> unitList;
	UnitList<?>[] allUnitList = {
			UnitList.currencyList,
			UnitList.lengthList,
			UnitList.areaList,
			UnitList.volumeList,			 
			UnitList.massList,
			UnitList.temperatureList,
			UnitList.durationList,
			UnitList.velocityList,
			UnitList.angleList,
			UnitList.energyList,
			UnitList.powerList,
			UnitList.pressureList,
			UnitList.forceList,			
	};

	private ArrayList<EditNumber> editTextList = new ArrayList<EditNumber>();

	private ScrollView scrollView;

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

		int listIndex = getArguments().getInt(ARG_UNIT_LIST, 0);		

		if (listIndex >= 0 && listIndex < allUnitList.length) {
			unitList = allUnitList[getArguments().getInt(ARG_UNIT_LIST)];
		}

		editTextList.clear();

		View view = inflater.inflate(R.layout.unit_fragment, container, false);

		scrollView = (ScrollView)view.findViewById(R.id.scroll_view);		
		scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

		TableLayout tableLayout = (TableLayout)(view.findViewById(R.id.unit_table_layout));

		for (int i = 0; i < unitList.size(); ++i) {
			TableRow rowView = (TableRow)inflater.inflate(R.layout.unit_row, tableLayout, false);
			EditNumber text	= (EditNumber) rowView.findViewById(R.id.unit_value);
			TextView label	= (TextView) rowView.findViewById(R.id.unit_label);

			editTextList.add(text);			
			text.setOnFocusChangeListener(new UnitEditListener(i));

			text.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent arg2) {
					scrollView.requestFocus();
					InputMethodManager imm = (InputMethodManager)(container.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
					imm.hideSoftInputFromWindow(v.getWindowToken(),0);

					return true;
				}});

			text.setSelectAllOnFocus(true);

			label.setText(unitList.getLabel(i));			

			tableLayout.addView(rowView);
		}

		updateEditText(-1);

		return view;
	}

	protected void updateEditText(int noPos) {

		for (int i = 0; i < editTextList.size(); ++i) {			
			if (i != noPos) {				
				editTextList.get(i).setNumber(unitList.getValue(i));
			}
		}
	}

	class UnitEditListener implements TextWatcher, OnFocusChangeListener {

		int position;

		public UnitEditListener(int position) {
			this.position = position;			
		}

		@Override
		public void afterTextChanged(Editable newValue) {
			try {
				double dv = Double.parseDouble(newValue.toString());
				if (!Double.isNaN(dv) && !Double.isInfinite(dv)) {
					unitList.setValue(dv, position);
					updateEditText(-1);
				}
			} catch (NumberFormatException ex) {
				unitList.setValue(0, position);
				updateEditText(-1);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				((EditNumber)v).addTextChangedListener(this);
			} else {
				((EditNumber)v).removeTextChangedListener(this);
				double number = 0;
				if (((EditNumber)v).getText().length() != 0) {	
					try {
					number = Double.parseDouble(((EditNumber)v).getText().toString());
					} catch (NumberFormatException ex) {
						number = 0;
					}
				}	
				if (!Double.isNaN(number) && !Double.isInfinite(number)) {
					((EditNumber)v).setNumber(number);
				} else {
					updateEditText(-1);
				}
			}

		}
	}
}
