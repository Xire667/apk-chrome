package com.example.chromemonitor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppMonitorService extends Service {
    private static final String TAG = "AppMonitorService";
    private static final String CHANNEL_ID = "AppMonitorChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final String CHROME_PACKAGE = "com.android.chrome";
    
    private ScheduledExecutorService executor;
    private UsageStatsManager usageStatsManager;
    private CameraHelper cameraHelper;
    private long lastChromeOpenTime = 0;
    private Handler mainHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
        
        mainHandler = new Handler(Looper.getMainLooper());
        usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        cameraHelper = new CameraHelper(this);
        
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());
        
        startMonitoring();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "App Monitor Service",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Monitorea cuando se abre Chrome");
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Chrome Monitor Activo")
                .setContentText("Monitoreando aplicaciones...")
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
    }

    private void startMonitoring() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::checkForChromeOpen, 0, 1, TimeUnit.SECONDS);
    }

    private void checkForChromeOpen() {
        try {
            long currentTime = System.currentTimeMillis();
            long startTime = currentTime - 2000; // Últimos 2 segundos
            
            UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, currentTime);
            UsageEvents.Event event = new UsageEvents.Event();
            
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                
                if (event.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED &&
                    CHROME_PACKAGE.equals(event.getPackageName()) &&
                    event.getTimeStamp() > lastChromeOpenTime) {
                    
                    Log.d(TAG, "Chrome abierto detectado!");
                    lastChromeOpenTime = event.getTimeStamp();
                    
                    // Ejecutar captura de foto en el hilo principal
                    mainHandler.post(() -> {
                        cameraHelper.takePicture();
                    });
                    
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking for Chrome open", e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Reiniciar el servicio si es terminado
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        
        if (cameraHelper != null) {
            cameraHelper.cleanup();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}