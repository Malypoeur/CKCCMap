package term3.fe.rupp.ckccmap;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient = null;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Maps:", "Activity OnCreated");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // for retrieving last known location
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

        // for retrieving Google direction
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://maps.googleapis.com/maps/api/directions/json?origin=11.566872,104.890467&destination=11.559841,104.910407&key=AIzaSyDZX40EuY1U9PsnHdtJb60AamHBKPeoltM";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley: ", "response success");
                Log.d("Volley: ", response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray geo_waypoints = jsonObject.getJSONArray("geocoded_waypoints");
                    JSONObject obj_temp = (JSONObject) geo_waypoints.get(0);
                    String place_id = obj_temp.getString("place_id");
                    Log.d("JSONObject: ", place_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley: ", "response Error");
            }
        });

        queue.add(stringRequest);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d("Maps:", "GoogleMap onMapReady");

        mMap = googleMap;

        locationListener = new LocationListener() {
            @Override
            public void onLocationUpdated(LatLng lastLocation) {

                Log.d("Maps:", "Receiver: onLocationUpdated");

                if(lastLocation !=null) {
                    mMap.addMarker(new MarkerOptions().position(lastLocation).title("CKCC Cooperation Center..."));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 14.0f));
                }

            }
        };
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Maps:", "GoogleAPIClient onConnected");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mLastLocation !=null){
            // Add a marker in Sydney and move the camera
            LatLng lastLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            // trigger onMapReady that we now have the last updatd location, so please add icon to the Map
            Log.d("Maps:", "Sender: onLocationUpdated");
            locationListener.onLocationUpdated(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
