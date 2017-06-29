package com.example.emily.triptalk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Created by emily on 22.06.2017.
 */

public class User {
    private int id;
    private String avatar_url;
    private String login;

    public int getId() {
        return id;
    }

    public String getAvatar_url(){
        return avatar_url;
    }

    public String getLogin() {
        return login;
    }
}
