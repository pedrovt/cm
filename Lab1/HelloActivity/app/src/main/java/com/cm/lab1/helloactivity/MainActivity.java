package com.cm.lab1.helloactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE_TAG = "com.cm.lab1.helloactivity.extra.MESSAGE";
    private static final int TEXT_REQUEST = 1;

    private EditText messageToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Restore the saved state.
        // Can also be done on onRestoreInstanceState(), which is called after onStart()
        // after the Activity is created.
        // See onSaveInstanceState() for what gets saved.
        /*if (savedInstanceState != null) {
            ...
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /* Save the Activity instance state with onSaveInstanceState() */
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity", "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy");
    }

    public void sendMessage(View view) {
        Log.i("MainActivity", "Just clicked send!");

        messageToSend = findViewById(R.id.txtMsgToSend);

        /* Request OS to open new page */

        // explicit intent -> I know the class to open
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(MESSAGE_TAG, messageToSend.getText().toString());       // TO STRING is important
        startActivityForResult(intent, TEXT_REQUEST);

        // implicit intent -> I don't know the class to open (e.g. I want to open a browser)
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Returned from second activity", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
