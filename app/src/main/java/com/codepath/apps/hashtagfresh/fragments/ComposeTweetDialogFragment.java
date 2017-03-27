package com.codepath.apps.hashtagfresh.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.hashtagfresh.R;
import com.codepath.apps.hashtagfresh.TwitterApplication;
import com.codepath.apps.hashtagfresh.models.Tweet;
import com.codepath.apps.hashtagfresh.models.User;
import com.codepath.apps.hashtagfresh.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeTweetDialogFragment extends DialogFragment {

    public interface TweetCreateListener {
        void onFinishCreate(Tweet tweet);
    }

    ImageView ivProfileImage;
    TextView tvUserName;
    TextView tvName;

    EditText etTweetBody;

    Button btnSave;
    Button btnCancel;

    /**
     * listener for save button
     */
    View.OnClickListener btnSaveOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            TwitterClient client = TwitterApplication.getRestClient();
            client.setStatus(etTweetBody.getText().toString(), new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());
                    Tweet newTweet = Tweet.fromJson(response);
                    TweetCreateListener listener = (TweetCreateListener) getActivity();
                    listener.onFinishCreate(newTweet);
                    dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
        }
    };

    /**
     * listener for cancel button
     */
    View.OnClickListener btnCancelOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            dismiss();
        }
    };

    public ComposeTweetDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ComposeTweetDialogFragment newInstance(User user) {
        ComposeTweetDialogFragment frag = new ComposeTweetDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", Parcels.wrap(user));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compose_tweet, container);

        // remove dialog fragment header
        ComposeTweetDialogFragment.this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get the user instance so that we can set the profile image, name, etc
        User user = Parcels.unwrap(getArguments().getParcelable("user"));

        setUpViews(view, user);

        // Show soft keyboard automatically and request focus to field
        etTweetBody.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void setUpViews(View view, User user) {
        // set views to instance variables
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvName = (TextView) view.findViewById(R.id.tvName);
        etTweetBody = (EditText) view.findViewById(R.id.etTweetBody);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        // set user profile image, username, etc
        Resources res = getContext().getResources();
        String handle = res.getString(R.string.handle, user.getScreenName());
        tvUserName.setText(handle);
        tvName.setText(user.getName());
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(ivProfileImage);

        // set up save and cancel buttons
        btnSave.setOnClickListener(btnSaveOnClick);
        btnCancel.setOnClickListener(btnCancelOnClick);
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }
}