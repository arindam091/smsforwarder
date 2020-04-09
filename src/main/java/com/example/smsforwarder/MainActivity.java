package com.example.smsforwarder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 24;
    String from_num = null;
    String from_num_name;
    String to_num=null;
    String to_num_name;
    int cond;
    TextView body_text;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int switch_Btn=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.list_view);

        //lv = (ListView) findViewById(R.id.adapter_list_view);
        //arrayList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        int dayofyear = cal.get(Calendar.DAY_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);

        Log.d("Calender","dayofyear " + dayofyear);
        Log.d("Calender","year " + year);
        Log.d("Calender","dayofweek " + dayofweek);
        Log.d("Calender","dayofmonth " + cal.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault()));

        cond=2;
        Log.d("OnCreate","Entered");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.RECEIVE_SMS},1000);
            Log.d("OnCreate","Receive SMS Permission");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.SEND_SMS},1000);
            Log.d("OnCreate","Send SMS Permission");
        }
        TextView from_num_srg = (TextView) findViewById(R.id.from_num);
       /* from_num_srg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               // String id = String.valueOf(view.getId());
                //Log.d("Contact", "Text View Clicked ID: " + id);
                Toast.makeText(MainActivity.this,"Select Contacts",Toast.LENGTH_LONG).show();
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                //contactPickerIntent.putExtra("id", id);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Intent serviceIntent = new Intent(MainActivity.this, ReceiveSms.class);

        if (requestCode ==1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted!", Toast.LENGTH_SHORT).show();
                Log.d("Permission","Permission Granted");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    BroadcastReceiver smsReceiver = new ReceiveSms();
                    IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                    intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
                    this.registerReceiver(smsReceiver, intentFilter);
                }
                startService(serviceIntent);
            } else {
                Toast.makeText(MainActivity.this,"Permission denied",Toast.LENGTH_LONG).show();
            }
        }
    }


    public void save(final View view) {

        Log.d("In save ","On Click");

        TextView from_num_srg = (TextView) findViewById(R.id.from_num);
        //String from_num = from_num_srg.getText().toString();

        TextView to_num_sng = (TextView) findViewById(R.id.to_num);
        //String to_num = to_num_sng.getText().toString();

        body_text = (TextView)findViewById(R.id.msg_body);
        String sss=body_text.getText().toString();

        TextView and_Radio = (TextView) findViewById(R.id.btn_and);
        TextView or_Radio = (TextView) findViewById(R.id.btn_or);

        try {
            radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton rd_btn = (RadioButton) findViewById(selectedId);
            String btn_check = (String) rd_btn.getText();
            String and = "Match All";
            String or = "Match Any";
            if(or.equals(btn_check)){
                cond = 0;
                Log.d("In OR ", "OR button Clicked");
            } else {
                cond = 1;
                Log.d("In AND ","AND button Clicked");
            }

        } catch (NullPointerException  e) {
            e.printStackTrace();
        }

        DatabaseHelper db = new DatabaseHelper(MainActivity.this);

        if (((from_num == null ) || (sss.isEmpty())) && (to_num.isEmpty())) {
            Log.d("In OR ", "Please enter the fields");
            Toast.makeText(MainActivity.this, "Please enter destination number", Toast.LENGTH_LONG).show();

            from_num_srg.setText(from_num);
            to_num_sng.setText(to_num);
            body_text.setText(sss);

        } else {
            if ((from_num == null ) && (sss.isEmpty())) {
                Toast.makeText(MainActivity.this, "Please enter atleast one field ", Toast.LENGTH_LONG).show();

                to_num_sng.setText(to_num);

            } else{
                if (cond==2) {

                    Toast.makeText(MainActivity.this, "please select a condition" , Toast.LENGTH_LONG).show();

                    from_num_srg.setText(from_num);
                    to_num_sng.setText(to_num);
                    body_text.setText(sss);

                } else {

                    Log.d("In Insert ", "Row Inserted from IF " +from_num + "+"+ from_num_name + " " + to_num_name);

                    db.insertRules(new Rules(from_num,from_num_name, to_num,to_num_name, cond, sss));
                    Log.d("In OR ", "Row Inserted from IF " + cond);

                    from_num_srg.setText("");
                    to_num_sng.setText("");
                    body_text.setText("");
                }
            }


        }

    }

    public void display(View view) {

        Intent myIntent = new Intent(this, DisplayActivity.class);
        ///myIntent.putExtra("key", value); //Optional parameters

        MainActivity.this.startActivity(myIntent);
    }

    public void contacts(View view) {

        //int id = view.getId();
        String id_name = getResources().getResourceName(view.getId());
        Log.d("Contact", "Text View Clicked Name: " + id_name);
            if(id_name.contains("from_num")){
                switch_Btn = 1;
            } else{
                switch_Btn = 0;
            }
        Toast.makeText(MainActivity.this,"Select Contacts",Toast.LENGTH_LONG).show();
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        //contactPickerIntent.putExtra("id", id_name);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // println("onActivityResult() for code <" + requestCode + ">");

      //  String id = data.getStringExtra("id");
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Contact", "switch_Btn: " + switch_Btn);
        Log.d("Contact", "Text View result code ID: " + resultCode);


        if (requestCode == RESULT_PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactsData = data.getData();
                CursorLoader loader = new CursorLoader(this, contactsData, null, null, null, null);
                Cursor c = loader.loadInBackground();
                TextView from_num_srg = (TextView) findViewById(R.id.from_num);
                TextView to_num_sng = (TextView) findViewById(R.id.to_num);
               if  (c.moveToFirst()) {
                    try {
                        if (switch_Btn == 1) {
                            from_num = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            from_num_name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            from_num_srg.setText(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        } else {
                            to_num_sng.setText(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                            to_num = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            to_num_name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        }
                    } catch (NullPointerException  e) {
                    e.printStackTrace();
                }
                    //String from_num; = from_num_srg.getText().toString();


                   /* Log.d("Contact", "Contacts ID: " + c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)));
                    Log.i("Contact", "Contacts Name: " + c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    Log.i("Contact", "Contacts Phone Number: " + c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                   // Log.i(TAG, "Contacts Photo Uri: " + c.getString(c.getColumnIndex(Photo.PHOTO_URI)));*/
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
