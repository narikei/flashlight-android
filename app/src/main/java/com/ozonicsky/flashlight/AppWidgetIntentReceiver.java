package com.ozonicsky.flashlight;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

public class AppWidgetIntentReceiver extends BroadcastReceiver {

    private static CameraManager mCameraManager;
    private static String mCameraId = null;
    public static boolean isOn = false;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        setup(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        if ("SWITCH_LIGHT".equals(intent.getAction())) {
            if (mCameraManager != null && mCameraId != null) {
                isOn = !isOn;
                try {
                    if (isOn) {
                        mCameraManager.setTorchMode(mCameraId, true);
                        remoteViews.setImageViewResource(R.id.lightButton, R.drawable.on);
                    } else {
                        mCameraManager.setTorchMode(mCameraId, false);
                        remoteViews.setImageViewResource(R.id.lightButton, R.drawable.off);
                    }
                } catch (CameraAccessException e) {
                    Log.e("change", e.getMessage());
                }
            }

            AppWidget.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setup(Context context) {
        if (mCameraManager == null || mCameraId == null) {
            mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            mCameraManager.registerTorchCallback(new CameraManager.TorchCallback() {
                @Override
                public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                    mCameraId = cameraId;
                }
            }, new Handler());
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void off() {
        if (mCameraManager != null && mCameraId != null) {
            isOn = false;
            try {
                mCameraManager.setTorchMode(mCameraId, false);
            } catch (CameraAccessException e) {
                Log.e("change", e.getMessage());
            }
        }
    }
}

