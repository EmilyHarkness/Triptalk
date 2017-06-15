package com.example.emily.triptalk.LoginActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emily.triptalk.MainActivity;
import com.example.emily.triptalk.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emily on 15.06.2017.
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.editTextEmailLogin)
    EditText email;
    @BindView(R.id.editTextPasswordLogin)
    EditText password;
    SharedPreferences mSettings;

    @Nullable
    String extraEmail;
    @Nullable
    String extraPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mSettings = getSharedPreferences("users", MODE_PRIVATE);
        Intent intent = getIntent();
        extraEmail = intent.getStringExtra("email");
        extraPassword = intent.getStringExtra("password");
        if (extraEmail != null && extraPassword != null) {
            email.setText(extraEmail);
            password.setText(extraPassword);
        }
    }

    @OnClick(R.id.textViewLink)
    public void onForgotClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com")));
    }

    @OnClick(R.id.buttonBack)
    public void onBackClick(View view) {
        finish();
    }

    public boolean checkIsEmpty(String value) {
        return value.isEmpty();
    }

    public boolean containsUsers(String email, String password) {
        return mSettings.getString(email, "").equals(password);
    }

    @OnClick(R.id.buttonLoginOk)
    public void onLoginOkClick(View view) {
        if (checkIsEmpty(password.getText().toString())) {
            password.setError("Password error");
            return;
        }
        if (containsUsers(email.getText().toString(), password.getText().toString())) {
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            email.setError("Email error or incorrect password");
        }
    }
}