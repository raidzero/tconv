package com.raidzero.tconv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by raidzero on 9/13/13 2:09 PM
 */
public class Tconv extends Activity implements View.OnClickListener {

    private static final String TAG = "TConf";
    private static final char DEG_SYMBOL = '\u00B0';

    private boolean animations_enabled = true;
    EditText input_temp, input_unit, dest_unit;
    TextView result_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loadPrefs();

        // set up listeners
        input_temp = (EditText) findViewById(R.id.input_temp);
        input_temp.setOnClickListener(this);

        input_unit = (EditText) findViewById(R.id.input_unit);
        input_unit.setOnClickListener(this);

        dest_unit = (EditText) findViewById(R.id.dest_unit);
        dest_unit.setOnClickListener(this);

        result_view = (TextView) findViewById(R.id.result_view);

        // text changed listener
        input_temp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performConversion();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    // be sure to reload preferences on all these events
    public void onStart() {
        super.onStart();
        loadPrefs();
    }
    public void onRestart() {
        super.onRestart();
        loadPrefs();
    }
    public void onPause() {
        super.onPause();
        loadPrefs();
    }
    public void onResume() {
        super.onResume();
        loadPrefs();
    }

    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.input_unit:
                hideSoftKeyboard(this);
                get_temp_unit(input_unit);
                break;
            case R.id.dest_unit:
                hideSoftKeyboard(this);
                get_temp_unit(dest_unit);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Prefs.class));
                return true;
        }
        return false;
    }

    public void loadPrefs() {
        // get preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        animations_enabled = prefs.getBoolean("animations", true);
    }

    // get the unit from a list presented to user and set the view's text to it
    public void get_temp_unit(final EditText v)
    {
        ArrayList<String> units = new ArrayList<String>();
        String[] availableUnits = getResources().getStringArray(R.array.temp_units); // all available units

        String currentDestUnit = dest_unit.getText().toString();
        String currentSourceUnit = input_unit.getText().toString();

        for (String item : availableUnits) {
            switch (v.getId()) {
                case R.id.input_unit:
                    // only add this item to the list if it isn't the current destination unit
                    if (!item.equals(currentDestUnit)) {
                        units.add(item);
                    }
                    break;
                case R.id.dest_unit:
                    // only add this item to the list if it isn't the current source unit
                    if (!item.equals(currentSourceUnit)) {
                        units.add(item);
                    }
                    break;
            }
        }

        final String[] unitsArray = units.toArray(new String[units.size()]);

        new AlertDialog.Builder(this)
                .setTitle(R.string.unit_select)
                .setItems(unitsArray,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                v.setText(unitsArray[i]);
                                performConversion();
                            }
                        })
                .show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void performConversion()
    {
        String input_temp_value = input_temp.getText().toString();
        if (input_temp_value == null || input_temp_value.isEmpty()) {
            return; // dont do anything
        }

        double input_temp_double_value = Double.valueOf(input_temp.getText().toString());
        String input_unit_value = input_unit.getText().toString();
        String dest_unit_value = dest_unit.getText().toString();

        if (input_unit_value != null && !dest_unit_value.isEmpty() && !input_unit_value.isEmpty())
        {
            char s_unit = input_unit_value.charAt(0);
            char d_unit = dest_unit_value.charAt(0);
            Temp t = new Temp(input_temp_double_value, s_unit);
            double res = t.convert(d_unit); // do conversion using convert method on Temp object

            result_view.setText("" + input_temp_double_value + " " + DEG_SYMBOL + s_unit + " -> " + d_unit + ":\n" + res + " " + DEG_SYMBOL + d_unit);

            if (animations_enabled) {
                Animation flyInEffect = AnimationUtils.loadAnimation(this, R.anim.fly_in);
                result_view.startAnimation(flyInEffect);
            }
            Log.d(TAG, "" + input_temp_double_value + " " + DEG_SYMBOL + s_unit + " -> " + d_unit + "=" + res + " " + DEG_SYMBOL + d_unit);
        }
    }
}
