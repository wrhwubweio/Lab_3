package com.example.lab_2;
import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MyTag";
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {

        }
        else {
            requestPermissionsNotification();
        }

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SYSTEM_ALERT_WINDOW) ==
                PackageManager.PERMISSION_GRANTED) {

        }
        else {
            requestPermissionsOverlay();
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.FOREGROUND_SERVICE) ==
                PackageManager.PERMISSION_GRANTED) {

        }
        else {
            requestPermissionsForeground();
        }
        setContentView(R.layout.main);
    }

    public void requestPermissionsNotification() {
        ActivityCompat.requestPermissions(this,
                new String[] {
                        android.Manifest.permission.POST_NOTIFICATIONS
                },
                1);
    }

    public void requestPermissionsOverlay() {
        ActivityCompat.requestPermissions(this,
                new String[] {
                        android.Manifest.permission.SYSTEM_ALERT_WINDOW
                },
                2);
    }

    public void requestPermissionsForeground() {
        ActivityCompat.requestPermissions(this,
                new String[] {
                        android.Manifest.permission.SYSTEM_ALERT_WINDOW
                },
                3);
    }

}