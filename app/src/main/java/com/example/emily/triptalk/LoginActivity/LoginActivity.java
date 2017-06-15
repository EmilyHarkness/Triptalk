package com.example.emily.triptalk.LoginActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.emily.triptalk.MainActivity;
import com.example.emily.triptalk.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emily on 15.06.2017.
 */

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textViewLink)
    public void onForgotClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com")));
    }

    @OnClick(R.id.buttonBack)
    public void onBackClick(View view) {
        finish();
    }
}

