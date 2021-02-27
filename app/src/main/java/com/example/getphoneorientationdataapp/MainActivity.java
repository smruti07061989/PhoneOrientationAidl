package com.example.getphoneorientationdataapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainAtivity";
    IMyAidlInterface service;
    AdditionServiceConnection connection;
    private TextView showResultTv;
    float result ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showResultTv = findViewById(R.id.showResultTv);
        initService();
    }

    @Override
    protected void onDestroy() {
        releaseService();
        super.onDestroy();
    }

    private void releaseService() {
        unbindService(connection);
        connection = null;
        Log.d(TAG, "releaseService() unbound.");
    }

    private void initService() {
        connection = new AdditionServiceConnection();
        Intent i = new Intent();
        i.setClassName("com.example.getphoneorientationdataapp", com.example.getphoneorientationdataapp.ExternalService.class.getName());
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "initService() bound with " + ret);
    }

    public void showResult(View view) throws RemoteException {
        result=service.showOrientationData();
        showResultTv.setText((int) result);

    }

    private class AdditionServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service =IMyAidlInterface.Stub.asInterface((IBinder) boundService);
            Log.d(TAG, "onServiceConnected() connected");
            Toast.makeText(MainActivity.this, "Service connected", Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.d(TAG, "onServiceDisconnected() disconnected");
            Toast.makeText(MainActivity.this, "Service connected", Toast.LENGTH_LONG)
                    .show();
        }
    }

}