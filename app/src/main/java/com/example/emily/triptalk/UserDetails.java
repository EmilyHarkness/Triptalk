package com.example.emily.triptalk;

/**
 * Created by emily on 23.06.2017.
 */

public class UserDetails {
    private String avatar_url;
    private String login;
    private String url;
    private String followers;
    private String following;
    private String public_repos;

    public String getAvatar_url(){
        return avatar_url;
    }

    public String getLogin() {
        return login;
    }

    public String getUrl(){
        return url;
    }

    public String getFollowers(){
        return followers;
    }

    public String getFollowing(){
        return following;
    }

    public String getPublic_repos(){
        return public_repos;
    }
}
