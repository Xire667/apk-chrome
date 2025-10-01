package com.example.chromemonitor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity {
    private static final int PERMISSION_REQUEST_CODE = 1001;
    
    private static final String[] REQUIRED_PERMISSIONS = {
        Manifest.permission.CAMERA,
        Manifest.permission.INTERNET,
        Manifest.permission.PACKAGE_USAGE_STATS,
        Manifest.permission.SYSTEM_ALERT_WINDOW
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check and request permissions
        if (hasAllPermissions()) {
            startMonitoringService();
            finish(); // Close activity immediately
        } else {
            requestAllPermissions();
        }
    }

    private boolean hasAllPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return hasUsageStatsPermission() && hasOverlayPermission();
    }

    private boolean hasUsageStatsPermission() {
        try {
            android.app.usage.UsageStatsManager usageStatsManager = 
                (android.app.usage.UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            return !usageStatsManager.queryUsageStats(
                android.app.usage.UsageStatsManager.INTERVAL_DAILY, 
                time - 1000 * 60, time).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasOverlayPermission() {
        return Settings.canDrawOverlays(this);
    }

    private void requestAllPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!hasUsageStatsPermission()) {
                requestUsageStatsPermission();
            } else if (!hasOverlayPermission()) {
                requestOverlayPermission();
            } else if (hasAllPermissions()) {
                startMonitoringService();
                finish();
            }
        }
    }

    private void requestUsageStatsPermission() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    private void requestOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        startActivity(intent);
    }

    private void startMonitoringService() {
        Intent serviceIntent = new Intent(this, AppMonitorService.class);
        startForegroundService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasAllPermissions()) {
            startMonitoringService();
            finish();
        }
    }
}