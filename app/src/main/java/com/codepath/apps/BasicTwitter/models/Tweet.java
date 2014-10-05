package com.codepath.apps.BasicTwitter.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by oleg on 9/27/2014.
 */

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable{
    private static final long serialVersionUID = 1102853432916723377L;

    @Column(name = "body")
    private String body;

    @Column(name = "uid" , unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "createAt")
    private String createAt;

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;



     public static Tweet fromJson(JSONObject jsonObject) {
         Tweet tweet = new Tweet();
         // extract values from JSON to populate member var's
         try {
             tweet.body = jsonObject.getString("text");
             tweet.uid = jsonObject.getLong("id");
             tweet.createAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
         } catch (JSONException e) {
             e.printStackTrace();
             return null;
         }
         return tweet;
     }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Tweet tweet = Tweet.fromJson(tweetJson);
            tweets.add(tweet);
        }
        return tweets;
    }
    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public User getUser() {
        return user;
    }


    @Override
    public String toString() {
        return (this.body + " - " + user.getScreenName());
    }

    //db utilities
    public static List<Tweet> getAll() {//Category category) {
    // SQl query
        return new Select()
                .from(Tweet.class)
                .orderBy("uid DESC")
                .execute();
    }
    public static void  saveAll(ArrayList<Tweet> tweetList) {
        for (int i=0; i<tweetList.size(); i++) {
            //tweetList.get(i).getUser().save();
            Tweet tweet = tweetList.get(i);
            tweet.getUser().save();
            tweet.save();
        }
    }
    public static void deleteAll(){
        new Delete().from(Tweet.class).execute();
    }

}
