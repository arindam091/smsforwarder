package com.example.smsforwarder;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class ReceiveSms extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("BroadCast Receiver","Control shifted");

        //Toast.makeText(context,"sms Received",Toast.LENGTH_SHORT).show();
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String msg_from;
            //String toPhoneNumber = "9832672233";
            if(bundle != null)  {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new  SmsMessage[pdus.length];
                    SmsManager smsManager = SmsManager.getDefault();
                    DatabaseHelper db = new DatabaseHelper(context);

                    for(int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        Toast.makeText(context,"From:" + msg_from + ",Body: " + msgBody,Toast.LENGTH_SHORT).show();
                        db.getRules(msg_from,msgBody);

                        Toast.makeText(context,"From:" + msg_from + ",Body: " + msgBody,Toast.LENGTH_SHORT).show();

                         /* if (msgBody.contains("WBSEDCL")==true) {
                              smsManager.getDefault().sendTextMessage("9832672233;9832472151", null, msgBody, null, null);

                              Toast.makeText(context, "MSG Sent", Toast.LENGTH_SHORT).show();

                          }*/

                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
