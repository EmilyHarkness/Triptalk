package com.example.emily.triptalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.emily.triptalk.Login.LoginActivity;
import com.example.emily.triptalk.Register.RegisterActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    boolean showHeadsetInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        HeadsetIntentReceiver receiver = new HeadsetIntentReceiver();
        registerReceiver(receiver, receiverFilter);
    }

    @OnClick(R.id.buttonRegister)
    public void onRegisterOkClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @OnClick(R.id.buttonLogin)
    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public class HeadsetIntentReceiver extends BroadcastReceiver {
        private String TAG = "HeadSet";

        public HeadsetIntentReceiver() {
            Log.d(TAG, "Created");
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case (0):
                        Log.d(TAG, "Headset unplugged");
                        if (showHeadsetInfo)
                            Toast.makeText(getApplicationContext(), R.string.headset_unplugged, Toast.LENGTH_SHORT).show();
                        showHeadsetInfo = true;
                        break;
                    case (1):
                        Log.d(TAG, "Headset plugged");
                        if (showHeadsetInfo)
                            Toast.makeText(getApplicationContext(), R.string.headset_plugged, Toast.LENGTH_SHORT).show();
                        showHeadsetInfo = true;
                        break;
                    default:
                        Log.d(TAG, "Error");
                }
            }
        }
    }
}
