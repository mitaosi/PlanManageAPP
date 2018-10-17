package com.example.abc;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class editPage_activity extends Activity {


	RadioButton longperiod,middleperiod,shortperiod;
	RadioGroup periodpanel;
	EditText showtopic,showtime,showdate,showdetail;
	Spinner showpriority;
	ArrayAdapter aa;
	Button choosedate,choosetime,save,cancel;
	ProgressBar pb;
	Calendar c;
	String getperiod="",gettopic="",getdate="",gettime="",getpriority="",getdetail="";
	String itemID;
	String showperiod="short period";
	dataBase data;
	Cursor cursor;

	private final static int DATE_DIALOG = 0;
	private final static int TIME_DIALOG = 1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editpage_layout);
		pb=(ProgressBar)findViewById(R.id.progressBar1);
		setTitle("edit plan");
		Bundle getid=getIntent().getExtras();

		itemID=getid.getString("ID");
		Log.d("whether ID has got",itemID);
		data=new dataBase(this);
		cursor=data.select(itemID);
		cursor.moveToFirst();
		Log.d("test the  query cursor",""+cursor.getString(1));

		showEditContent();

	}

	public void showEditContent()
	{
		longperiod=(RadioButton)findViewById(R.id.radioButton2);
		middleperiod=(RadioButton)findViewById(R.id.radioButton3);
		shortperiod=(RadioButton)findViewById(R.id.radioButton1);
		periodpanel=(RadioGroup)findViewById(R.id.radiogroup);

		showtopic=(EditText)findViewById(R.id.edittopic);
		showtime=(EditText)findViewById(R.id.edittime);
		showdate=(EditText)findViewById(R.id.editdate);
		showpriority=(Spinner)findViewById(R.id.spinner1);
		showdetail=(EditText)findViewById(R.id.xiangxi);
		choosedate=(Button)findViewById(R.id.choosedate);
		choosetime=(Button)findViewById(R.id.choosetime);
		save=(Button)findViewById(R.id.edit);

		if(cursor.getString(1).equals("long period"))
		{

			periodpanel.check(R.id.radioButton2);
			Log.d("show","long period");
		}
		if(cursor.getString(1).equals("middle period"))
		{

			periodpanel.check(R.id.radioButton3);

			Log.d("show","middle period");
		}
		if(cursor.getString(1).equals("short period"))
		{

			periodpanel.check(R.id.radioButton1);

			Log.d("show","short period");

		}
		showperiod=cursor.getString(1);


		periodpanel.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				//String result="";
				if(R.id.radioButton2==checkedId)
					showperiod=longperiod.getText().toString();
				else if(R.id.radioButton3==checkedId)
					showperiod=middleperiod.getText().toString();
				else if(R.id.radioButton1==checkedId)
					showperiod=shortperiod.getText().toString();

				// TODO Auto-generated method stub
				Log.d("show",showperiod);
			}
		});

		showtopic.setText(cursor.getString(2));
		showdate.setText(cursor.getString(3));
		choosedate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG);
			}
		});
		showtime.setText(cursor.getString(4));
		choosetime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(TIME_DIALOG);
			}
		});
		showdetail.setText(cursor.getString(5));
		aa=ArrayAdapter.createFromResource(editPage_activity.this,R.array.setlevel,android.R.layout.simple_gallery_item);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		showpriority.setAdapter(aa);

		aa.notifyDataSetChanged();

		if(cursor.getString(6).equals("Urgent"))
			showpriority.setSelection(0);
		else if(cursor.getString(6).equals("Important"))
			showpriority.setSelection(1);
		else
		{
			Log.d("showpriority",cursor.getString(6));
			showpriority.setSelection(2);
		}

		showpriority.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				getpriority=showpriority.getSelectedItem().toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});



		cancel=(Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pb.setVisibility(View.VISIBLE);
				AlertDialog.Builder cancelConfirm=new AlertDialog.Builder(editPage_activity.this);
				cancelConfirm.setTitle("Unsaved!");
				cancelConfirm.setPositiveButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent toboss=new Intent(editPage_activity.this,main_activity.class);
						startActivity(toboss);

					}
				});
				cancelConfirm.setNegativeButton("continue", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						pb.setVisibility(View.INVISIBLE);

					}
				});
				cancelConfirm.show();

			}
		});

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pb.setVisibility(View.VISIBLE);
				getperiod=showperiod;
				Log.d("getperiod",getperiod);
				gettopic=showtopic.getText().toString();
				getdate=showdate.getText().toString();
				gettime=showtime.getText().toString();
				getdetail=showdetail.getText().toString();
				data.update(itemID, getperiod, gettopic, getdate, gettime, getpriority, getdetail);
				Toast.makeText(getApplicationContext(),"change saved??",Toast.LENGTH_SHORT).show();
				Intent toboss=new Intent(editPage_activity.this,main_activity.class);
				startActivity(toboss);
			}
		});
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		c = Calendar.getInstance();
		switch (id) {
			case DATE_DIALOG:

				dialog = new DatePickerDialog(
						this,
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
								showdate.setText( year + "Year" + (month+1) + "Month" + dayOfMonth + "Day");
							}
						},
						c.get(Calendar.YEAR),
						c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH)
				);

				dialog.setTitle(c.get(Calendar.YEAR)+","+c.get(Calendar.MONTH)+","+c.get(Calendar.DAY_OF_MONTH));
				break;
			case TIME_DIALOG:
				dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						showtime.setText(hourOfDay+":"+minute);
					}
				}, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true);
				break;
		}
		return dialog;
	}

	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if((System.currentTimeMillis()-exitTime) > 2000){
				Toast.makeText(getApplicationContext(), "Click again to quit", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
