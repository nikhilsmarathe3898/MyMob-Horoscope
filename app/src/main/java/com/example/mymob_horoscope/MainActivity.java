package com.example.mymob_horoscope;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mymob_horoscope.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BluetoothAdapter bluetoothAdapter;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    Context context;

    String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mymobpdf/";
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
                checkPermission();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        } else {

            ArrayList<String> list = new ArrayList<>();
            list.add("!! MyMob_Hororscope !!");
            list.add("Device: " + binding.tvDeviceName.getText().toString());
            list.add("Blutooth: " + binding.tvBlutooth.getText().toString());
            list.add("Date: " + binding.tvTime.getText().toString());
            list.add("IP: " + binding.tvIp.getText().toString());
            list.add("IMEI: " + binding.tvImei.getText().toString());
            Log.e("List", "onClick: " + list);

            createwPDF(list);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createwPDF(ArrayList<String> list) {
        // For PDF File Generation.
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.
                PageInfo.Builder(300, 600, 1).create(); // creating page no.


        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        for (int i = 0; i < list.size(); i++) {
            canvas.drawText(list.get(0), 80, 45, paint); // Write text into PDF
            canvas.drawLine(40, 70, 250, 70, paint); // for drawing a line.
            canvas.drawText(list.get(1), 50, 85, paint);
            canvas.drawLine(40, 90, 250, 90, paint);
            canvas.drawText(list.get(2), 50, 105, paint);
            canvas.drawLine(40, 110, 250, 110, paint);
            canvas.drawText(list.get(3), 50, 125, paint);
            canvas.drawLine(40, 130, 250, 130, paint);
            canvas.drawText(list.get(4), 50, 145, paint);
            canvas.drawLine(40, 150, 250, 150, paint);
            canvas.drawText(list.get(5), 50, 165, paint);
            canvas.drawLine(40, 170, 250, 170, paint);
        }
        pdfDocument.finishPage(page);

        File file = new File(directoryPath);

        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPDF = directoryPath + "mymob.pdf";
        File filePath = new File(targetPDF);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            pdfDocument.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "PDF Created!", Toast.LENGTH_SHORT).show();
            Log.e("---File PAth---", "createwPDF: Path:" + filePath);

        } catch (IOException e) {
            Log.e("---PDF---", "error: " + e.toString());
            Toast.makeText(this, "Something went wrong for PDF creation!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        pdfDocument.close();
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
                binding.tvBlutooth.setText("Blutooth ON");
            else if (!bluetoothAdapter.isEnabled())
                binding.tvBlutooth.setText("Blutooth OFF");
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