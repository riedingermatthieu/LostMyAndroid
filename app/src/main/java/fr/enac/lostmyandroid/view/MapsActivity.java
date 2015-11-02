package fr.enac.lostmyandroid.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import fr.enac.lostmyandroid.R;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Location location;
    private float latitude;
    private float longitude;

    public static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        longitude = savedInstanceState.getFloat("longitude");
        latitude = savedInstanceState.getFloat("latitude");

        setContentView(R.layout.activity_maps);
        buildGoogleApiClient();
        setUpMapIfNeeded();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
                setUpMap();

        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        showLocation();
    }


    //TODO Get position of the device when receiving an SMS - Poke Matthieu

    /**
     * Look for the current device's location and returns it
     *
     * @return Location
     */
    public Location getMyLocation() {
        Context context = getApplicationContext();

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        return lastKnownLocation;
    }

    /**
     * Add a marker on the map
     **/
    public void showLocation() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,
                longitude)).title("Position"));
        //Build camera position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude,
                        longitude))
                .zoom(14).build();
        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        //if (mLastLocation != null) {
        //FIXME récuparation des coordonnées
        //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
        //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        //}
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("ErreurAuth", connectionResult.getErrorMessage());
    }


}
