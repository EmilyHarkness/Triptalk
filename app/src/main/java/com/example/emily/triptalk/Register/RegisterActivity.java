package com.example.emily.triptalk.Register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.emily.triptalk.Login.LoginActivity;
import com.example.emily.triptalk.R;

/**
 * Created by emily on 15.06.2017.
 */

public class RegisterActivity extends AppCompatActivity {
    boolean showPassword = false;
    @BindView(R.id.editTextEmail)
    EditText email;
    @BindView(R.id.editTextPassword)
    EditText password;
    @BindView(R.id.editTextFirstName)
    EditText firstName;
    @BindView(R.id.editTextLastName)
    EditText lastName;
    @BindView(R.id.imageButtonPassword)
    ImageButton btnPassword;
    SharedPreferences mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mUsers = getSharedPreferences("users", Context.MODE_PRIVATE);
    }

    @OnClick(R.id.textViewLink)
    public void onTermsClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com")));
    }

    @OnClick(R.id.buttonBack)
    public void onBackClick(View view) {
        finish();
    }

    @OnClick(R.id.imageButtonPassword)
    public void onPasswordClick(View view) {
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

    public boolean containsUsers(String value) {
        return mUsers.contains(value);
    }

    @OnClick(R.id.buttonRegisterOk)
    public void onRegisterOkClick(View view) {
        boolean reg = true;
        if (checkIsEmpty(firstName.getText().toString())) {
            firstName.setError("First name error");
            reg = false;
        }
        if (checkIsEmpty(lastName.getText().toString())) {
            lastName.setError("Last name error");
            reg = false;
        }
        if (checkIsEmpty(password.getText().toString())) {
            password.setError("Password error");
            reg = false;
        }
        if (isEmailValidation(email.getText().toString())) {
            if (!containsUsers(email.getText().toString())) {
                if (reg) {
                    SharedPreferences.Editor editor = mUsers.edit();
                    editor.putString(email.getText().toString(), password.getText().toString());
                    editor.apply();
                    Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    myIntent.putExtra("email", email.getText().toString());
                    myIntent.putExtra("password", password.getText().toString());
                    finish();
                    RegisterActivity.this.startActivity(myIntent);
                }
            } else {
                email.setError("This email is already registered");
                reg = false;
            }
        } else {
            email.setError("Email validation error");
            reg = false;
        }
    }
}
