package term3.fe.rupp.ckccmap;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
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
