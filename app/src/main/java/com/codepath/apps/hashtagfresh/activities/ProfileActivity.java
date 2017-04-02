package com.codepath.apps.hashtagfresh.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.hashtagfresh.R;
import com.codepath.apps.hashtagfresh.fragments.UserTimelineFragment;
import com.codepath.apps.hashtagfresh.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

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
        TextView tvTagLine = (TextView) findViewById(R.id.tvTagLine);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        tvTagLine.setText(user.getScreenName());
//        iv.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }
}
