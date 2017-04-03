package com.codepath.apps.hashtagfresh.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.apps.hashtagfresh.R;
import com.codepath.apps.hashtagfresh.activities.ProfileActivity;
import com.codepath.apps.hashtagfresh.databinding.ItemTweetBinding;
import com.codepath.apps.hashtagfresh.models.Tweet;
import com.codepath.apps.hashtagfresh.models.User;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        User user;

        private final Context context;
        final ItemTweetBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            binding = ItemTweetBinding.bind(itemView);

            ImageView ivProfileImage = binding.tweetHeader.ivProfileImage;
            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // launch profile view
                    Intent i = new Intent(getContext(), ProfileActivity.class);
                    i.putExtra("user", Parcels.wrap(user));
                    context.startActivity(i);
                }
            });
        }
    }

    private List<Tweet> mTweets;
    private Context mContext;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        mTweets = tweets;
        mContext = context;
    }

    /**
     * Inflates and returns a new viewholder instance
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public TweetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    /**
     * Sets the data in the viewholder with the tweet @ the given position
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(TweetsAdapter.ViewHolder viewHolder, int position) {
        final Tweet tweet = mTweets.get(position);

        viewHolder.user = tweet.getUser();

        viewHolder.binding.setTweet(tweet);  // setVariable(BR.user, user) would also work
        viewHolder.binding.executePendingBindings();   // update the view now

        ImageView ivProfileImage = viewHolder.binding.tweetHeader.ivProfileImage;
        ivProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(getContext(), 5, 5))
                .into(ivProfileImage);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}
