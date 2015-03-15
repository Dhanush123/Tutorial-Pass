package com.x10host.dhanushpatel.tutorialpass;

/**
 * Created by Dhanush on 1/4/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EditPass extends Activity {
    public static final String TIME_FORMAT = "hh:mm aa";
    Button passCheckAgainButton, passCompleteButton;
    EditText teacherFromCode;
    TextView teacherSelectedName2, txtPass;
    private String data, pass, set;
    private int codeEntered;
    String addition, time, finishedPass;
    static String testing;
    private boolean continuePassMake;

	ArrayList<Integer> Codes = new ArrayList<Integer>();   //FIXME to be removed
    ArrayList<String> Teachers = new ArrayList<String>();  //FIXME to be removed
	HashMap<String, Integer> teacherCodes = new HashMap<String, Integer>(); //FIXME to be implemented

    Intent i;
    ParseObject passObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_list_item_view);

        passObject = new ParseObject("Pass");
        txtPass = (TextView) findViewById(R.id.pass_label);
        passCheckAgainButton = (Button) findViewById(R.id.passCheckAgainButton);
        passCompleteButton = (Button) findViewById(R.id.passCompleteButton);
        teacherFromCode = (EditText) findViewById(R.id.teacherFromCode);
        teacherSelectedName2 = (TextView) findViewById(R.id.teacherSelectedName2);

        SharedPreferences pref = getSharedPreferences("MyData", MainActivity.MODE_PRIVATE);
        data = pref.getString("namePlusID","ERROR");

	    Codes.add(1000); Codes.add(1001); Codes.add(1002); Codes.add(1003); Codes.add(1004); Codes.add(1005); Codes.add(1006);
        Teachers.add("J. Saiki"); Teachers.add("S. Dick"); Teachers.add("B. Quick"); Teachers.add("A. Wheeler"); Teachers.add("M. Pottinger"); Teachers.add("D. Sater"); Teachers.add("G. Osborn");

        addListenerOnButton();
        i = getIntent();

        // getting attached intent data
        pass = i.getStringExtra("pass");
        if(pass!=null) {
            // displaying selected pass name
            txtPass.setText(pass);
        }
    }

    public void addListenerOnButton() {
        passCheckAgainButton.setOnClickListener(new View.OnClickListener() {
            //Run when button is clicked
            @Override
            public void onClick(View v) {
                if (teacherFromCode.getText().toString().length() > 0) {
                    codeEntered = Integer.parseInt(teacherFromCode.getText().toString());
                }

	            if (Codes.contains(codeEntered)) {
                    int index = Codes.indexOf(codeEntered);
                    set = Teachers.get(index);
                    teacherSelectedName2.setText(set);
                    testing = "Selected Teacher From is: " + set;
                    continuePassMake = true;
                } else {
                    testing = "Error: Valid Teacher Code not entered.";
                    continuePassMake = false;
                }

                toast(testing);
            }
        });

        passCompleteButton.setOnClickListener(new View.OnClickListener() {
            //Run when button is clicked
            @Override
            public void onClick(View v) {
                if (!continuePassMake) {
                    toast("Error: Incorrect Teacher From code");
                } else {
                    addition = "\nTeacher From: " + set;
                    time = "\nTime left: "+ getCurrentTime();
                    finishedPass=pass;
                    if(!finishedPass.contains("Teacher From")) {
                        finishedPass = pass+addition+time;
                        txtPass.setText(finishedPass);
                        passObject.put("firstPass", finishedPass + "\n" + data);
                        passObject.saveEventually();
                        i.putExtra("result", finishedPass);
                        setResult(Activity.RESULT_OK, i);
                        toast("You have completed the pass.");
                    } else {
                        toast("You are not allowed to change Teacher From again.");
                    }

	                finish();
                }
            }
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat TimeFormat = new SimpleDateFormat(TIME_FORMAT);
        Calendar calTime = Calendar.getInstance();
        String time = TimeFormat.format(calTime.getTime());

        if(time.substring(0,1).equals("0")) {
            time = time.substring(1);
        }

	    return time;
    }

	private void toast(Object o) {
		Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_SHORT).show();
	}

	private void log(Object o) {
		Log.i("DEBUG", o.toString());
	}
}