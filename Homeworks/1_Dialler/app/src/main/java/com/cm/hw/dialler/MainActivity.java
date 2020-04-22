package com.cm.hw.dialler;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * HW1_Dialler - MainActivity <br>
 *
 * @author Pedro Teixeira pedro.teix@ua.pt
 * @version 1.0 - April 22, 2020
 */
public class MainActivity extends AppCompatActivity {

    public static final String SPEED_DIAL_NUMBER_TAG = "com.cm.hw.dialler.extras.numberTag" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Set speed dial buttons (button 1, 2 and 3) */
        View.OnLongClickListener speedDialListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switch (v.getId()) {
                    case R.id.button1:
                        Log.i("speedDial", "Pressed 1");
                        speedDial("1");
                        break;
                    case R.id.button2:
                        Log.i("speedDial", "Pressed 2");
                        speedDial("2");
                        break;
                    case R.id.button3:
                        Log.i("speedDial", "Pressed 3");
                        speedDial("3");
                        break;
                }
                return true;
            }
        };
        findViewById(R.id.button1).setOnLongClickListener(speedDialListener);
        findViewById(R.id.button2).setOnLongClickListener(speedDialListener);
        findViewById(R.id.button3).setOnLongClickListener(speedDialListener);
    }

    public void addDigit (View view) {
        switch (view.getId()) {
            case R.id.button1:
                Log.i("addDigit", "Pressed 1");
                updateNumber("1");
                break;
            case R.id.button2:
                Log.i("addDigit", "Pressed 2");
                updateNumber("2");
                break;
            case R.id.button3:
                Log.i("addDigit", "Pressed 3");
                updateNumber("3");
                break;
            case R.id.button4:
                Log.i("addDigit", "Pressed 4");
                updateNumber("4");
                break;
            case R.id.button5:
                Log.i("addDigit", "Pressed 5");
                updateNumber("5");
                break;
            case R.id.button6:
                Log.i("addDigit", "Pressed 6");
                updateNumber("6");
                break;
            case R.id.button7:
                Log.i("addDigit", "Pressed 7");
                updateNumber("7");
                break;
            case R.id.button8:
                Log.i("addDigit", "Pressed 8");
                updateNumber("8");
                break;
            case R.id.button9:
                Log.i("addDigit", "Pressed 9");
                updateNumber("9");
                break;
            case R.id.button0:
                Log.i("addDigit", "Pressed 0");
                updateNumber("0");
                break;
            case R.id.buttonStar:
                Log.i("addDigit", "Pressed *");
                updateNumber("*");
                break;
            case R.id.buttonNumberSign:
                Log.i("addDigit", "Pressed #");
                updateNumber("#");
                break;
            default:
                Log.d("addDigit", "Invalid button pressed");
        }

    }

    public void call(View view) {
        Log.i("call", "Call");

        EditText phoneNumberTextView = findViewById(R.id.phoneNumber);
        String phoneNumberText = phoneNumberTextView.getText().toString();
        if (phoneNumberText.length() == 0) {
            Toast.makeText(this, "Can't call to empty number", Toast.LENGTH_LONG).show();
        }
        else {
            Uri phoneNumber = Uri.parse("tel:" + phoneNumberText);

            /* Request OS to dial */
            //Intent intent = new Intent(Intent.ACTION_DIAL, phoneNumber);
            Intent intent = new Intent(Intent.ACTION_DIAL, phoneNumber);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error while calling " + phoneNumberText, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void speedDial(String s) {
        Log.i("Speed dial", "Speed dial");

        /* Show new activity */
        Intent intent = new Intent(this, SpeedDialActivity.class);
        intent.putExtra(SPEED_DIAL_NUMBER_TAG, s);
        startActivity(intent);
    }


    public void clearDigit(View view) {
        // Clear EditText
        EditText phoneNumberTextView = findViewById(R.id.phoneNumber);
        String phoneNumberText = phoneNumberTextView.getText().toString();
        if (phoneNumberText.length() == 0) {
            phoneNumberTextView.setText(R.string.insert_a_number_using_the_keypad_below);
        }
        else {
            phoneNumberTextView.setText(phoneNumberText.substring(0, phoneNumberText.length() - 1));
        }
    }

    private void updateNumber(String digit) {
        // Update EditText
        // More advanced solutions should store the number in a list
        // This is a naive approach
        EditText phoneNumberTextView = findViewById(R.id.phoneNumber);
        String phoneNumberText = phoneNumberTextView.getText().toString();
        phoneNumberText += digit;
        phoneNumberTextView.setText(phoneNumberText);
    }

}
