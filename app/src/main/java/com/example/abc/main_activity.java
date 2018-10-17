package com.example.abc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ContextMenu;


public class main_activity extends Activity {
	Button createNew,btest;
	EditText test;
	ListView list;


	Cursor cursor,cursorInfo;
	dataBase data;
	List<Map<String,String>> listitems;
	projectAdapter listViewAdapter;
	LayoutInflater listContainer;


	TextView topicInfo,periodinfo,dateInfo,
			timeInfo,priorityInfo,detailInfo;

	protected final static int MENU_DETAIL = Menu.FIRST;
	protected final static int MENU_DELETE = Menu.FIRST + 1;
	protected final static int MENU_UPDATE = Menu.FIRST + 2;
	protected final static int MENU_EXIT = Menu.FIRST + 3;


	ProgressBar pb;
	String topic,dateandtime;
	String ItemID,state;
	String getListID="2";
	int checkid,mposition,getPosition;
	String SWITCH="cm";

	Bundle gettest,obtaintopic,obtaindateandtime,obtainpriority;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		setTitle("My Personal Plans");
		pb=(ProgressBar)findViewById(R.id.progressBar1);


		data=new dataBase(this);
		cursor=data.select();
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			Log.d("OUTPUT", ""+cursor.getString(1));
			cursor.moveToNext();
		}
		cursor.moveToFirst();


		list=(ListView)findViewById(R.id.projectList);
		listitems=getListItems();
		listViewAdapter=new projectAdapter(listitems, this);

		list.setAdapter(listViewAdapter);

		this.registerForContextMenu(list);



		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				getListID=listitems.get(arg2).get("ID");
				Log.d("get database ID",""+getListID);

				Log.d("get listview position",""+arg2);
				mposition=arg2;
				SWITCH="om";
				//	int i=Integer.parseInt(s);
			}
		});


		createNew=(Button)findViewById(R.id.cancel);
		createNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pb.setVisibility(View.VISIBLE);
				Intent turn=new Intent(main_activity.this,newproject_activity.class);
				startActivity(turn);
			}

		});
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		Log.v("test", "populate context menu");

		menu.setHeaderTitle("Choose");
		menu.setHeaderIcon(R.drawable.ic_launcher);

		menu.add(0, 1, Menu.NONE, "Details");
		menu.add(0, 2, Menu.NONE, "Delete");
		menu.add(0, 3, Menu.NONE, "Edit");
		menu.add(0, 4, Menu.NONE, "Cancel");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		super.onContextItemSelected(item);
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		ItemID=listitems.get(menuInfo.position).get("ID");
		getPosition=menuInfo.position;
		SWITCH="cm";

		switch(item.getItemId()) {
			case 1:
				pb.setVisibility(View.VISIBLE);
				showdetail(ItemID);

				break;
			case 2:
				Log.d("getlistID",getListID);
				Log.d("itemID",ItemID);
				delete(ItemID);

				break;
			case 3:
				edit(ItemID);
				break;

			default:
				return super.onContextItemSelected(item);
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_DETAIL, 0, "info").setIcon(R.drawable.detailinfo);
		menu.add(Menu.NONE, MENU_DELETE, 0, "delete").setIcon(R.drawable.deleteitem);
		menu.add(Menu.NONE, MENU_UPDATE, 0, "edit").setIcon(R.drawable.edititem);
		menu.add(Menu.NONE, MENU_EXIT, 0, "quit").setIcon(R.drawable.exit);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);

		switch (item.getItemId())
		{
			case MENU_DETAIL:
				showdetail(getListID);
				break;
			case MENU_DELETE:
				Log.d("getlistID",getListID);
				delete(getListID);
				break;
			case MENU_UPDATE:
				edit(getListID);
				Log.d("update",""+item.getItemId());
			case MENU_EXIT:
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				android.os.Process.killProcess(android.os.Process.myPid());

				break;
		}
		return true;
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


	List<Map<String,String>> getListItems(){

		List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();

		listItems.clear();

		for(int i=0;i<cursor.getCount();i++){

			Map<String,String> map = new HashMap<String,String>();

			map.put("ID", ""+cursor.getString(0));

			map.put("priority&topic", ""+"["+cursor.getString(6)+"]"+cursor.getString(2));

			map.put("date&time",""+cursor.getString(3)+","+cursor.getString(4));

			map.put("state", cursor.getString(7));

			cursor.moveToNext();

			listItems.add(map);
		}
		cursor.moveToFirst();

		return listItems;
	}


	public void showdetail(String getListID)
	{
		LayoutInflater usedisplayinfoxml=LayoutInflater.from(main_activity.this);
		final View displayview=usedisplayinfoxml.inflate(R.layout.infotipsview_layout, null);

		topicInfo=(TextView)displayview.findViewById(R.id.topicInfo);
		periodinfo=(TextView)displayview.findViewById(R.id.periodinfo);
		dateInfo=(TextView)displayview.findViewById(R.id.dateInfo);
		timeInfo=(TextView)displayview.findViewById(R.id.timeInfo);
		priorityInfo=(TextView)displayview.findViewById(R.id.priorityInfo);
		detailInfo=(TextView)displayview.findViewById(R.id.detailInfo);

		cursorInfo=data.select(getListID);
		cursorInfo.moveToFirst();
		Log.d("CURSORINFO",cursorInfo.getString(2));

		topicInfo.setText("Theme:"+cursorInfo.getString(2));
		periodinfo.setText("Period:"+cursorInfo.getString(1));
		dateInfo.setText("Date:"+cursorInfo.getString(3));
		timeInfo.setText("Time:"+cursorInfo.getString(4));
		priorityInfo.setText("Priority:"+cursorInfo.getString(6));
		detailInfo.setText("Remark:"+cursorInfo.getString(5));

		AlertDialog.Builder displayInfo=new AlertDialog.Builder(main_activity.this);
		displayInfo.setView(displayview);
		displayInfo.setTitle("My Plan");
		displayInfo.setIcon(R.drawable.ic_launcher);

		displayInfo.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {           //设置消息窗口确定键的监听事件

			@Override
			public void onClick(DialogInterface dialog, int which) {

				pb.setVisibility(View.INVISIBLE);
			}
		});
		displayInfo.show();
	}


	public void edit(String ItemID)
	{
		Intent intentToEdit=new Intent(main_activity.this,editPage_activity.class);
		Bundle sentId=new Bundle();
		sentId.putString("ID", ItemID);
		intentToEdit.putExtras(sentId);
		startActivity(intentToEdit);
	}


	public void delete(String getListID)
	{
		final String getlistid=getListID;
		Log.d("test issss ",""+ItemID);
		AlertDialog.Builder cancelConfirm=new AlertDialog.Builder(main_activity.this);
		cancelConfirm.setTitle("Would you delete this？");
		cancelConfirm.setPositiveButton("Cancel", new DialogInterface.OnClickListener() { //取消删除

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(),"Success canceled！",Toast.LENGTH_SHORT).show();
			}
		});

		cancelConfirm.setNegativeButton("DELETE", new DialogInterface.OnClickListener() { //确定删除

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("get list IDDDDDDDD",getlistid);
				Log.d("get list getPoooooo",""+getPosition);
				Log.d("get list mPPPPPPPP",""+mposition);

				data.delete(getlistid);
				if(SWITCH.equals("om"))
				{
					listitems.remove(mposition);
				}
				if(SWITCH.equals("cm"))
				{
					listitems.remove(getPosition);
					Log.d("cm",SWITCH);
				}
				listViewAdapter.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(),"Deleted！",Toast.LENGTH_SHORT).show();

			}
		});
		cancelConfirm.show();


	}





	public class projectAdapter extends BaseAdapter{

		private List<Map<String,String>> listItems;
		Context context;

		private boolean[] hasChecked;

		public final class ListItemView
		{
			public TextView ID;
			public TextView period;
			public TextView prioritytopic;
			public TextView datetime;
			public TextView detail;
			public CheckBox check;
			public TextView state;

		}

		public projectAdapter(List<Map<String,String>> listItems,Context context)
		{
			this.context=context;
			listContainer=LayoutInflater.from(context);
			this.listItems=listItems;

		}


		@Override
		public int getCount() {

			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		public boolean hasChecked(int checkedID)
		{
			return hasChecked[checkedID];
		}

		public void getID(int checkedID)
		{
			String id= listitems.get(checkedID).get("ID").toString();
			Log.d("checkID",""+id);
		}

		private void checkedChange(int checkedID, boolean isChecked)              //checkbox的状态处理 方法 标记事件是否完成
		{
			if(isChecked==true)
			{
				state="finished";
				data.modifyState(listitems.get(checkedID).get("ID").toString(), state);
				Toast.makeText(getApplicationContext(), "Plan accomplished!", Toast.LENGTH_SHORT).show();

			}

			else
			{
				state="unfinished";
				data.modifyState(listitems.get(checkedID).get("ID").toString(), state);
				Toast.makeText(getApplicationContext(), "Plan hasn't finished yet!", Toast.LENGTH_SHORT).show();

			}
		}



		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final int selectID=position;

			ListItemView listItemView=null;

			if(convertView==null)
			{
				listItemView=new ListItemView();
				convertView=listContainer.inflate(R.layout.listitem_layout, null);
				listItemView.prioritytopic=(TextView)convertView.findViewById(R.id.ItemPriorityTopic);
				listItemView.datetime=(TextView)convertView.findViewById(R.id.ItemDateTime);
				listItemView.check = (CheckBox) convertView.findViewById(R.id.checkBox1);
				convertView.setTag(listItemView);

			}
			else
			{
				listItemView=(ListItemView)convertView.getTag();
			}

			listItemView.prioritytopic.setText((String)listitems.get(position).get("priority&topic"));
			listItemView.datetime.setText((String)listitems.get(position).get("date&time"));

			getID(selectID);

			listItemView.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {          //listitem点击事件

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					checkedChange(selectID,isChecked);

					Log.d("checked is pressed","yeah"+isChecked);

				}
			});
			return convertView;
		}
	}
}
