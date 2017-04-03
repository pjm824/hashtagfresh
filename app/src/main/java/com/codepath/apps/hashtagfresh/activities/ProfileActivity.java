package com.codepath.apps.hashtagfresh.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.hashtagfresh.R;
import com.codepath.apps.hashtagfresh.fragments.UserTimelineFragment;
import com.codepath.apps.hashtagfresh.models.User;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        populateProfileHeader(user);

        // get screen name
        String screenName = user.getScreenName();

        if (savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);

            // display user fragment in activity (dynamically)
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    public void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvTagLine = (TextView) findViewById(R.id.tvTagLine);
        TextView tvFollowersCount = (TextView) findViewById(R.id.tvFollowersCount);
        TextView tvFriendsCount = (TextView) findViewById(R.id.tvFriendsCount);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ImageView ivProfileBackgroundImage = (ImageView) findViewById(R.id.ivProfileBackgroundImage);

        String handle = getResources().getString(R.string.handle, user.getScreenName());

        tvName.setText(user.getName());
        tvScreenName.setText(handle);
        tvTagLine.setText(user.getTagLine());
        tvFollowersCount.setText(String.format("%d", user.getFollowersCount()));
        tvFriendsCount.setText(String.format("%d", user.getFriendsCount()));

        Glide.with(this)
            .load(user.getProfileImageUrl())
            .bitmapTransform(new RoundedCornersTransformation(this, 5, 5))
            .into(ivProfileImage);

        Glide.with(this)
            .load(user.getProfileBannerImageUrl())
            .centerCrop()
            .into(ivProfileBackgroundImage);
    }
}
