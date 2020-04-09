package com.example.smsforwarder;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    ArrayList<Rules> arrayList;
    RuleListAdapter  ruleListAdapter;
    ListView lv;
    private static final int RESULT_PICK_CONTACT = 24;
    int switch_Btn=-1;
    String from_num = null;
    String from_num_name;
    String to_num=null;
    String to_num_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.list_view);
        loadDataInListView();
    }

    private void loadDataInListView() {
        Log.d("Display Activity ","On Click");
        DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);
        //setContentView(R.layout.list_view);
        lv = (ListView) findViewById(R.id.adapter_list_view);
        arrayList = new ArrayList<>();
        arrayList = db.getAllData();
        //Log.d("Display Activity ","On Click" + arrayList);
        ruleListAdapter = new RuleListAdapter(this,arrayList);
        lv.setAdapter(ruleListAdapter);
        ruleListAdapter.notifyDataSetChanged();

        registerForContextMenu(lv);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        //menu.setHeaderTitle("Select The Action");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        //Object id = ruleListAdapter.getItem(listPosition);//list item title
        final DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);
        Rules rules = arrayList.get(listPosition);

        View view = info.targetView;
        final int id = rules.id;
        //String from_num_upd = rules.from_num;
        //String to_num_upd = rules.to_num;
        int cond_upd = rules.cond;
        String body_text_upd = rules.body_text;

        //RadioButton radioButton;


        Log.d("In EDIT Button ", "AND button Clicked " + rules.to_num);

        if (item.getItemId() == R.id.edit) {
            //Toast.makeText(getApplicationContext(),"calling code",Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);

            String chk_null = "null";
            TextView from_num_srg = (TextView) findViewById(R.id.from_num);
            TextView to_num_srg = (TextView) findViewById(R.id.to_num);
            TextView body_text = (TextView) findViewById(R.id.msg_body);
           // TextView id_srg = (TextView) findViewById(R.id.dis_rules_id);

            Log.d("In OR ", "ID Clicked " + id);


            RadioButton and_Radio = (RadioButton) findViewById(R.id.btn_and);
            RadioButton or_Radio = (RadioButton) findViewById(R.id.btn_or);
            Button btn_save = (Button) findViewById(R.id.save);

            DatabaseHelper db_upd = new DatabaseHelper(DisplayActivity.this);

            Rules rule_for_update = db_upd.get_Rules_For_Update(id);
            Log.d("rules_for_update ", "ID Clicked " + rule_for_update.to_num_name);

            if (rule_for_update.to_num.contains(chk_null)) {
                from_num = null;
            }

            if (rule_for_update.body_text.contains(chk_null)) {
                Log.d("In OR ", "body_text is null");
            }
            from_num_srg.setText(rule_for_update.from_num_name);
            to_num_srg.setText(rule_for_update.to_num_name);
            body_text.setText(body_text_upd);
            to_num = rule_for_update.to_num;
            to_num_name = rule_for_update.to_num_name;

            if (cond_upd == 1) {
                and_Radio.setChecked(true);
            } else {
                // and_Radio.setText(true);
                or_Radio.setChecked(true);
            }


            btn_save.setText("Update");
            btn_save.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    int cond = 2;
                    RadioGroup radioGroup;
                    TextView from_num_srg = (TextView) findViewById(R.id.from_num);
                    TextView to_num_srg = (TextView) findViewById(R.id.to_num);
                    TextView body_text_updated = (TextView) findViewById(R.id.msg_body);
                    //TextView id_srg = (TextView) findViewById(R.id.dis_rules_id);
                    //RadioButton and_Radio = (RadioButton) findViewById(R.id.btn_and);
                    //RadioButton or_Radio = (RadioButton) findViewById(R.id.btn_or);

                    radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton rd_btn = (RadioButton) findViewById(selectedId);

                    //String from_num_name = from_num_srg.getText().toString();
                    //String to_num_name = to_num_srg.getText().toString();
                    String sss = body_text_updated.getText().toString();
                    String btn_check = (String) rd_btn.getText();
                    String and = "Match All";
                    String or = "Match Any";

                    Log.d("In OR ", "Before the IF condition =" + btn_check);

                    try {
                        if (or.equals(btn_check)) {
                            cond = 0;
                            Log.d("In OR ", "OR button Clicked");
                        } else {
                            cond = 1;
                            Log.d("In AND ", "AND button Clicked");
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    //DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);

                    //if (((from_num != null)|| (sss.isEmpty())) && (to_num==null)) {
                    if (to_num == null) {
                        Log.d("In OR ", "Please enter the fields " + from_num + " " + to_num);
                        Toast.makeText(DisplayActivity.this, "Please enter destination number!", Toast.LENGTH_LONG).show();
                    } else {
                        if ((from_num != null) && (sss.isEmpty())) {
                            Toast.makeText(DisplayActivity.this, "Please enter sender number or massage keyword!", Toast.LENGTH_LONG).show();
                        } else {
                            if (cond != 2) {

                                db.updateRules(new Rules(from_num, from_num_name, to_num, to_num_name, cond, sss), id);
                                Log.d("In OR ", "Row Inserted to_num " + to_num);
                                Log.d("In OR ", "Row Inserted to_num_name " + to_num_name);
                                Log.d("In OR ", "Row Inserted from_num " + from_num);
                                Log.d("In OR ", "Row Inserted from_num_name " + from_num_name);
                                Log.d("In OR ", "Row Inserted from IF " + cond +
                                        " " + sss);

                            } else {
                                Toast.makeText(DisplayActivity.this, "please select a condition", Toast.LENGTH_LONG).show();
                            }


                        }


                        Toast.makeText(DisplayActivity.this, "Update Button Clicked", Toast.LENGTH_LONG).show();
                    }


                }
            });
        }
            else if (item.getItemId() == R.id.delete) {

                Log.d("Display Activity ", "On Click " + id);
                db.deleteRules(id);
                loadDataInListView();

            } else {
                return false;
            }
            return true;
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
        Toast.makeText(DisplayActivity.this,"Select Contacts",Toast.LENGTH_LONG).show();
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
                    Log.d("Contact", "Contacts ID: " + c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)));
                    Log.i("Contact", "Contacts Name: " + c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    Log.i("Contact", "Contacts Phone Number: " + c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                  //Log.i(TAG, "Contacts Photo Uri: " + c.getString(c.getColumnIndex(Photo.PHOTO_URI)));
                }
            }
        }
    }

}
