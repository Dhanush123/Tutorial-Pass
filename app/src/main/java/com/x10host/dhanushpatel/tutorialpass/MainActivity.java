package com.x10host.dhanushpatel.tutorialpass;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity implements ActionBar.OnNavigationListener, MakePass.onPassMakeButtonSelectedListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Button passMakeButton;
    private DatePicker dateChoose;
    private EditText teacherToCode;
    private TextView teacherSelectedName;
    ViewPasses secondFragment = ViewPasses.newInstance(2);
	SettingsManager settingsFragment = SettingsManager.newInstance(2);

    //ArrayList<Integer> teachers = new ArrayList<Integer>();
    // private int rows = 1, columns = 7;
    //private int code = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);//earlier false
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[] {getString(R.string.title_section1), getString(R.string.title_section2), getString(R.string.title_section3)}),
                        this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

		// Enable Local Datastore
	    while(!enableLocalDatastorage()); //this makes sure that the Local Datastore will be forcefully enabled

	    Parse.initialize(this, "GReNWfVXkgP4FGj0vaJrkPz1EnocmHg6L8qf2YZZ", "gMqEYaOVeM4SAR7PVUNqrtKpPzZnWYYRckW03N23");
    }

	public boolean enableLocalDatastorage() {
		boolean result;

		try {
			Parse.enableLocalDatastore(this);
			result = true;
		} catch (Exception ex) {
			String why = ex.getMessage();
			if (why.equals("enableOfflineStore() called multiple times.")) { //then its already enabled
				result = true;
			} else { //its some other error, ignore it for now //FIXME may need to do more than ignore based on the error
				result = false;
			}
		}

		return result;
	}

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        //FIXME
        /*if (id == R.id.action_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment, "SettingFragment_TAG").commit();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        //toast("id = " + id);

        if (id == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, MakePass.newInstance(position + 1),"MakePass_TAG").commit();
        } else if (id == 1) {
            if(ViewPasses.passes != null && ViewPasses.passes.size() >= 0) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment, "ViewPasses_TAG").commit();
            } else {
                toast("Error: No passes have been yet.");
            }
        } else if (id == 2) {
	        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment, "SettingsManager_TAG").commit();
        }

        return true;
    }

    public void sendPassInfo(String info) {
        // sends the information of a pass

	    ViewPasses aFrag = (ViewPasses) getSupportFragmentManager().findFragmentByTag("PlaceholderSecondFragment_TAG");
        log("right before null test is this point in sendPassInfo implementation in MainActivity");

        if (aFrag != null) {
            // If article frag is available, we're in two-pane layout...
	        log("It pass the null test!");
            log(info);
            // Call a method in the ArticleFragment to update its content
            aFrag.setPassInfo(info);
        } else {
	        log("It didn't pass the null test!");
            // Otherwise, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.container, secondFragment);
	        transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
            secondFragment.setPassInfo(info);
	        log("We got to end of else statement!");
            log(info);
        }
    }

	private void toast(Object o) {
		Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_SHORT).show();
	}

	private void log(Object o) {
		Log.i("DEBUG", o.toString());
	}
}