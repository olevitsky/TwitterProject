package com.codepath.apps.BasicTwitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;


import com.codepath.apps.BasicTwitter.R;
import com.codepath.apps.BasicTwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import android.widget.EditText;



public class ComposeTweetActivity extends Activity {


    private TwitterClient client;
    private EditText twitterText;
    private TextView twitterCharLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        getActionBar().setTitle("Compose New Tweet");
        client = TwitterApplication.getRestClient();
         twitterText = (EditText) findViewById(R.id.etTweetBody);
        twitterCharLeft = (TextView) findViewById(R.id.tvTweetCharLeft);
        twitterText.addTextChangedListener(inputTextWatcher);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickTweetButton (View v) {
        if(isNetworkAvailable()) {
            // could use ASYNCHTTPResponseHandler, but then would need to manage JSON
            // So use JsonHandler

            client.postTweet(twitterText.getText().toString(), new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject json) {
                    Log.d("POST" , json.toString());
                    Tweet returnTweet = Tweet.fromJson(json);
                    Intent data = new Intent();
                    data.putExtra("tweet", returnTweet);
                    setResult(RESULT_OK, data); // set result code and bundle data for response
                    finish();


                }
                @Override
                public void onFailure(Throwable e, String s) {
                    Log.d("debug", e.toString());
                    Log.d("debug", s.toString());
                    finish();
                }

                @Override
                protected void handleFailureMessage(Throwable e, String s) {
                    Log.d("debug", e.toString());
                    Log.d("debug", s.toString());
                    finish();
                }
            });
        } else {
            Toast.makeText(this, "No Network available", Toast.LENGTH_SHORT).show();
            finish();
        }
         // closes the activity, pass data to parent
    }

    android.text.TextWatcher inputTextWatcher = new android.text.TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            try {
                int charsLeft = 140 - s.length();
                twitterCharLeft.setText(Integer.toString(charsLeft));
                if(charsLeft == 0) {
                    twitterCharLeft.setTextColor(Color.RED);
                }
            } catch (Exception e) {
                twitterCharLeft.setText("140");
            }
        }
    };



   /* twitterText.addTextChangedListener(new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
        int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            try {
                int charsLeft = 140 - s.length();
                twitterCharLeft.setText(Integer.toString(charsLeft));
            } catch (Exception e) {
                twitterCharLeft.setText("140");
            }
        }
    });*/

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
