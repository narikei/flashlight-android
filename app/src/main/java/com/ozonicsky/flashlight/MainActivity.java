package com.ozonicsky.flashlight;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private String mCameraId = null;
    private boolean isOn = false;

    @InjectView(R.id.lightButton)
    ImageButton lightButton;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        mCameraManager.registerTorchCallback(new CameraManager.TorchCallback() {
            @Override
            public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                mCameraId = cameraId;
                isOn = enabled;
            }
        }, new Handler());
    }

    @TargetApi(Build.VERSION_CODES.M)
    @OnClick(R.id.lightButton)
    public void onLight() {
        isOn = !isOn;

        try {
            if (isOn) {
                mCameraManager.setTorchMode(mCameraId, true);
                lightButton.setImageResource(R.drawable.on);
            } else {
                mCameraManager.setTorchMode(mCameraId, false);
                lightButton.setImageResource(R.drawable.off);
            }
        } catch (CameraAccessException e) {
            Log.e("change", e.getMessage());
        }
    }

}
