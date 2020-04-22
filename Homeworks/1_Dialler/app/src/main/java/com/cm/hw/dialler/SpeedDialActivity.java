package com.cm.hw.dialler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * HW1_Dialler - SpeedDialActivity <br>
 *
 * @author Pedro Teixeira pedro.teix@ua.pt
 * @version 1.0 - April 22, 2020
 */
public class SpeedDialActivity extends AppCompatActivity {

    private static final String SPEED_DIAL_NAME_KEY = "com.cm.hw.dialler.speedDial.prefs.name";
    private static final String SPEED_DIAL_NUMBER_KEY = "com.cm.hw.dialler.speedDial.prefs.number";
    private String numberTag;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_dial);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        Intent whoCalledMe = getIntent();
        numberTag = whoCalledMe.getStringExtra(MainActivity.SPEED_DIAL_NUMBER_TAG);
        retrieveSpeedDial();
    }

    public void retrieveSpeedDial() {
        if (numberTag != null) {
            Log.i("Speed Dial","Retriving from " + numberTag);

            EditText speedDialNameTextView = findViewById(R.id.speedDialName);
            speedDialNameTextView.setText(sharedPref.getString(SPEED_DIAL_NAME_KEY + "_" + numberTag, "No speed dial name"));

            EditText speedDialNumberTextView = findViewById(R.id.speedDialNumber);
            speedDialNumberTextView.setText(sharedPref.getString(SPEED_DIAL_NUMBER_KEY + "_" + numberTag, "No speed dial number"));
        }
    }

    public void saveSpeedDial(View view) {
        if (numberTag != null) {
            Log.i("Speed Dial","Saving to " + numberTag);

            EditText speedDialNameTextView = findViewById(R.id.speedDialName);
            String speedDialName = speedDialNameTextView.getText().toString();

            EditText speedDialNumberTextView = findViewById(R.id.speedDialNumber);
            String speedDialNumber = speedDialNumberTextView.getText().toString();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(SPEED_DIAL_NAME_KEY + "_" + numberTag, speedDialName);
            editor.putString(SPEED_DIAL_NUMBER_KEY + "_" + numberTag, speedDialNumber);
            editor.apply();

            Toast.makeText(this, "Saved succesfully", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void callSpeedDial(View view) {
        Log.i("Speed Dial","Saving and calling");
        saveSpeedDial(view);

        /* Call */
        String error = "No speed dial number";
        String speedDialNumber = sharedPref.getString(SPEED_DIAL_NUMBER_KEY + "_" + numberTag, error);
        Uri phoneNumber = Uri.parse("tel:" + speedDialNumber);

        /* Request OS to dial */
        Intent intent = new Intent(Intent.ACTION_DIAL, phoneNumber);
        if (intent.resolveActivity(getPackageManager()) != null && !error.equals(speedDialNumber)) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error while calling " + speedDialNumber, Toast.LENGTH_LONG).show();
        }
    }
}
