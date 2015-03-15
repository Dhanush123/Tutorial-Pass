package com.x10host.dhanushpatel.tutorialpass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhanush on 12/29/2014.
 */

public class ViewPasses extends Fragment  {
    private static final String ARG_SECTION_NUMBER = "section_number";
    String passString;
    static List<Pass> passes;
    static ArrayList<String> passes2;
    ArrayAdapter<String> adapter;

    ListView lv;
    String pass;
    boolean yesOrNo;
    static SharedPreferences sp;

    public static ViewPasses newInstance(int sectionNumber) {
	    ViewPasses fragment = new ViewPasses();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log("Entering onCreateView: onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_second, container, false);
        passes = Pass.listAll(Pass.class);
        passes2 = new ArrayList<String>();

        if(passString != null) {
            Pass pass = new Pass(passString);
            pass.save();
            passes.add(pass);//here yo
            passString = null;
        }

        for(int i = 0; i < passes.size(); i++) {
            //toast(passes.get(i).info);
            Pass tmp = SugarRecord.findById(Pass.class, (long) i);

            if(tmp != null) {
                String pass = tmp.info;
                passes2.add(pass);
            }
        }

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, passes2);

        lv = (ListView)rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int arg2, long arg3) {
                // onlyOpenOne = false;
                final AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setMessage("Are you sure you want to remove this pass?");
                b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        yesOrNo = true;
                    }
                });

                b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        yesOrNo = false;
                    }
                });

	            b.show();

                if (yesOrNo) {
                    Pass pass = Pass.findById(Pass.class, (long) arg2);
                    pass.delete();
                    //  passes.remove(arg2);//where arg2 is position of item you click
                    lv.invalidate();
                    adapter.notifyDataSetChanged();
                    // onlyOpenOne = true;
                }
                return false;
            }

        });

        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                log("Entering onItemClick: onCreateView");
                // selected item
               //Pass pass2 = Pass.findById(Pass.class,Long.valueOf(position));
                pass = SugarRecord.findById(Pass.class, (long) position).info;

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getActivity(), EditPass.class);
                // sending data to new activity
                i.putExtra("pass", pass);
                i.putExtra("id",0);
                startActivityForResult(i, 1);

            }
        });

       // Log.i("returning from  onCreateView:", "onCreateView");
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  Log.i("Entering onActivityResult:", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            for(int i = 0; i < passes.size(); i++) {
                if(passes.get(i).equals(pass)) {
                    Pass pass = Pass.findById(Pass.class, (long) i);
                    pass.info = data.getStringExtra("result"); // modify the values
                    pass.save(); // updates the previous entry with new values.

                    log("New pass info is: " + pass.info);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public void setPassInfo(String passInfo)
    {
        passString = passInfo;
    }

	private void toast(Object o) {
		Toast.makeText(getActivity().getApplicationContext(), o.toString(), Toast.LENGTH_SHORT).show();
	}

	private void log(Object o) {
		Log.i("DEBUG", o.toString());
	}
}