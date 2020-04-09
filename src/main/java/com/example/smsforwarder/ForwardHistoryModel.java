package com.example.smsforwarder;

import android.util.Log;

public class ForwardHistoryModel {
    int id;
    String from_num;
    String from_num_name;
    String to_num;
    String to_num_name;
    int cond;
    String body_text;

    public ForwardHistoryModel(int id, String from_num, String from_num_name, String to_num, String to_num_name, int cond, String body_text){

        this.id=id;
        Log.d("INDB","In rules");
        this.from_num=from_num;
        this.from_num_name=from_num_name;
        this.to_num = to_num;
        this.to_num_name = to_num_name;
        this.cond=cond;
        this.body_text = body_text;
    }

    public ForwardHistoryModel(String from_num, String from_num_name, String to_num, String to_num_name, int cond, String body_text){
        Log.d("INDB","In rules");
        this.from_num=from_num;
        this.from_num_name = from_num_name;
        this.to_num = to_num;
        this.to_num_name = to_num_name;
        this.cond=cond;
        this.body_text = body_text;
    }
    public ForwardHistoryModel(int cr_id, String cr_from_num, String from_num_name, String cr_to_num, String to_num_name, String cr_cond, String cr_body_text){
        Log.d("INDB","In rules");
        //this.id= Integer.parseInt(String.valueOf(cr_id));
        this.id= cr_id;
        this.from_num=cr_from_num;
        this.from_num_name = from_num_name;
        this.to_num = cr_to_num;
        this.to_num_name = to_num_name;
        this.cond=Integer.parseInt(cr_cond);
        this.body_text = cr_body_text;
    }



    public int getId()
    {
        return  id;
    }


    public String getFrom_num(){
        return from_num;

    }
    public String getFrom__num_name(){
        return from_num_name;
    }

    public String getTo_num(){
        return to_num;
    }

    public String getTo_num_name(){
        return to_num_name;
    }

    public int  getCond(){

        return cond;
    }

    public String   getBody_text(){
        return body_text;
    }

}
