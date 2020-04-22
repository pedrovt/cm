package com.cm.hw.dialler;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
