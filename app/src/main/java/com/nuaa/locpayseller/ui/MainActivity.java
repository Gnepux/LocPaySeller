package com.nuaa.locpayseller.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nuaa.locpayseller.R;
import com.nuaa.locpayseller.permission.PermissionManager;
import com.nuaa.locpayseller.permission.RequestPermCallback;
import com.nuaa.locpayseller.util.RSAUtil;

import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void register(View view) {
        PermissionManager.getInstance().requestPermission(this, "需开启定位权限", new RequestPermCallback() {
            @Override
            protected void onGrant() {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            protected void onDeny() {
                Toast.makeText(MainActivity.this, "开启定位权限失败", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    public void getCash(View view) {
        PermissionManager.getInstance().requestPermission(this, "需开启定位权限", new RequestPermCallback() {
            @Override
            protected void onGrant() {
                Intent intent = new Intent(MainActivity.this, GetCashActivity.class);
                startActivity(intent);
            }

            @Override
            protected void onDeny() {
                Toast.makeText(MainActivity.this, "开启定位权限失败", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }
}
