package com.example.mymob_horoscope;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymob_horoscope.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BluetoothAdapter bluetoothAdapter;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    Context context;


    private static final String FILE_NAME = "myMobHoroscope.txt";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnSubmit.setOnClickListener(v -> {
            deviceIP();
            deviceTime();
            blutoothStatus();
            deviceName();
            deviceSDKVERSION();
            deviceIMEI();

        });

        binding.btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String deviceName = binding.tvDeviceName.getText().toString();
                ArrayList<String> list = new ArrayList<>();
                list.add("Device: " + binding.tvDeviceName.getText().toString());
                list.add("Blutooth: " + binding.tvBlutooth.getText().toString());
                list.add("Date: " + binding.tvTime.getText().toString());
                list.add("IP: " + binding.tvIp.getText().toString());
                list.add("IMEI: " + binding.tvImei.getText().toString());
                Log.e("List", "onClick: " + list);
                // generateFileFromString(context, FILE_NAME, "text", list.toString());
                Log.e("File", "onClick: " + list.toString());


                File file = new File(getExternalFilesDir(null), FILE_NAME);
                /*File root = android.os.Environment.getExternalStorageDirectory();
                File file = new File(root.getAbsolutePath() +"/"+ FILE_NAME);*/

                FileOutputStream outputStream = null;

                try {
                    outputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                    for (int i = 0; i < list.size(); i++) {
                        outputStream.write(list.get(i).getBytes());
                    }
                    outputStream.close();
                    Toast.makeText(MainActivity.this, "saved \n " + file, Toast.LENGTH_SHORT).show();
                    Log.e("---File---", "Path: " + "saved \n " + file);
                    return;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });


    }

    private void deviceIMEI() {
        String imeiNo;
        imeiNo = Build.SERIAL;
        binding.tvImei.setText(imeiNo);
    }

    private void deviceSDKVERSION() {
        String androidVersion = Build.VERSION.RELEASE;
        binding.tvVersion.setText("Android " + androidVersion);
    }

    private void deviceName() {
        String deviceName = Build.MODEL;
        binding.tvDeviceName.setText(deviceName);
    }

    private void blutoothStatus() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
            binding.tvBlutooth.setText("Blutooth Not Supported!");
        else {
            if (bluetoothAdapter.isEnabled())
                binding.tvBlutooth.setText("Blutooth ON!");
            else if (!bluetoothAdapter.isEnabled())
                binding.tvBlutooth.setText("Blutooth OFF!");
        }
    }

    private void deviceTime() {
        String dateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        binding.tvTime.setText(dateTime);
    }

    private void deviceIP() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(manager.getConnectionInfo().getIpAddress());
        binding.tvIp.setText(ip);
        Log.e("---IP Address---", "Mobile IP: " + ip);
    }
}