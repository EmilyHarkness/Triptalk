package com.example.emily.triptalk.Login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
    boolean showPassword = false;
    @BindView(R.id.editTextEmailLogin)
    EditText email;
    @BindView(R.id.editTextPasswordLogin)
    EditText password;
    @BindView(R.id.imageButtonPasswordLogin)
    ImageButton btnPassword;
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

    @OnClick(R.id.imageButtonPasswordLogin)
    public void onPasswordLoginClick(View view) {
        if (showPassword) {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnPassword.setActivated(false);
            showPassword = false;
        } else {
            password.setInputType(InputType.TYPE_CLASS_TEXT);
            btnPassword.setActivated(true);
            showPassword = true;
        }
    }

    public boolean checkIsEmpty(String value) {
        return value.isEmpty();
    }

    public boolean isEmailValidation(String value) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

    public boolean containsUsers(String email, String password) {
        return mSettings.getString(email, "").equals(password);
    }

    @OnClick(R.id.buttonLoginOk)
    public void onLoginOkClick(View view) {
        boolean log = true;
        if (checkIsEmpty(password.getText().toString())) {
            password.setError("Password error");
            log = false;
        }
        if (isEmailValidation(email.getText().toString())) {
            if (containsUsers(email.getText().toString(), password.getText().toString()) && log) {
                startActivity(new Intent(this, UsersActivity.class));
                finish();
            } else {
                email.setError("Email error or incorrect password");
                log = false;
            }
        } else {
            email.setError("Email validation error");
            log = false;
        }
    }
}