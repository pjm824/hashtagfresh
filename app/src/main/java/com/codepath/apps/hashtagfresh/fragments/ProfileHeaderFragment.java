package com.codepath.apps.hashtagfresh.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.hashtagfresh.R;
import com.codepath.apps.hashtagfresh.models.User;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileHeaderFragment extends Fragment{
    TextView tvName;
    TextView tvScreenName;
    TextView tvTagLine;
    TextView tvFollowersCount;
    TextView tvFriendsCount;

    ImageView ivProfileImage;
    ImageView ivProfileBackgroundImage;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_header, parent, false);
        tvName = (TextView) v.findViewById(R.id.tvName);
        tvScreenName = (TextView) v.findViewById(R.id.tvScreenName);
        tvTagLine = (TextView) v.findViewById(R.id.tvTagLine);
        tvFollowersCount = (TextView) v.findViewById(R.id.tvFollowersCount);
        tvFriendsCount = (TextView) v.findViewById(R.id.tvFriendsCount);

        ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
        ivProfileBackgroundImage = (ImageView) v.findViewById(R.id.ivProfileBackgroundImage);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get the user instance so that we can set the profile image, name, etc
        User user = Parcels.unwrap(getArguments().getParcelable("user"));
        populateProfileHeader(user);
    }

    public static ProfileHeaderFragment newInstance(User user) {
        ProfileHeaderFragment frag = new ProfileHeaderFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", Parcels.wrap(user));
        frag.setArguments(args);
        return frag;
    }

    public void populateProfileHeader(User user) {
        String handle = getResources().getString(R.string.handle, user.getScreenName());

        tvName.setText(user.getName());
        tvScreenName.setText(handle);
        tvTagLine.setText(user.getTagLine());
        tvFollowersCount.setText(String.format("%d", user.getFollowersCount()));
        tvFriendsCount.setText(String.format("%d", user.getFriendsCount()));

        Glide.with(this)
                .load(user.getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(getActivity(), 5, 5))
                .into(ivProfileImage);

        Glide.with(this)
                .load(user.getProfileBannerImageUrl())
                .centerCrop()
                .into(ivProfileBackgroundImage);
    }
}
