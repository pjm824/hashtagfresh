package com.codepath.apps.hashtagfresh.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.hashtagfresh.R;
import com.codepath.apps.hashtagfresh.activities.ProfileActivity;
import com.codepath.apps.hashtagfresh.models.Tweet;
import com.codepath.apps.hashtagfresh.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        User user;
        ImageView ivProfileImage;
        TextView tvUserName;
        TextView tvBody;
        TextView tvName;
        TextView tvTime;

        private final Context context;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);

            context = itemView.getContext();

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
        // Get the data model based on position
        Tweet tweet = mTweets.get(position);

        // populate data into subviews
        Resources res = getContext().getResources();
        viewHolder.user = tweet.getUser();
        String handle = res.getString(R.string.handle, viewHolder.user.getScreenName());
        viewHolder.tvUserName.setText(handle);
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvName.setText(tweet.getUser().getName());
        viewHolder.tvTime.setText(tweet.getRelativeTimestamp());
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivProfileImage);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}
