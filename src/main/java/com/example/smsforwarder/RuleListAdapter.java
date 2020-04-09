package com.example.smsforwarder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RuleListAdapter extends BaseAdapter{

    Context context;
    ArrayList<Rules> arrayList;


    public RuleListAdapter(Context context,ArrayList<Rules> arrayList) {
        this.context=context;
        this.arrayList = arrayList;
       // Log.d("Adapter Class", "Get view method");
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.display_rules, null);

        }
            Rules rules = arrayList.get(position);
            //TextView dis_rules_id =(TextView) convertView.findViewById(R.id.dis_rules_id);
            TextView dis_received_from = (TextView) convertView.findViewById(R.id.dis_received_from);
            TextView dis_text = (TextView) convertView.findViewById(R.id.dis_text);
            TextView dis_forward_to = (TextView) convertView.findViewById(R.id.dis_forward_to);
            TextView dis_rules = (TextView) convertView.findViewById(R.id.dis_rules);


            String cond = String.valueOf(rules.getCond());

           // dis_rules_id.setText(String.valueOf(rules.getId()));
            dis_received_from.setText(rules.getFrom__num_name());
            dis_text.setText(rules.getBody_text());
            dis_forward_to.setText(rules.getTo_num_name());
            dis_rules.setText(cond);


        Log.d("Adapter Class", "Get view method for adapter load");
        return convertView;
    }
}
