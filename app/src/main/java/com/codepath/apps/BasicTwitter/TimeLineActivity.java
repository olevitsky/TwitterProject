package com.codepath.apps.BasicTwitter;

import android.app.ActionBar;
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

import com.codepath.apps.BasicTwitter.Fragments.HomeTimeLineFragment;
import com.codepath.apps.BasicTwitter.Fragments.MentionsTimeLineFragment;
import com.codepath.apps.BasicTwitter.Fragments.TweetsListFragment;
import com.codepath.apps.BasicTwitter.R;
import com.codepath.apps.BasicTwitter.listeners.FragmentTabListener;
import com.codepath.apps.BasicTwitter.models.Tweet;
import com.codepath.apps.BasicTwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimeLineActivity extends Activity {
    private final int REQUEST_CODE = 20;
    private TwitterClient client;
    //   private ArrayList<Tweet> tweets;
    //   private ArrayAdapter<Tweet> aTweets;
    //  private ListView lvTweets;

    private TweetsListFragment fragmentTweetsList;
    private User myUser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        client = TwitterApplication.getRestClient();
        setupTabs();

        // lvTweets = (ListView) findViewById(R.id.lvTweets);
        //tweets = new ArrayList<Tweet>();
        // aTweets = new TweetArrayAdapter(this, tweets);
        // lvTweets.setAdapter(aTweets);
       // fragmentTweetsList = (TweetsListFragment) getFragmentManager().findFragmentById(R.id.);
        //populateTimeLine(-1);
        getActionBar().setTitle("Timeline");
       /* lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });*/
        getUserCredentials();
    }
    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab tab1 = actionBar
                .newTab()
                .setText("Home")
                //.setIcon(R.drawable.ic_honme)
                .setTag("HomeTimelineFragment")
                .setTabListener(
                        new FragmentTabListener<HomeTimeLineFragment>(R.id.flContainer, this, "first",
                                HomeTimeLineFragment.class));

        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        ActionBar.Tab tab2 = actionBar
                .newTab()
                .setText("Mentions")
                //.setIcon(R.drawable.ic_mention)
                .setTag("MentionsTimelineFragment")
                .setTabListener(
                        new FragmentTabListener<MentionsTimeLineFragment>(R.id.flContainer, this, "second",
                                MentionsTimeLineFragment.class));

        actionBar.addTab(tab2);
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
            i.putExtra("user", myUser);
            startActivityForResult(i, REQUEST_CODE);
            return true;
        } else if (id == R.id.myProfile) {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("user", myUser);
            startActivity(i);
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
             HomeTimeLineFragment hft = (HomeTimeLineFragment) getFragmentManager().findFragmentById(R.id.flContainer);
            hft.insert(tweet, 0);
        }
        //Log.d("FIlter", "DONE WIth filter settings");
    }
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void getUserCredentials() {
        client.getCredentials(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject json) {
                myUser = User.fromJSON(json);
            }
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
                Toast.makeText(getApplicationContext(), "Can't identify the user.", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }


    // WORKS for back button. for the very first time after login when I hit back button in timeline it takes me back
    // to timeline, but on the second back it takes to phone home and if I bring app back then back always takes back to phone home.
  /* @Override
    public void onBackPressed() {
        moveTaskToBack(true);
       // doesn't help
        super.onBackPressed();
        // finish doesn't help here - same behavoir
        //finish();
    }*/
}