package com.codepath.apps.hashtagfresh.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.hashtagfresh.R;
import com.codepath.apps.hashtagfresh.TwitterApplication;
import com.codepath.apps.hashtagfresh.adapters.TweetsAdapter;
import com.codepath.apps.hashtagfresh.fragments.ComposeTweetDialogFragment;
import com.codepath.apps.hashtagfresh.models.Tweet;
import com.codepath.apps.hashtagfresh.models.User;
import com.codepath.apps.hashtagfresh.network.TwitterClient;
import com.codepath.apps.hashtagfresh.util.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialogFragment.TweetCreateListener {

    /**
     * Add newly created tweet to timeline
     * @param newTweet
     */
    public void onFinishCreate(Tweet newTweet) {
        tweets.add(0, newTweet);
        adapter.notifyItemRangeInserted(0, 1);
        rvTweets.smoothScrollToPosition(0);
    }

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsAdapter adapter;
    private RecyclerView rvTweets;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        rvTweets = (RecyclerView) findViewById(R.id.lvTweets);
        rvTweets.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        client = TwitterApplication.getRestClient();

        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(getEndlessScrollListener(linearLayoutManager));

        initializeToolbar();

        // get initial tweets
        loadNextDataFromApi(0);

        // get logged in user's data
        getUserData();
    }

    /**
     * set up toolbar
     */
    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_white_48dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * get an instance of endless scroll listener
     *
     * @param linearLayoutManager
     * @return
     */
    private EndlessRecyclerViewScrollListener getEndlessScrollListener(LinearLayoutManager linearLayoutManager) {
        return new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int totalItemsCount, RecyclerView view) {
                // get tweets older than the last one currently in the list
                Tweet lastTweet = tweets.get(tweets.size()-1);
                loadNextDataFromApi(lastTweet.getUid()-1);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Set up menu item actions
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miCompose:
                showComposeTweetDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Helper function to show compose dialog tweet
     */
    private void showComposeTweetDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment composeTweetDialogFragment = ComposeTweetDialogFragment.newInstance(currentUser);
        composeTweetDialogFragment.show(fm, "compose_tweet");
    }

    /**
     * Get tweets for timeline by calling twitter timeline api
     * @param offset
     */
    private void loadNextDataFromApi(long offset) {
        client.getHomeTimeline(offset, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                tweets.addAll(Tweet.fromJsonArray(response));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    /**
     * Calls api to get the currently logged in user's data
     */
    private void getUserData() {
        client.getAccountDetails(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                currentUser = User.fromJson(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}