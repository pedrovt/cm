package com.cm.lab1.helloactivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE_TAG = "com.cm.lab1.helloactivity.extra.MESSAGE";
    private static final int TEXT_REQUEST = 1;

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

        // Receive an implicit intent
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            Toast.makeText(this, "URI is " + uri.toString(), Toast.LENGTH_LONG).show();
        }
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

    /* Button Click Handlers */
    public void openWebsite(View view) {
        Log.i("MainActivity", "Open Website");

        EditText urlText = findViewById(R.id.text_website);
        String url = urlText.getText().toString();
        Uri webpage = Uri.parse(url);

        /* Request OS to open website */

        // implicit intent -> I don't know the class to open
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("openWebsite", "Can't handle opening the website!");
        }
}

    public void openLocation(View view) {
        Log.i("MainActivity", "Open Location");

        EditText urlText = findViewById(R.id.text_location);
        String loc = urlText.getText().toString();
        Uri addressUri = Uri.parse("geo:0,0?q=" + loc);

        /* Request OS to open website */
        Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("openWebsite", "Can't handle opening the location!");
        }
    }

    public void share(View view) {
        Log.i("MainActivity", "Share");

        EditText shareText = findViewById(R.id.text_share);
        String share = shareText.getText().toString();
        String mimeType = "text/plain";

        /* Request OS to share */
        ShareCompat.IntentBuilder
                   .from(this)
                   .setType(mimeType)
                   .setChooserTitle(R.string.share_message)
                   .setText(share)
                   .startChooser();
    }

    public void sendMessage(View view) {
        Log.i("MainActivity", "Send message");

        EditText messageToSend = findViewById(R.id.text_msg_to_send);

        /* Request OS to open new page */

        // explicit intent -> I know the class to open
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(MESSAGE_TAG, messageToSend.getText().toString());       // TO STRING is important
        startActivityForResult(intent, TEXT_REQUEST);

        // implicit intent -> I don't know the class to open (e.g. I want to open a browser)
    }

}
