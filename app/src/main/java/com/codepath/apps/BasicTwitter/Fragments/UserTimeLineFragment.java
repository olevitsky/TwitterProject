package com.codepath.apps.BasicTwitter.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.BasicTwitter.models.Tweet;
import com.codepath.apps.BasicTwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleg on 10/5/2014.
 */
public class UserTimeLineFragment extends TweetsListFragment {
    private TweetsListFragment fragmentTweetsList;
    private User myUser;

    //private TwitterClient client;

    //populateTimeLine?

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentTweetsList = this;
        myUser = (User) getArguments().getSerializable("user");
        //client = TwitterApplication.getRestClient();
        // populateTimeLine(-1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=  super.onCreateView(inflater, container, savedInstanceState);
        populateTimeLine(-1);
        return v;
    }

    public void populateTimeLine(final long max_id) {
        if(isNetworkAvailable()) {
            // could use ASYNCHTTPResponseHandler, but then would need to manage JSON
            // So use JsonHandler
            client.getUserTimeLine(myUser.getUid(), max_id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    //Log.d("JSON" , jsonArray.toString());
                    ArrayList<Tweet> tweetListFromJson = Tweet.fromJSONArray(jsonArray);
                    if(max_id == -1) {
                        fragmentTweetsList.clear();
                        fragmentTweetsList.notifyDataSetInvalidated();
                    }

                    fragmentTweetsList.addAll(tweetListFromJson);
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
            fragmentTweetsList.clear();
            fragmentTweetsList.notifyDataSetInvalidated();
            List<Tweet> savedTweets = (List<Tweet>) Tweet.getAll();
            fragmentTweetsList.addAll(savedTweets);
            Toast.makeText(getActivity(), "No Network available. Loading local copy", Toast.LENGTH_SHORT).show();
        }
        finishedLoadingData();
    }
    public void customLoadMoreDataFromApi(int page) {
        long max_id;
        if (fragmentTweetsList.getCount() == 0) {
            max_id = -1;
        } else {
            max_id = (fragmentTweetsList.getItem(fragmentTweetsList.getCount() - 1).getUid()) - 1;
        }
        populateTimeLine(max_id);
    }

    public static UserTimeLineFragment newInstance(User currUser) {
        UserTimeLineFragment localFrg = new UserTimeLineFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", currUser);
        localFrg.setArguments(args);
        return localFrg;
    }
}
