package com.example.mymob_horoscope.Fragment;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mymob_horoscope.R;
import com.jaredrummler.android.device.DeviceName;

import java.lang.reflect.Field;

public class SystemFragment extends Fragment {

    TextView tvWifiStatus, tvIPAdd, tvManufacturer, tvRelease, tvBrand, tvModelName, tvModel, tvApi, tvCodeName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_system, container, false);
        main(v);
        return v;

    }

    private void main(View v) {

        tvWifiStatus = v.findViewById(R.id.tvWifiStatus);
        tvIPAdd = v.findViewById(R.id.tvIPAdd);
        tvManufacturer = v.findViewById(R.id.tvManufacturer);
        tvRelease = v.findViewById(R.id.tvRelease);
        tvBrand = v.findViewById(R.id.tvBrand);
        tvModelName = v.findViewById(R.id.tvModelName);
        tvModel = v.findViewById(R.id.tvModel);
        tvApi = v.findViewById(R.id.tvApi);
        tvCodeName = v.findViewById(R.id.tvCodeName);


        WifiManager manager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            tvWifiStatus.setText("Wi-Fi Enabled");
        } else {
            tvWifiStatus.setText("Wi-Fi Not Connected!");
        }

        String ip = Formatter.formatIpAddress(manager.getConnectionInfo().getIpAddress());
        tvIPAdd.setText(ip);
        Log.e("---IP Address---", "Mobile IP: " + ip);

        String manuFacturer = Build.MANUFACTURER;
        tvManufacturer.setText(manuFacturer);

        String release = Build.VERSION.RELEASE;
        tvRelease.setText(release);

        String brand = Build.BRAND;
        tvBrand.setText(brand);

        String modelName = DeviceName.getDeviceName();
        tvModelName.setText(modelName);

        String model = Build.MODEL;
        tvModel.setText(model);

        String api = String.valueOf(Build.VERSION.SDK_INT);
        tvApi.setText(api);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for(Field field:fields){
            String codeName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(fieldValue == Build.VERSION.SDK_INT){
                tvCodeName.setText(codeName);
            }
        }


    }
}