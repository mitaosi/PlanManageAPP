package com.example.abc;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class dataBase extends SQLiteOpenHelper{

	private static String DATABASE_NAME="scheme.db";
	private static String TABLE_NAME="project";
	private static String ID="project_id";
	private static String TOPIC="topic";
	private static String DETAIL="detail";
	private static String DATE="date";
	private static String TIME="time";
	private static String PRIORITY="priority";
	private static String PERIOD="period";
	private static String STATE="state";


	public dataBase(Context context) {
		super(context, DATABASE_NAME, null,1);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + ID
				+ " INTEGER primary key autoincrement, "+ PERIOD +" text," + TOPIC + " text, "+ DATE +" text,"
				+ TIME +" text,"+ DETAIL +" text,"+ PRIORITY +" text,"+ STATE +" text);";
		db.execSQL(sql);
		db.execSQL(sql);

		Log.d("check",TABLE_NAME);
	}



	public long insert(String period,String topic,String date,String time,
					   String priority,String detail,String state)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(PERIOD, period);
		cv.put(TOPIC, topic);
		cv.put(DATE, date);
		cv.put(TIME, time);
		cv.put(DETAIL, detail);
		cv.put(PRIORITY, priority);
		cv.put(STATE, state);

		long row= db.insert(TABLE_NAME, null, cv);
		return row;
	}



	public void delete(String id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = ID + " = ?";
		String[] whereValue={id};
		db.delete(TABLE_NAME, where, whereValue);
	}

	public void update(String id,String period,String topic,String date,String time,
					   String priority,String detail)

	{
		SQLiteDatabase db = this.getWritableDatabase();

		String where = ID + " = ?";

		String[] whereValue = {id};

		ContentValues cv = new ContentValues();

		cv.put(PERIOD, period);
		cv.put(TOPIC, topic);
		cv.put(DATE, date);
		cv.put(TIME, time);
		cv.put(DETAIL, detail);
		cv.put(PRIORITY, priority);
		//cv.put(STATE, state);

		db.update(TABLE_NAME, cv, where, whereValue);
	}

	public void modifyState(String id,String state)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		String where = ID + " = ?";

		String[] whereValue = {id};

		ContentValues cv = new ContentValues();

		cv.put(STATE, state);

		db.update(TABLE_NAME, cv, where, whereValue);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.d("executing","insert initsql");
		String sql="DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);

	}

	public Cursor select()
	{
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.query(TABLE_NAME,  null, null, null, null, null, null);
		return cursor;
	}

	public Cursor select(String id)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		String Selection= ID + " = ?";
		String[] selectionArgs={id};
		Cursor cursor=db.query(TABLE_NAME, null, Selection, selectionArgs, null, null, null);
		return cursor;
	}

}
