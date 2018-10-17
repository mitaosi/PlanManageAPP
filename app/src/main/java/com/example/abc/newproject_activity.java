package com.example.abc;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class newproject_activity extends Activity{
	ImageView back,next;
	RadioButton longperiod,middleperiod,shortperiod;
	RadioGroup periodpanel;
	String result="";
	ProgressBar pb;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.newproject_layout);
		pb=(ProgressBar)findViewById(R.id.progressBar1);
		setTitle("choose the range of plan");
		longperiod=(RadioButton)findViewById(R.id.radioButton1);
		middleperiod=(RadioButton)findViewById(R.id.radioButton2);
		shortperiod=(RadioButton)findViewById(R.id.radioButton3);
		periodpanel=(RadioGroup)findViewById(R.id.radiogroup);

		periodpanel.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(R.id.radioButton1==checkedId)
					result=longperiod.getText().toString();
				if(R.id.radioButton2==checkedId)
					result=middleperiod.getText().toString();
				if(R.id.radioButton3==checkedId)
					result=shortperiod.getText().toString();
				// TODO Auto-generated method stub
				Log.d("show",result);
			}
		});


		back=(ImageView)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pb.setVisibility(View.VISIBLE);
				Intent turnback=new Intent(newproject_activity.this,main_activity.class);
				startActivity(turnback);
			}
		});


		next=(ImageView)findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pb.setVisibility(View.VISIBLE);
				Intent turnToNext=new Intent(newproject_activity.this,next_activity.class);
				Bundle sentPeriod=new Bundle();
				sentPeriod.putString("period",result);
				turnToNext.putExtras(sentPeriod);
				startActivity(turnToNext);
			}
		});
	}

}
