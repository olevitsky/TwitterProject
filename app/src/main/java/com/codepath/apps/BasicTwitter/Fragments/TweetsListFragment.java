package com.codepath.apps.BasicTwitter.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.BasicTwitter.EndlessScrollListener;
import com.codepath.apps.BasicTwitter.R;
import com.codepath.apps.BasicTwitter.TweetArrayAdapter;
import com.codepath.apps.BasicTwitter.TwitterApplication;
import com.codepath.apps.BasicTwitter.TwitterClient;
import com.codepath.apps.BasicTwitter.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleg on 10/4/2014.
 */
abstract public class TweetsListFragment extends Fragment {
    private ArrayList<Tweet> tweets;
    private ArrayAdapter<Tweet> aTweets;
    private ListView lvTweets;
    protected TwitterClient client;
    protected ProgressBar pbLoading;
    //private TweetsListFragment fragmentTweetsList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);
        //assign view references
        //lvItems = ....
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        pbLoading = (ProgressBar) v.findViewById(R.id.pbLoading);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                pbLoading.setVisibility(ProgressBar.VISIBLE);
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
        //return the layout view
        // populateTimeLine(-1);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //non view init
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<Tweet>();
        // shoudl reach activity as least as possible!!!!
        aTweets = new TweetArrayAdapter(getActivity(), tweets);
        //fragmentTweetsList = this;
    }

    public void addAll(ArrayList<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void insert(Tweet tweet, int pos) {
        aTweets.insert(tweet, pos);
    }
    public void clear() {
        aTweets.clear();
    }
    public void notifyDataSetInvalidated() {
        aTweets.notifyDataSetInvalidated();
    }
    public void addAll(List<Tweet> allSavedTweets) {
        aTweets.addAll(allSavedTweets);
    }
    public int getCount() {
        return aTweets.getCount();
    }
    public Tweet getItem(int pos) {
        return aTweets.getItem(pos);
    }
    //  abstract public void customLoadMoreDataFromApi(int page);

    abstract public void customLoadMoreDataFromApi(int page);
    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    public void finishedLoadingData() {
        pbLoading.setVisibility(ProgressBar.INVISIBLE);
    }
}
