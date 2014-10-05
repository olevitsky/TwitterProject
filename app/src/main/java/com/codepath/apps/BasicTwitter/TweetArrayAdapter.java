package com.codepath.apps.BasicTwitter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.BasicTwitter.models.Tweet;
import com.codepath.apps.BasicTwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by oleg on 9/27/2014.
 */
public class TweetArrayAdapter extends ArrayAdapter <Tweet> {
    private Context context;
    public TweetArrayAdapter(Context context, List<Tweet> tweets)
    {
        super(context, 0 , tweets);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get data from item posiiton
        Tweet tweet = getItem((position));
        View v;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.tweet_item, parent, false);
        } else {
            v = convertView;
        }
        //Find the item within template
        ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImageHeader);
        TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        TextView tvUserScreenName = (TextView) v.findViewById(R.id.tvScreenName);
        TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
        TextView timeStamp = (TextView) v.findViewById(R.id.tvTimestamp);

        ivProfileImage.setImageResource(android.R.color.transparent);
        ImageLoader imageLoader = ImageLoader.getInstance();
        //populate views
        imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
        tvUserName.setText(tweet.getUser().getName());
        tvUserScreenName.setText(("@"+tweet.getUser().getScreenName()));
        tvBody.setText(Html.fromHtml(tweet.getBody()).toString());
        timeStamp.setText(getRelativeTimeStamp(tweet.getCreateAt()));
        ivProfileImage.setTag(tweet.getUser());
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileActivity.class);
                User user = (User) v.getTag();
                i.putExtra("user", user);
                context.startActivity(i);
                //Toast.makeText(getContext(), "ImageClick", Toast.LENGTH_SHORT).show();
            }
        });

        return v;

    }

    public String getRelativeTimeStamp(String rawJsonDate) {
        String twitTimeFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(twitTimeFormat, Locale.ENGLISH);
        sdf.setLenient(true);
        String relativeTime = "";

        try {
            long dateMillis = sdf.parse(rawJsonDate).getTime();
            if(System.currentTimeMillis() < dateMillis) {
                relativeTime = "now";
            } else {
                relativeTime = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String returnRelativeTime = relativeTime.replaceAll(" hour.* ago", "h");

        returnRelativeTime = returnRelativeTime.replaceAll("Yesterday", "1d");
        returnRelativeTime = returnRelativeTime.replaceAll(" year.* ago", "y");

        returnRelativeTime = returnRelativeTime.replaceAll(" minute.* ago", "m");
        returnRelativeTime = returnRelativeTime.replaceAll(" second.* ago", "s");
        returnRelativeTime = returnRelativeTime.replaceAll(" day.* ago", "d");
        return returnRelativeTime;
    }
}
