package com.aleskovacic.pact.activities;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aleskovacic.pact.R;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

class MapMessageAdapter implements InfoWindowAdapter  {

    private View message_info_window=null;
    private LayoutInflater inflater=null;

    MapMessageAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }
    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (message_info_window == null) {
            message_info_window=inflater.inflate(R.layout.message_item_info_window, null);
        }

        TextView mText=(TextView)message_info_window.findViewById(R.id.info_window_mText);

        mText.setText(marker.getTitle());



        TextView lastname=(TextView)message_info_window.findViewById(R.id.info_window_lastname);

        lastname.setText(marker.getSnippet());
        return(message_info_window);
    }
}