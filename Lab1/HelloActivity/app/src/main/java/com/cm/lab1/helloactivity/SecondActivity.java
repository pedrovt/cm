package com.cm.lab1.helloactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent whoCalledMe = getIntent();
        String message = whoCalledMe.getStringExtra(MainActivity.MESSAGE_TAG);

        EditText text = findViewById(R.id.messageText);
        text.setText(message);
    }

    public void returnButton(View view) {
        /* Return to main activity */
        Intent replyIntent = new Intent();
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
