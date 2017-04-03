package com.codepath.apps.hashtagfresh.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    public long uid;
    public String name;
    public String screenName;
    public String profileImageUrl;
    public String profileBannerImageUrl;
    public String tagLine;
    public int followersCount;
    public int friendsCount;

    // empty constructor needed by the Parceler library
    public User() {
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileBannerImageUrl() {
        return profileBannerImageUrl;
    }

    public String getTagLine() {
        return tagLine;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public static User fromJson(JSONObject jsonObject) {
        User user = new User();

        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            String profileImageNormal = jsonObject.getString("profile_image_url");
            user.profileImageUrl = profileImageNormal.replace("normal", "bigger");
            user.profileBannerImageUrl = jsonObject.getString("profile_banner_url") + "/mobile_retina";
            user.tagLine = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.friendsCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
