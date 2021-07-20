package com.example.mymob_horoscope.Fragment;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymob_horoscope.R;

public class SystemFragment extends Fragment {

    TextView tvWifiStatus,tvIPAdd;
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

        tvWifiStatus =  v.findViewById(R.id.tvWifiStatus);
        tvIPAdd = v.findViewById(R.id.tvIPAdd);


        WifiManager manager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(manager.isWifiEnabled()){
            tvWifiStatus.setText("Wi-Fi Enabled");
        }else {
            tvWifiStatus.setText("Wi-Fi Not Connected!");
        }

        String ip = Formatter.formatIpAddress(manager.getConnectionInfo().getIpAddress());
        tvIPAdd.setText(ip);
        Log.e("---IP Address---", "Mobile IP: " + ip);
    }
}