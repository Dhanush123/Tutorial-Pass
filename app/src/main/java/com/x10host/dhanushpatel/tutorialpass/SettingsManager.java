package com.x10host.dhanushpatel.tutorialpass;


/**
 * Created by Dhanush on 1/6/2015.
 */

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

public class SettingsManager extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Button checkNameAndID, setNameAndID;
    private EditText nameText, IDText;
    private TextView nameAndIDShow;
    private String name, ID, namePlusID;
    static SharedPreferences pref;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    public static SettingsManager newInstance(int sectionNumber) {
	    SettingsManager fragment = new SettingsManager();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        checkNameAndID = (Button) rootView.findViewById(R.id. checkNameAndID);
        setNameAndID = (Button) rootView.findViewById(R.id.setNameAndID);
        nameText = (EditText) rootView.findViewById(R.id.nameText);
        SharedPreferences pref = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String info = pref.getString("namePlusID","Name and ID not set");
        nameAndIDShow = (TextView) rootView.findViewById(R.id.nameAndIDShow);
        nameAndIDShow.setText(info);
        IDText = (EditText) rootView.findViewById(R.id.IDText);

        addListenerOnButton();
        return rootView;
    }

    public void addListenerOnButton() {
        checkNameAndID.setOnClickListener(new View.OnClickListener() {
            //Run when button is clicked
            @Override
            public void onClick(View v) {
                name = nameText.getText().toString();
                ID = IDText.getText().toString();
                toast("Name: "+name+"\nID: "+ID);
            }
        });

        setNameAndID.setOnClickListener(new View.OnClickListener() {
            //Run when button is clicked
            @Override
            public void onClick(View v) {
                if (nameAndIDShow.getText().toString().equals("Name and ID not set") || nameAndIDShow.getText().toString().equals("")) {
                    pref = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
                    // We need an editor object to make changes
                    SharedPreferences.Editor edit = pref.edit(); //from online

                    if(ID.length() == 6) {
                        namePlusID = "Name: " + name + "\nID: " + ID;

                        // Set/Store data
                        edit.putString("namePlusID", namePlusID);

                        // Commit the changes
                        edit.commit();
                        String info = pref.getString("namePlusID","");
                        nameAndIDShow.setText(info);

                        toast("Name and ID have been permanently set.");
                    } else {
                        toast("ID must be 6 digits long.");
                    }
                } else {
                    toast("Resetting the name or ID is not allowed.");
                }
            }
        });
    }

    public String getPersonalInfo()
    {
        return namePlusID;
    }

	private void toast(Object o) {
		Toast.makeText(getActivity().getApplicationContext(), o.toString(), Toast.LENGTH_SHORT).show();
	}

	private void log(Object o) {
		Log.i("DEBUG", o.toString());
	}
}
