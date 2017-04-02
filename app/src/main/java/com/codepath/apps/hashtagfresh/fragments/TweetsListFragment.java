package com.codepath.apps.hashtagfresh.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.hashtagfresh.R;
import com.codepath.apps.hashtagfresh.adapters.TweetsAdapter;
import com.codepath.apps.hashtagfresh.models.Tweet;
import com.codepath.apps.hashtagfresh.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public abstract class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetsAdapter adapter;
    private RecyclerView rvTweets;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        rvTweets = (RecyclerView) v.findViewById(R.id.lvTweets);
        rvTweets.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(linearLayoutManager);
        // Add the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(getEndlessScrollListener(linearLayoutManager));
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.tweets = new ArrayList<>();
        adapter = new TweetsAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> tweets) {
        this.tweets.addAll(tweets);
        adapter.notifyDataSetChanged();
    }

    public void addTweet(Tweet tweet) {
        this.tweets.add(0, tweet);
        adapter.notifyItemRangeInserted(0, 1);
        rvTweets.smoothScrollToPosition(0);
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
                getTweets(lastTweet.getUid()-1);
            }
        };
    }

    abstract void getTweets(long lastUid);
}
