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

    @OnClick(R.id.buttonRegisterOk)
    public void onRegisterOkClick(View view) {
        if(firstName.getText().toString().isEmpty()) {
            firstName.setError("Error first name");
            return;
        }
        if(lastName.getText().toString().isEmpty()) {
            lastName.setError("Error last name");
            return;
        }
        if(password.getText().toString().isEmpty()) {
            password.setError("Error password");
            return;
        }
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            if (!(mSettings.contains(email.getText().toString()) || email.getText().toString().isEmpty())) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(email.getText().toString(), password.getText().toString());
                editor.apply();
                Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                email.setError("Error email");
            }
        } else {
            email.setError("Email valid error");
        }
    }
}
