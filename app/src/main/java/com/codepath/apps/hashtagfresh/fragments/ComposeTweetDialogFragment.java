package com.codepath.apps.hashtagfresh.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
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

    ImageView ivProfileImage;
    TextView tvUserName;
    TextView tvName;

    EditText etTweetBody;
    TextView tvCharacterCount;

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

    /**
     * Count remaining characters
     */
    private final TextWatcher characterCountWatcher = new TextWatcher()
    {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setRemainingCount();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
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
        setUpViews(user);

        // Show soft keyboard automatically and request focus to field
        etTweetBody.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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


    private void setUpViews(User user) {
        // set views to instance variables
        ivProfileImage = (ImageView) getView().findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) getView().findViewById(R.id.tvUserName);
        tvName = (TextView) getView().findViewById(R.id.tvName);
        etTweetBody = (EditText) getView().findViewById(R.id.etTweetBody);
        tvCharacterCount = (TextView) getView().findViewById(R.id.tvCharacterCount);
        btnSave = (Button) getView().findViewById(R.id.btnSave);
        btnCancel = (Button) getView().findViewById(R.id.btnCancel);

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

        // add listener and initialize remaining count text view
        etTweetBody.addTextChangedListener(characterCountWatcher);
        setRemainingCount();
    }

    private void setRemainingCount() {
        Resources res = getContext().getResources();
        String remainingCount = res.getString(R.string.character_count, 140 - etTweetBody.getText().toString().length());
        tvCharacterCount.setText(remainingCount);
    }

    public interface TweetCreateListener {
        void onFinishCreate(Tweet tweet);
    }
}