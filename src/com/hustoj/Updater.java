package com.hustoj;

import android.widget.Button;
import android.widget.EditText;

public class Updater implements Runnable {

	private MainActivity activity;
	private String text;

	public Updater(MainActivity activity,String text) {
		// TODO Auto-generated constructor stub
		this.activity=activity;
		this.text=text;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		EditText console=(EditText) activity.findViewById(R.id.editOut);
		Button run=(Button) activity.findViewById(R.id.btRun);
		run.setEnabled(true);
		console.setText(text);
	}

}
