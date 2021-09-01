package com.clwater.wxpictureexport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.clwater.wxpictureexport.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        initView();
    }

    private void initView() {
        binding.btMainOpenacc.setOnClickListener(v -> {
            if (!WxPictureExportService.isRunning()) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });
        binding.btMainStart.setOnClickListener(v -> AutoManager.INSTANCE.start(MainActivity.this));
    }


}