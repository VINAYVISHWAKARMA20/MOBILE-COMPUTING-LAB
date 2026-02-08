package com.example.mobilecom;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "NOTIF_CH";
    private BluetoothAdapter bluetoothAdapter;
    private TextView tvDeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        tvDeviceList = findViewById(R.id.tvDeviceList);
        EditText etMessage = findViewById(R.id.etMessage);

        createNotificationChannel();
        checkPermissions();

        findViewById(R.id.btnSendNotification).setOnClickListener(v -> {
            String msg = etMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                sendAlert("Notification", msg);
            } else {
                Toast.makeText(this, "Type something!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnTurnOn).setOnClickListener(v -> {
            if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
                startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }
        });

        findViewById(R.id.btnTurnOff).setOnClickListener(v -> {
            if (bluetoothAdapter != null) {
                bluetoothAdapter.disable();
                tvDeviceList.setText("Bluetooth: OFF");
            }
        });

        findViewById(R.id.btnDiscovery).setOnClickListener(v -> {
            if (bluetoothAdapter != null) {
                bluetoothAdapter.startDiscovery();
                tvDeviceList.setText("Finding devices...");
            }
        });

        findViewById(R.id.btnListDevices).setOnClickListener(v -> {
            if (bluetoothAdapter != null) {
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                StringBuilder sb = new StringBuilder("PAIRED DEVICES:\n");
                for (BluetoothDevice device : pairedDevices) {
                    sb.append("- ").append(device.getName()).append("\n");
                }
                tvDeviceList.setText(sb.toString());
            }
        });
    }

    private void sendAlert(String title, String content) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) v.vibrate(400);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) manager.notify(101, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "MyChannel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    private void checkPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.VIBRATE
        }, 1);
    }
}
