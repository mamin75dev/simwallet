package com.example.smartcardtest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private TextView text;

    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        checkAndRequestReadPhoneStatePermission();
        button.setOnClickListener(this);
    }

    private void initViews() {
        text = findViewById(R.id.text);
        button = findViewById(R.id.button);
    }

    private void checkAndRequestReadPhoneStatePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE_PERMISSION);
        } else {
            // Permission is already granted, perform the operation
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_PHONE_STATE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, perform the operation
            } else {
                // Permission is denied, handle the case
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getImei();
        int channel = 0;
        int cla = (byte) 0x00;
        int instruction = (byte) 0xA4;
        int p1 = (byte) 0x04;
        int p2 = (byte) 0x00;
        int p3 = (byte) hexStringToByteArray("546563546f7257616c6c657400").length;

        Log.i("has privilege", String.valueOf(telephonyManager.hasCarrierPrivileges()));

//        telephonyManager.

        String data = "546563546f7257616c6c657400";
//        telephonyManager.iccOpenLogicalChannel("546563546f7257616c6c657400");
        String response = telephonyManager.iccTransmitApduLogicalChannel(channel, cla, instruction, p1, p2, p3, data);
        text.setText(response);
        Log.i("card response", response);
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
}