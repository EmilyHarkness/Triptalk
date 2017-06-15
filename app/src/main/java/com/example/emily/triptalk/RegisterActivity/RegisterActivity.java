package com.example.emily.triptalk.RegisterActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.emily.triptalk.LoginActivity.LoginActivity;
import com.example.emily.triptalk.R;

/**
 * Created by emily on 15.06.2017.
 */

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.editTextEmail)
    EditText email;
    @BindView(R.id.editTextPassword)
    EditText password;
    @BindView(R.id.editTextFirstName)
    EditText firstName;
    @BindView(R.id.editTextLastName)
    EditText lastName;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mSettings = getSharedPreferences("users", Context.MODE_PRIVATE);
    }

    @OnClick(R.id.textViewLink)
    public void onTermsClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com")));
    }

    @OnClick(R.id.buttonBack)
    public void onBackClick(View view) {
        finish();
    }

    public boolean isEmailValidation(String value)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

    public boolean checkIsEmpty(String value)
    {
        return value.isEmpty();
    }

    @OnClick(R.id.buttonRegisterOk)
    public void onRegisterOkClick(View view) {
        if (checkIsEmpty(firstName.getText().toString())) {
            firstName.setError("First name error");
            return;
        }
        if (checkIsEmpty(lastName.getText().toString())) {
            lastName.setError("Last name error");
            return;
        }
        if (checkIsEmpty(password.getText().toString())) {
            password.setError("Password error");
            return;
        }
        if (isEmailValidation(email.getText().toString())) {
            if (!mSettings.contains(email.getText().toString())) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(email.getText().toString(), password.getText().toString());
                editor.apply();
                Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                myIntent.putExtra("email", email.getText().toString());
                myIntent.putExtra("password", password.getText().toString());
                finish();
                RegisterActivity.this.startActivity(myIntent);
            } else {
                email.setError("Email error");
            }
        } else {
            email.setError("Email validation error");
        }
    }
}
