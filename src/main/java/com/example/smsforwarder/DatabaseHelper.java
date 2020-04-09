package com.example.smsforwarder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Forwarding_rules.db";
    private static final int DATABASE_VERSION = 2;

    public static final String RULES_TABLE_NAME="rules";
    public static final String RULES_ID="id";
    public static final String RULES_FROM_NUM ="from_num";
    public static final String RULES_FROM_NUM_NAME ="from_num_name";
    public static final String RULES_TO_NUM ="to_num";
    public static final String RULES_TO_NUM_NAME ="to_num_name";
    public static final String RULES_COND ="cond";
    public static final String RULES_BODY_TEXT ="body_text";
    SmsManager smsManager = SmsManager.getDefault();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String Create_Table="CREATE TABLE " + RULES_TABLE_NAME + "(" + RULES_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + RULES_FROM_NUM + " TEXT," + RULES_FROM_NUM_NAME + " TEXT,"
                + RULES_TO_NUM +" TEXT,"+ RULES_TO_NUM_NAME + " TEXT,"  + RULES_COND +
                  " TEXT," + RULES_BODY_TEXT + " TEXT" + ")";

        db.execSQL(Create_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop table if exists
        db.execSQL("DROP TABLE IF EXISTS " + RULES_TABLE_NAME);
        //create the table again
        onCreate(db);
    }

    public void insertRules(Rules rules){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d("INDB","Inserting rows"+ rules.getId());

        //cv.put(RULES_ID,rules.getId());
        cv.put(RULES_FROM_NUM,rules.getFrom_num());
        cv.put(RULES_FROM_NUM_NAME,rules.getFrom__num_name());
        cv.put(RULES_TO_NUM,rules.getTo_num());
        cv.put(RULES_TO_NUM_NAME,rules.getTo_num_name());
        cv.put(RULES_COND,rules.getCond());
        cv.put(RULES_BODY_TEXT,rules.getBody_text());
        Log.d("INDB","Inserting rows"+ cv);

        db.insert(RULES_TABLE_NAME,null,cv);
        db.close();

    }

    public void updateRules(Rules rules,int id){
        SQLiteDatabase db=this.getWritableDatabase();

        Log.d("INDB","Updating rows"+ id + "" + rules.getBody_text());
       /* Log.d("INDB","Updating rules.getTo_num() "+ rules.getTo_num());
        Log.d("INDB","Updating rows"+ id + "" + rules.getBody_text());
        Log.d("INDB","Updating rows"+ id + "" + rules.getBody_text());
        Log.d("INDB","Updating rows"+ id + "" + rules.getBody_text());
*/
        db.execSQL("UPDATE "+ RULES_TABLE_NAME + " SET " + RULES_FROM_NUM + " = " + "'"+ rules.getFrom_num()+ "',"+
                        RULES_FROM_NUM_NAME+ " = " + "'"+ rules.getFrom__num_name()+ "',"+
                        RULES_TO_NUM + " = " + "'"+ rules.getTo_num() + "',"+
                         RULES_TO_NUM_NAME+ " = " + "'"+ rules.getTo_num_name()+ "',"+
                        RULES_BODY_TEXT + " = " +"'" + rules.getBody_text()+ "'"+"," +
                        RULES_COND + " = " + rules.getCond() + " " +
                        "WHERE " + RULES_ID + " = " + id);

        db.close();

    }

    public void getRules(String sender, String msg){

        String cr_from_num;
        String cr_to_num;
        int cr_cond;
        String cr_body_text;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + RULES_FROM_NUM + "," + RULES_TO_NUM +
                " ," + RULES_COND + " ," + RULES_BODY_TEXT + " FROM " + RULES_TABLE_NAME,null );
        if ((cursor.moveToFirst())){
            do {
                //Toast.makeText(this,"From:" + sender + ",Body: ",Toast.LENGTH_SHORT).show();
                cr_from_num = cursor.getString(0);
                cr_to_num=cursor.getString(1);
                cr_cond= Integer.parseInt(cursor.getString(2));
                cr_body_text=cursor.getString(3);
                String q= "null";

                Log.d("INDB"," Sender "+ sender);
                Log.d("INDB"," cr_from_num "+ cr_from_num);
                Log.d("INDB"," cr_to_num "+ cr_to_num);
                Log.d("INDB"," cr_body_text "+ cr_body_text);
                Log.d("INDB"," cr_cond "+ cr_cond);

                if((cr_from_num == null || "null".equals(cr_from_num))){
                        //( cr_from_num.isEmpty()))

                    Log.d("INDB", "NULL  Check Done");
                }
               /* if( cr_from_num.isEmpty()){

                    Log.d("INDB", "Empty Check Done");
                }*/

                if(msg.contains(cr_body_text)){
                    Log.d("INDB", "String cjecking true");
                }

                if (cr_cond==1) {
                    if (sender.contains(cr_from_num) && msg.contains(cr_body_text)) {
                        Log.d("INDB", "sending from and condition" + sender + " "+ cr_body_text);
                        smsManager.getDefault().sendTextMessage(cr_to_num, null, msg, null, null);

                        //Toast.makeText(context, "MSG Sent", Toast.LENGTH_SHORT).show();
                    }
                } else if(cr_cond == 0){
                    if (((cr_from_num == null || "null".equals(cr_from_num))  && msg.contains(cr_body_text)) || (sender.contains(cr_from_num) && (cr_body_text.isEmpty()))) {
                        Log.d("INDB", "sending from OR condition" + sender);
                        smsManager.getDefault().sendTextMessage(cr_to_num, null, msg, null, null);

                        //Toast.makeText(context, "MSG Sent", Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d("INDB","Sender"+ sender);
                Log.d("INDB","cr_from_num"+ cr_from_num);
                Log.d("INDB","cr_to_num"+ cr_to_num);
                //Log.d("INDB","cr_cond"+ cr_cond);
                //Log.d("INDB","cr_body_text"+ cr_body_text);



            } while (cursor.moveToNext());

        }
    }

    public  ArrayList<Rules> getAllData() {
        ArrayList<Rules> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT ID, " + RULES_FROM_NUM +  "," + RULES_FROM_NUM_NAME +
                "," + RULES_TO_NUM +  "," + RULES_TO_NUM_NAME + " ," + RULES_COND + " ," + RULES_BODY_TEXT
                + " FROM " + RULES_TABLE_NAME, null);

        Log.d("DatabaseHelper", "In getAllDATA");

        int cr_id;
        String cr_from_num;
        String cr_from_num_name;
        String cr_to_num;
        String cr_to_num_name;
        String  cr_cond;
        String cr_body_text;
        if ((cursor.moveToFirst())) {
            do {
                //Toast.makeText(this,"From:" + sender + ",Body: ",Toast.LENGTH_SHORT).show();
                cr_id = Integer.parseInt(cursor.getString(0));
                cr_from_num = cursor.getString(1);
                cr_from_num_name = cursor.getString(2);
                cr_to_num = cursor.getString(3);
                cr_to_num_name = cursor.getString(4);
                cr_cond = cursor.getString(5);
                cr_body_text = cursor.getString(6);

                Rules rules1 = new Rules(cr_id,  cr_from_num,cr_from_num_name, cr_to_num,cr_to_num_name,cr_cond,cr_body_text);
                arrayList.add(rules1);
                Log.d("DatabaseHelper", " cr_from_num: "+cr_from_num + " cr_to_num: "+cr_to_num+" cr_from_num_name: " +
                        cr_from_num_name + " cr_to_num_name: "+ cr_to_num_name + " "+
                        cr_cond+cr_body_text);

            } while (cursor.moveToNext());


        }

        return arrayList;
    }

    public Rules get_Rules_For_Update(int id){

        int cr_id;
        String cr_from_num;
        String cr_from_num_name;
        String cr_to_num;
        String cr_to_num_name;
        String  cr_cond;
        String cr_body_text;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT ID, " + RULES_FROM_NUM +  "," + RULES_FROM_NUM_NAME +
                        "," + RULES_TO_NUM +  "," + RULES_TO_NUM_NAME + " ," + RULES_COND + " ," + RULES_BODY_TEXT
                        + " FROM " + RULES_TABLE_NAME+
                         " WHERE " + RULES_ID + " = " + id, null);

        if (cursor != null)
            cursor.moveToFirst();
        cr_id = Integer.parseInt(cursor.getString(0));
        cr_from_num = cursor.getString(1);
        cr_from_num_name = cursor.getString(2);
        cr_to_num = cursor.getString(3);
        cr_to_num_name = cursor.getString(4);
        cr_cond = cursor.getString(5);
        cr_body_text = cursor.getString(6);
        Log.d("rules_for_update","cursor "+ cr_from_num+cr_from_num_name+cr_to_num+cr_to_num_name);


        Rules rules_upd = new Rules(cr_id,  cr_from_num,cr_from_num_name, cr_to_num,cr_to_num_name,cr_cond,cr_body_text);
        return rules_upd;
    }

    public void deleteRules(int id){

        SQLiteDatabase  db = this.getWritableDatabase();
        String query = "DELETE FROM " + RULES_TABLE_NAME + " WHERE " + RULES_ID + " = " + id;
        db.execSQL(query);
        //Toast.makeText(this,"calling code",Toast.LENGTH_LONG).show();
        Log.d("INDB_delete","Deleted item "+ id);
    }



}
