package com.x10host.dhanushpatel.tutorialpass;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Dhanush on 12/29/2014.
 */

public class MakePass extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Button checkMakePass;
    private Button passMakeButton;
    private EditText teacherToCode;
    private TextView teacherSelectedName;
    private int codeEntered;
    static String testing;
    static String date;
    static String time;
    private EditText dateEntered;
    private String set;
    private boolean continuePassMake;
    private String passInfo;
    TextView nameAndIDShow;
    public static final String PREFS_NAME = "MyPrefsFile";
    ArrayList<Integer> Codes = new ArrayList<Integer>();
    ArrayList<String> Teachers = new ArrayList<String>();
    private String data;
    private long id;
    onPassMakeButtonSelectedListener mCallback;

    public interface onPassMakeButtonSelectedListener {
        public void sendPassInfo(String info);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (onPassMakeButtonSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    public static MakePass newInstance(int sectionNumber) {
	    MakePass fragment = new MakePass();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        checkMakePass = (Button) rootView.findViewById(R.id.checkMakePass);
        passMakeButton = (Button) rootView.findViewById(R.id.passMakeButton);
        teacherToCode = (EditText) rootView.findViewById(R.id.teacherToCode);
        teacherSelectedName = (TextView) rootView.findViewById(R.id.teacherSelectedName);
        dateEntered = (EditText) rootView.findViewById(R.id.dateEntered);
        SharedPreferences pref = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);

        // SharedPreferences.Editor edit = pref.edit();
        data = pref.getString("namePlusID",null);
        Codes.add(1000);
        Codes.add(1001);
        Codes.add(1002);
        Codes.add(1003);
        Codes.add(1004);
        Codes.add(1005);
        Codes.add(1006);
        Teachers.add("J. Saiki");
        Teachers.add("S. Dick");
        Teachers.add("B. Quick");
        Teachers.add("A. Wheeler");
        Teachers.add("M. Pottinger");
        Teachers.add("D. Sater");
        Teachers.add("G. Osborn");
        addListenerOnButton();
        return rootView;

    }

    public void addListenerOnButton() {
        checkMakePass.setOnClickListener(new View.OnClickListener() {
            //Run when button is clicked
            @Override
            public void onClick(View v) {
                //  if(data!=null)
                //{
                if (teacherToCode.getText().toString().length() > 0)
                    codeEntered = Integer.parseInt(teacherToCode.getText().toString());

                if (dateEntered.getText().toString().length() > 0) {
                    if (dateEntered.getText().toString().matches("[0-9]{2}/[0-9]{2}/[0-9]{2}") || dateEntered.getText().toString().matches("[0-9]{1}/[0-9]{2}/[0-9]{2}") || dateEntered.getText().toString().matches("[0-9]{1}/[0-9]{1}/[0-9]{2}") || dateEntered.getText().toString().matches("[0-9]{2}/[0-9]{1}/[0-9]{2}")) {
                        date = "\nDate: " + dateEntered.getText().toString();
                    } else {
                        toast("Error: Date not properly formatted.");
                    }
                }

                for (int i = 0; i < Codes.size(); i++) {
                    if (codeEntered == Codes.get(i)) {
                        int index = Codes.indexOf(codeEntered);
                        set = Teachers.get(index);
                        teacherSelectedName.setText(set);
                        testing = "Teacher To: " + set;
                        if (date != null) {
                            testing += date;
                            continuePassMake = true;
                        }
                        break;
                    } else {
                        testing = "Error: Valid Teacher Code not entered.";
                        continuePassMake = false;
                    }
                }

                toast(testing);
            }
        });

        passMakeButton.setOnClickListener(new View.OnClickListener() {
            //Run when button is clicked
            @Override
            public void onClick(View v) {
                if (data != null) {
                    if (!continuePassMake) {
	                    toast("Error: Incorrect Date format or Teacher To code");
                    } else {
                        toast("New pass made. Check \"See All Passes\"");
                        mCallback.sendPassInfo(testing);
                        // Log.i("End of 1st Frag:", testing);
                    }
                } else {
                    toast("First set your name and ID in \"Settings\".");
                }
            }
        });
    }

	private void toast(Object o) {
		Toast.makeText(getActivity().getApplicationContext(), o.toString(), Toast.LENGTH_SHORT).show();
	}

	private void log(Object o) {
		Log.i("DEBUG", o.toString());
	}
}