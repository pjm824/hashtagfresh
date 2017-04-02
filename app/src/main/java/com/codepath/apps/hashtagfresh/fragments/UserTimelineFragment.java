package com.codepath.apps.hashtagfresh.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.hashtagfresh.TwitterApplication;
import com.codepath.apps.hashtagfresh.models.Tweet;
import com.codepath.apps.hashtagfresh.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        // get initial tweets
        getTweets(0);
    }

    /**
     * Get tweets for timeline by calling twitter timeline api
     * @param lastUid
     */
    void getTweets(long lastUid) {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(screenName, lastUid, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                addAll(Tweet.fromJsonArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
