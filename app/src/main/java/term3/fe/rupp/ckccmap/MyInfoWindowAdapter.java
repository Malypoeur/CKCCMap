package term3.fe.rupp.ckccmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by malypoeur on 11/12/16.
 */

public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public MyInfoWindowAdapter(Context context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
        TextView textView = (TextView) view.findViewById(R.id.txtMarkerTitle);
        textView.setText(marker.getTitle());
        return view;
    }
}