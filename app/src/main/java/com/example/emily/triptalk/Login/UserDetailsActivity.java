package com.example.emily.triptalk.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emily.triptalk.GitHubService;
import com.example.emily.triptalk.R;
import com.example.emily.triptalk.User;
import com.example.emily.triptalk.UserDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.emily.triptalk.R.id.imageView;

/**
 * Created by emily on 22.06.2017.
 */

public class UserDetailsActivity extends AppCompatActivity {
    private String log;
    UserDetails userDetails;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.url)
    TextView url;
    @BindView(R.id.followers)
    TextView followers;
    @BindView(R.id.following)
    TextView following;
    @BindView(R.id.repositories)
    TextView repositories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        //TODO: load data into user details
        log = getIntent().getStringExtra("login");
        login.setText(log);
        //avatar

        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        Call<UserDetails> call = gitHubService.getUserDetails(log);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.isSuccessful()) {
                    //ok
                    userDetails = response.body();
                    Picasso.with(UserDetailsActivity.this).load(userDetails.getAvatar_url()).into(avatar);
                    url.setText(userDetails.getUrl());
                    followers.setText(userDetails.getFollowers());
                    following.setText(userDetails.getFollowing());
                    repositories.setText(userDetails.getPublic_repos());
                } else {
                    //bad request
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                //no network
            }
        });
    }


}
