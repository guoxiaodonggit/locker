package com.zero.locker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zero.locker.R;
import com.zero.locker.lock.LockerProtectService;
import com.zero.locker.lock.LockerService;
import com.zero.locker.util.Config;

/**
 * @author
 * @version 1.0
 * @date 16-2-1
 */
public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ThemeWaterActivity.class));
            }
        });
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.sIsLock = true;
                startService(new Intent(MainActivity.this, LockerService.class));
                startService(new Intent(MainActivity.this, LockerProtectService.class));
                LockerProtectService.startPollingService(MainActivity.this, 60, 
                        LockerProtectService.class);
            }
        });
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.sIsLock = false;
                stopService(new Intent(MainActivity.this, LockerService.class));
                stopService(new Intent(MainActivity.this, LockerProtectService.class));
                LockerProtectService.stopPollingService(MainActivity.this, 
                        LockerProtectService.class);
            }
        });
        
    }
}
