package com.codepath.apps.hashtagfresh.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.hashtagfresh.R;
import com.codepath.apps.hashtagfresh.fragments.ProfileHeaderFragment;
import com.codepath.apps.hashtagfresh.fragments.UserTimelineFragment;
import com.codepath.apps.hashtagfresh.models.User;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

            ProfileHeaderFragment phFragment = ProfileHeaderFragment.newInstance(user);

            String screenName = user.getScreenName();
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flProfileHeader, phFragment);
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();

        }
    }
}
