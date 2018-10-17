package com.example.abc;
/**
 * Activity handles information when create new plan
 * next_layout.xml is the corresponding layout
 */
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class next_activity extends Activity {

	ArrayAdapter aa;
	ProgressBar pb;
	Spinner showpriority;
	Button choosedate,choosetime,finish,back;
	EditText showtopic,showtime,showdate,showdetail;
	Calendar c;
	String getperiod="",gettopic="",getdate="",gettime="",getpriority="",getdetail="";
	dataBase data;
	Cursor cursor;

	Dialog dialog=null;
	private final static int DATE_DIALOG = 0;
	private final static int TIME_DIALOG = 1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.next_layout);
		pb=(ProgressBar)findViewById(R.id.progressBar1);
		setTitle("Please write details of plan");
		data=new dataBase(this);
		cursor=data.select();
		cursor.moveToFirst();
		choosedate=(Button)findViewById(R.id.choosedate);
		choosedate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG);
			}
		});

		choosetime=(Button)findViewById(R.id.choosetime);
		choosetime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Calendar c=Calendar.getInstance();  //show the time
//				String st1=c.getTime().toString();
//				showtime.setText(st1);
				showDialog(TIME_DIALOG);
			}
		});


		showpriority=(Spinner)findViewById(R.id.spinner1);
		aa=ArrayAdapter.createFromResource(next_activity.this,R.array.setlevel,android.R.layout.simple_dropdown_item_1line);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		showpriority.setAdapter(aa);
		showpriority.setVisibility(View.VISIBLE);
		showpriority.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				// TODO Auto-generated method stub
				getpriority=showpriority.getSelectedItem().toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});



		showtopic=(EditText)findViewById(R.id.edittopic);

		showtime=(EditText)findViewById(R.id.edittime);

		showdate=(EditText)findViewById(R.id.editdate);

		showdetail=(EditText)findViewById(R.id.xiangxi);

		back=(Button)findViewById(R.id.cancel);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder cancelConfirm=new AlertDialog.Builder(next_activity.this);
				cancelConfirm.setTitle("Would you return without saving ?");
				cancelConfirm.setPositiveButton("Return", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent turnback=new Intent(next_activity.this,newproject_activity.class);
						startActivity(turnback);

					}
				});
				cancelConfirm.setNegativeButton("Continue", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						pb.setVisibility(View.INVISIBLE);

					}
				});
				cancelConfirm.show();

			}
		});

		finish=(Button)findViewById(R.id.finish);
		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pb.setVisibility(View.VISIBLE);
				Bundle getperiodbundle=getIntent().getExtras();
				Log.d("check", getperiodbundle.getString("period"));
				getperiod=getperiodbundle.getString("period");
				gettopic=showtopic.getText().toString();
				getdate=showdate.getText().toString();
				gettime=showtime.getText().toString();
				getdetail=showdetail.getText().toString();
				String state="unfinished";

				add(getperiod, gettopic, getdate, gettime, getpriority, getdetail, state);

				Toast.makeText(getApplicationContext(),"New Plan created??",Toast.LENGTH_SHORT).show();

				Intent toboss=new Intent(next_activity.this,main_activity.class);
				startActivity(toboss);

			}
		});


	}

	public void add(String period,String topic,String date,String time,
					String priority,String detail,String state)
	{
		if(period.equals("")||topic.equals("")||date.equals("")||time.equals("")||priority.equals(""))
		{
			return;
		}
		else
		{
			data.insert(period, topic, date, time, priority, detail, state);
			cursor.requery();
		}
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
								showdate.setText( year + "/" + (month+1) + "/" + dayOfMonth );
								//showdate.setText( year + "Year" + (month+1) + "Month" + dayOfMonth + "Day");
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
}
