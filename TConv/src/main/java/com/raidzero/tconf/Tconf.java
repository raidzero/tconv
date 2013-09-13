package com.raidzero.tconf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by raidzero on 9/13/13 2:09 PM
 */
public class Tconf extends Activity implements View.OnClickListener {

    private static final String TAG = "TConf";
    private static final char DEG_SYMBOL = '\u00B0';

    EditText input_temp, input_unit, dest_unit;
    Button convert_button;
    TextView result_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // set up listeners
        input_temp = (EditText) findViewById(R.id.input_temp);
        input_temp.setOnClickListener(this);

        input_unit = (EditText) findViewById(R.id.input_unit);
        input_unit.setOnClickListener(this);

        dest_unit = (EditText) findViewById(R.id.dest_unit);
        dest_unit.setOnClickListener(this);

        convert_button = (Button) findViewById(R.id.go_button);
        convert_button.setOnClickListener(this);

        result_view = (TextView) findViewById(R.id.result_view);
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
            case R.id.go_button:
                hideSoftKeyboard(this);

                float input_temp_value = Float.valueOf(input_temp.getText().toString());
                String input_unit_value = input_unit.getText().toString();
                String dest_unit_value = dest_unit.getText().toString();

                char s_unit = input_unit_value.charAt(0);
                char d_unit = dest_unit_value.charAt(0);
                Temp t = new Temp(input_temp_value, s_unit);
                double res = t.convert(d_unit); // do conversion using convert method on Temp object


                result_view.setText("" + input_temp_value + " " + DEG_SYMBOL + s_unit + " -> " + d_unit + ":\n" + res + " " + DEG_SYMBOL + d_unit);
                Log.d(TAG, "" + input_temp_value + " " + DEG_SYMBOL + s_unit + " -> " + d_unit + "=" + res + " " + DEG_SYMBOL + d_unit);
                break;
        }
    }

    // get the unit from a list presented to user and set the view's text to it
    public void get_temp_unit(final EditText v)
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.unit_select)
                .setItems(R.array.temp_units,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                String[] unitsArray = getResources().getStringArray(R.array.temp_units);
                                v.setText(unitsArray[i]);
                            }
                        })
                .show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
