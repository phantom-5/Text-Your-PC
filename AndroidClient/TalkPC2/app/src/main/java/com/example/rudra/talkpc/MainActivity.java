package com.example.rudra.talkpc;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageButton send;
    TextView tv;
    EditText et;
    static int control=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send=findViewById(R.id.button);
        tv=findViewById(R.id.textView3);
        et=findViewById(R.id.editText2);
        tv.setText("");

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  s=et.getText().toString();
                if(s.equals("Talk to PC")){
                    et.setText("");
                }
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=et.getText().toString();
                if(s.equals("")||s.equals("Talk to PC"))
                {
                    tv.setText("I Didn't Undertsand You");
                }else{
                   final String cmd=et.getText().toString();
                   Thread t=new Thread(new Runnable() {
                       @Override
                       public void run() {
                           try {
                               URL url = new URL("https://api.myjson.com/bins/11tt7a");
                               HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                               connection.setRequestMethod("PUT");
                               connection.setRequestProperty("Content-Type","application/json");
                               connection.setDoOutput(true);
                               connection.setDoInput(true);

                               JSONObject jsOb=new JSONObject();
                               jsOb.put("sup_a",cmd);
                               DataOutputStream dos=new DataOutputStream(connection.getOutputStream());
                               dos.writeBytes(jsOb.toString());
                               Log.d("ConTest",Integer.toString(connection.getResponseCode()));
                               dos.flush();
                               dos.close();
                               connection.disconnect();
                               while(true) {
                                   connection = (HttpURLConnection) url.openConnection();
                                   connection.setRequestMethod("GET");
                                   BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                   String inp;
                                   StringBuffer resp = new StringBuffer();
                                   while ((inp = in.readLine()) != null) {
                                       resp.append(inp);
                                   }
                                   if(resp.indexOf("sup_p")>=0){
                                       Log.d("ConTest",resp.toString());
                                       final String resp_inner=resp.toString();
                                       runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                               tv.setText(resp_inner.substring(10,resp_inner.length()-2));
                                               tv.setBackgroundColor(Color.parseColor("#95ffffff"));
                                           }
                                       });
                                   break;
                                   }
                                   in.close();
                                   connection.disconnect();
                               }

                           }catch(Exception e){
                               Log.d("ConTest","Caught in send.onClick");
                           }
                       }
                   });
                   t.start();

                }

            }
        });


    }
}
