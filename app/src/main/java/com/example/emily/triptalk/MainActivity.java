package com.example.emily.triptalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.emily.triptalk.LoginActivity.LoginActivity;
import com.example.emily.triptalk.RegisterActivity.RegisterActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegister = (Button)findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(this);
        btnLogin = (Button)findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.buttonLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
