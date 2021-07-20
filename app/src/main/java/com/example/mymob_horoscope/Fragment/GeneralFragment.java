package com.example.mymob_horoscope.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DeviceAdminService;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.pdf.PdfDocument;
import android.hardware.SensorManager;
import android.hardware.fingerprint.FingerprintManager;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.mymob_horoscope.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class GeneralFragment extends Fragment {

    private BluetoothAdapter bluetoothAdapter;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    Context context;
    String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mymobpdf/";
    private static final String FILE_NAME = "myMobHoroscope.txt";


    TextView tvResolution,tvRam,tvPlateform, tvMagnetometer,tvIp, tvTime, tvBlutooth, tvDeviceName, tvVersion, tvKernel,tvImei;

    Button btnSubmit, btnFile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_general, container, false);
        main(v);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void main(View v) {
        btnSubmit = v.findViewById(R.id.btnSubmit);
        btnFile = v.findViewById(R.id.btnFile);
        tvResolution = v.findViewById(R.id.tvResolution);
        tvRam = v.findViewById(R.id.tvRam);
        tvKernel = v.findViewById(R.id.tvKernel);
        tvIp = v.findViewById(R.id.tvIp);
        tvTime = v.findViewById(R.id.tvTime);
        tvMagnetometer = v.findViewById(R.id.tvMagnetometer);
        tvBlutooth = v.findViewById(R.id.tvBlutooth);
        tvDeviceName = v.findViewById(R.id.tvDeviceName);
        tvVersion = v.findViewById(R.id.tvVersion);
        tvPlateform = v.findViewById(R.id.tvPlateform);
        tvImei = v.findViewById(R.id.tvImei);

        deviceResolution();
        deviceRam();
        deviceIP();
        deviceTime();
        blutoothStatus();
        deviceName();
        deviceSDKVERSION();
        deviceIMEI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            devicePlateform();
        }
        deviceMagnetometer();
        deviceKernel();

        btnFile.setOnClickListener(view -> {
            checkSelfPermission();
        });
    }

    private void deviceMagnetometer() {
        String devMagnetometerName;
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        devMagnetometerName = String.valueOf(sensorManager.getSensorList(1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                devMagnetometerName = String.valueOf(sensorManager.getSensorList(2).get(0).getVendor() + "\t| " +
                        sensorManager.getSensorList(2).get(0).getHighestDirectReportRateLevel() + "\t| " +
                        sensorManager.getSensorList(2).get(0).getFifoMaxEventCount() + "\t| " +
                        sensorManager.getSensorList(2).get(0).getFifoReservedEventCount() + "\t| " +
                        sensorManager.getSensorList(2).get(0).getId() + "\t| " +
                        sensorManager.getSensorList(2).get(0).getPower() + "\t| " +
                        sensorManager.getSensorList(2).get(0).getMaxDelay() + "\t| " +
                        sensorManager.getSensorList(2).get(0).getMinDelay() + "\t| " +
                        sensorManager.getSensorList(2).get(0).getReportingMode() + "\t| " +
                        sensorManager.getSensorList(2).get(0).getResolution() + "\t| " +
                        sensorManager.getSensorList(2).get(0).getVersion() ); // Magnetometer Full Detail
            }
        }
        tvMagnetometer.setText(devMagnetometerName);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void devicePlateform() {
        String devPlateform;
        devPlateform = Build.DEVICE; // Device and Product
        devPlateform = Build.BOARD; // CPU
        devPlateform = Build.CPU_ABI; // ABI
        devPlateform = Build.TYPE; // Build Type
        devPlateform = String.valueOf(getActivity().getPackageManager().hasSystemFeature("android.hardware.touchscreen"));// TouchScreen

        DisplayMetrics displayMetrics = (DisplayMetrics) getActivity().getSystemService(TouchDelegate.class.getName());
        devPlateform = String.valueOf(displayMetrics);// Touchscreen Name
        FingerprintManager fingerprintManager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
        devPlateform = fingerprintManager.toString();// fingerprint
        devPlateform = Build.HARDWARE;// Plateform
        Log.e("------", "devicePlateform: " + devPlateform );
        tvPlateform.setText(devPlateform);
    }

    private void deviceKernel() {
        String kernel = System.getProperty("os.version");
        tvKernel.setText(kernel);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void deviceRam() {
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        long totalRam = memoryInfo.totalMem / ( 1024 * 1024 )  / 1000 ;
        tvRam.setText(String.valueOf(totalRam + 1) + " GB");

//        long totalRam = memoryInfo.totalMem / ( 1024 * 1024 )  / 1000;
//        long availRam = memoryInfo.availMem;
//        long usedMemory = totalRam - availRam;
//        float percentage = (((float) (availRam  / totalRam)) * 100);
//        tvRam.setText(percentage + " %");

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
    private void deviceResolution() {
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        display.getSize(size);
        int hight = size.x;
        int width = size.y;

        tvResolution.setText(hight + " x " + width);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
        } else {

            ArrayList<String> list = new ArrayList<>();
            list.add("!! MyMob_Hororscope !!");
            list.add("Device: " + tvDeviceName.getText().toString());
            list.add("Blutooth: " + tvBlutooth.getText().toString());
            list.add("Date: " + tvTime.getText().toString());
            list.add("IP: " + tvIp.getText().toString());
            list.add("IMEI: " + tvImei.getText().toString());
            Log.e("List", "onClick: " + list);

            createPDF(list);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPDF(ArrayList<String> list) {
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
            Toast.makeText(getActivity(), "PDF Created!", Toast.LENGTH_SHORT).show();
            Log.e("---File PAth---", "createwPDF: Path:" + filePath);

        } catch (IOException e) {
            Log.e("---PDF---", "error: " + e.toString());
            Toast.makeText(getActivity(), "Something went wrong for PDF creation!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    private void deviceIMEI() {
        String imeiNo;
        imeiNo = Build.SERIAL;
        tvImei.setText(imeiNo);
    }

    private void deviceSDKVERSION() {
        String androidVersion = Build.VERSION.RELEASE;
        tvVersion.setText("Android " + androidVersion);
    }

    private void deviceName() {
        String deviceName = Build.MODEL;
        tvDeviceName.setText(deviceName);
    }

    private void blutoothStatus() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
            tvBlutooth.setText("Blutooth Not Supported!");
        else {
            if (bluetoothAdapter.isEnabled())
                tvBlutooth.setText("Blutooth ON");
            else if (!bluetoothAdapter.isEnabled())
                tvBlutooth.setText("Blutooth OFF");
        }
    }

    private void deviceTime() {
        String dateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        tvTime.setText(dateTime);
    }

    private void deviceIP() {
        WifiManager manager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(manager.getConnectionInfo().getIpAddress());
        tvIp.setText(ip);
        Log.e("---IP Address---", "Mobile IP: " + ip);
    }
}