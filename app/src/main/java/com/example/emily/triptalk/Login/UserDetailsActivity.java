package com.example.emily.triptalk.Login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emily.triptalk.GitHubService;
import com.example.emily.triptalk.R;
import com.example.emily.triptalk.Register.RegisterActivity;
import com.example.emily.triptalk.User;
import com.example.emily.triptalk.UserAdapter;
import com.example.emily.triptalk.UserDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    @BindView(R.id.followers)
    Button followers;
    @BindView(R.id.following)
    Button following;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.repositories)
    TextView repositories;
    @BindView(R.id.buttonOpen)
    Button btnOpen;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);

        progressBar.setVisibility(ProgressBar.VISIBLE);

        log = getIntent().getStringExtra("login");
        if (log == null) {
            String link = (getIntent().getData()).toString();
            String[] separated = link.split("/");
            log = separated[3];
            if (separated.length > 4)
                finish();
        }
        login.setText(log);

        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        Call<UserDetails> call = gitHubService.getUserDetails(log);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.isSuccessful()) {
                    //ok
                    userDetails = response.body();
                    Picasso.with(UserDetailsActivity.this).load(userDetails.getAvatar_url()).into(avatar);
                    followers.setText(getResources().getString(R.string.followers) + " " + userDetails.getFollowers());
                    following.setText(getResources().getString(R.string.following) + " " + userDetails.getFollowing());
                    if (userDetails.getName() == null) {
                        name.setVisibility(View.GONE);
                    } else {
                        name.setText(getResources().getString(R.string.name) + " " + userDetails.getName());
                    }
                    if (userDetails.getEmail() == null) {
                        email.setVisibility(View.GONE);
                    } else {
                        email.setText(getResources().getString(R.string.email) + " " + userDetails.getEmail());
                    }
                    if (userDetails.getLocation() == null) {
                        location.setVisibility(View.GONE);
                    } else {
                        location.setText(getResources().getString(R.string.location) + " " + userDetails.getLocation());
                    }
                    if (userDetails.getCompany() == null) {
                        company.setVisibility(View.GONE);
                    } else {
                        company.setText(getResources().getString(R.string.company) + " " + userDetails.getCompany());
                    }
                    repositories.setText(getResources().getString(R.string.repositories) + " " + userDetails.getPublic_repos());
                } else {
                    //bad request
                }
                progressBar.setVisibility(ProgressBar.GONE);
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                //no network
                progressBar.setVisibility(ProgressBar.GONE);
            }
        });
    }

    @OnClick(R.id.buttonOpen)
    public void onOpenClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(userDetails.getHtmlUrl())));
    }

    @OnClick(R.id.avatar)
    public void onAvatarClick(View view) {
        //startActivity(new Intent(this, FullScreenActivity.class));
        Intent intent = new Intent(this, FullScreenActivity.class);
        intent.putExtra("image", userDetails.getAvatar_url());
        startActivity(intent);
    }
}
