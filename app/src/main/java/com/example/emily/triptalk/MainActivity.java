package com.example.emily.triptalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.emily.triptalk.Login.LoginActivity;
import com.example.emily.triptalk.Register.RegisterActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonRegister)
    public void onRegisterOkClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @OnClick(R.id.buttonLogin)
    public void onLiginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
