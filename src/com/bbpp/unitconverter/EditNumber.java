package com.bbpp.unitconverter;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditNumber extends EditText {

	private double value = 0;

	public EditNumber(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public EditNumber(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public EditNumber(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Canvas canvas) {

		if (!isFocused()) {		
			int length = (int) ((getWidth() / getPaint().measureText("8")));
			String sv = double2String(value, length);
			if (!sv.equals((getText().toString()))) {
				//	System.out.println(sv + ":" + getText() + ":" + sv.equals(getText()));
				setText(sv);				
			}
		}
		super.draw(canvas);
	}

	public void setNumber(double value) {
		this.value = value;
		invalidate();
	}

	private String double2String(double value, int length) {

		int tl;

		length -= 2;
		if (length > 15)
			length = 15;

		String pattern="";

		for (int i = 0; i < 2; ++i)
			pattern += "@";

		DecimalFormat format = new DecimalFormat(pattern);
		String text = format.format(value);

		tl = text.substring(0, 1).equals("-") ? text.length() - 1 : text.length();

		if (tl <= length) {
			while  (tl <= length) {	
				pattern += "@";			
				format.applyPattern(pattern);
				text = format.format(value);
				tl = text.substring(0, 1).equals("-") ? text.length() - 1 : text.length();
			}

			tl = text.substring(0, 1).equals("-") ? text.length() - 1 : text.length();
			while (text.contains(".") && (text.substring(text.length()- 1).equals("0") || tl > length)) {
				tl = text.substring(0, 1).equals("-") ? text.length() - 1 : text.length();
				text = text.substring(0, text.length()- 1);
			}

			if (text.substring(text.length()- 1).equals(".")) {
				text = text.substring(0, text.length()- 1);
			}


			//tl = text.substring(0, 1).equals("-") ? text.length() - 1 : text.length();		
		} else {
			//if (tl > length) {
			//System.out.println(text + ":" + tl);
			pattern = "0.";
			String pattern2 = "";
			for (int i = 0; i < length; ++i)
				pattern2 += "#";
			pattern += pattern2 + "E0";
			format.applyPattern(pattern);
			text = format.format(value);
			tl = text.substring(0, 1).equals("-") ? text.length() - 1 : text.length();
			while (tl > length && pattern2.length() > 1) {				
				pattern2 = pattern2.substring(1);
				pattern = "0." + pattern2 + "E0";
				format.applyPattern(pattern);
				text = format.format(value);
				tl = text.substring(0, 1).equals("-") ? text.length() - 1 : text.length();
			}			
		}		

		return text;
	}



}
