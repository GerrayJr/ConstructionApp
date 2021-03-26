package com.gerray.fmsystem.LesseeModule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gerray.fmsystem.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindow implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    // private TextView link_tv;

    @SuppressLint("InflateParams")
    public InfoWindow(Context mContext) {
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.activity_info_window,null);

    }

    private void rendowWindowText(Marker marker, View view)
    {
        String title = marker.getTitle();
        String type = marker.getSnippet();
        TextView tvTitle = view.findViewById(R.id.title);
//        TextView tvType = view.findViewById(R.id.type);

        if(!title.equals(""))
        {
            tvTitle.setText(title);
        }

        if (!type.equals(""))
        {
//            tvType.setText(type);
        }
    }


    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;
    }
}
