package com.codepath.apps.BasicTwitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.BasicTwitter.R;
import com.codepath.apps.BasicTwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimeLineActivity extends Activity {
    private final int REQUEST_CODE = 20;
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private ArrayAdapter<Tweet> aTweets;
    private ListView lvTweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        client = TwitterApplication.getRestClient();

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        populateTimeLine(-1);
        getActionBar().setTitle("Timeline");
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    private void customLoadMoreDataFromApi(int page) {
        long max_id;
        if (aTweets.getCount() == 0) {
            max_id = -1;
        } else {
            max_id = (aTweets.getItem(aTweets.getCount()-1).getUid())-1;
        }
        populateTimeLine(max_id);
    }

    public void populateTimeLine(long max_id) {
        if(isNetworkAvailable()) {
            // could use ASYNCHTTPResponseHandler, but then would need to manage JSON
            // So use JsonHandler
            client.getHomeTimeLine(max_id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    //Log.d("JSON" , jsonArray.toString());
                    ArrayList<Tweet> tweetListFromJson = Tweet.fromJSONArray(jsonArray);
                    aTweets.addAll(tweetListFromJson);
                    Tweet.deleteAll();
                    Tweet.saveAll(tweetListFromJson);
                }

                @Override
                public void onFailure(Throwable e, String s) {
                    Log.d("debug", e.toString());
                    Log.d("debug", s.toString());
                }
            });
        } else {
            aTweets.clear();
            aTweets.notifyDataSetInvalidated();
            List<Tweet> savedTweets = (List<Tweet>) Tweet.getAll();
            aTweets.addAll(savedTweets);
            Toast.makeText(this, "No Network available. Loading local copy", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time_line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.compose_tweet) {
            Intent i = new Intent(this, ComposeTweetActivity.class);
            startActivityForResult(i, REQUEST_CODE);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = (Tweet) data.getExtras().getSerializable("tweet");
           tweet.save();
            aTweets.insert(tweet, 0);
        }
        //Log.d("FIlter", "DONE WIth filter settings");
    }
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
