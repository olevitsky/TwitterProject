package com.codepath.apps.BasicTwitter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.BasicTwitter.Fragments.TweetsListFragment;
import com.codepath.apps.BasicTwitter.Fragments.UserTimeLineFragment;
import com.codepath.apps.BasicTwitter.R;
import com.codepath.apps.BasicTwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends Activity {

    private User user;
    private TweetsListFragment fragmentTweetsList;
    private TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//setupTabs();
        client = TwitterApplication.getRestClient();
        user = (User) getIntent().getSerializableExtra("user");
        getActionBar().setTitle("@" + user.getScreenName());
        loadProfileInfo();
       // Fragment frg = getFragmentManager().findFragmentById(R.id.userTimeLineFragment);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        UserTimeLineFragment userTimeline = UserTimeLineFragment.newInstance(user);
        ft.replace(R.id.flContainer_profile, userTimeline);
        ft.commit();
        //setupFragment();
    }

    private void loadProfileInfo() {
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
//ImageView ivProfileBackgroundImage = (ImageView) findViewById(R.id.ivProfileBackgroundImage);
        tvUserName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText("Following " + user.getFollowingCount());
        ImageLoader.getInstance().displayImage( user.getProfileImageUrl(), ivProfileImage);



        /*TwitterApplication.getRestClient().getProfileBannerUrl(user.getUid(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
                try {
                    String bannerUrl = json.getJSONObject("sizes").getJSONObject("mobile_retina").getString("url");

                    ImageView ivProfileBackgroundImage = (ImageView) findViewById(R.id.ivProfileBackgroundImage);
                    ImageLoader.getInstance().displayImage(bannerUrl, ivProfileBackgroundImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
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
}
