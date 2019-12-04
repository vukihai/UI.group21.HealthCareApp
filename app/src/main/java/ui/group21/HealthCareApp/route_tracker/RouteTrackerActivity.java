package ui.group21.HealthCareApp.route_tracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import ui.group21.HealthCareApp.R;

public class RouteTrackerActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        StartRouteTrackerFragment.OnFragmentInteractionListener,
        TrackingFragment.OnFragmentInteractionListener,
        TrackingResultsFragment.OnFragmentInteractionListener {
    private GoogleMap map;
    private Polyline gpsTrack;
    private SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;
    private LatLng lastKnownLatLng;
    private boolean firstLat;
    private boolean foundLocation = false;
    double distance;
    private boolean onGoing = false;
    private TextView gpsStatusTextView;
    private int step = 0;
    private double goal = 10000;
    Fragment startRouteTrackerFragment, trackingFragment, trackingResultsFragment;
    private long startTime = 0;
    private TextView timeTextView, distanceTextView;
    /**
     * Hàm khởi tạo. init các giá trị.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Luyện tập");
//        getActionBar().t.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_tracker);
        firstLat = true;
        distance = 0;

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        gpsStatusTextView = findViewById(R.id.txt_gps_status);
        timeTextView = findViewById(R.id.text_time);
        distanceTextView = findViewById(R.id.text_speed);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (startRouteTrackerFragment == null) {
            startRouteTrackerFragment = new StartRouteTrackerFragment();
        }
        transaction.replace(R.id.fragment_bottom, startRouteTrackerFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        mapFragment.getMapAsync(this);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        // disable button khi chua tim thay vi tri
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        final Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.fragment_bottom);

        if (fragmentInFrame instanceof StartRouteTrackerFragment) {
            finish();
        } else if (fragmentInFrame instanceof TrackingFragment) {
            new AlertDialog.Builder(this)
                    .setTitle("kết thúc")
                    .setMessage("bạn muốn kết thúc hành trình?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            onFragmentInteraction();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            finish();
        }
    }

    /**
     * Hàm load các chuyến đi trong lịch sử.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng defaultCamera = new LatLng(21.028511, 105.804817);
        map.moveCamera(CameraUpdateFactory.newLatLng(defaultCamera));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultCamera, 19));
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.CYAN);
        polylineOptions.width(20);
        gpsTrack = map.addPolyline(polylineOptions);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Hàm update quãng đường khi vị trí thay đổi.
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        Location tmp = new Location("");
        if (!firstLat) {
            if (!onGoing) return;
            tmp.setLongitude(lastKnownLatLng.longitude);
            tmp.setLatitude(lastKnownLatLng.latitude);
        }
        lastKnownLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (firstLat) {
            map.moveCamera(CameraUpdateFactory.newLatLng(lastKnownLatLng));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLatLng, 19));
            firstLat = false;
            ((StartRouteTrackerFragment) startRouteTrackerFragment).setStartButtonEnable(true);
            ((StartRouteTrackerFragment) startRouteTrackerFragment).setGpsStatus("đã tìm thấy vị trí");
//            Button start = (Button) findViewById(R.id.btn_start);
//            start.setEnabled(true);
        } else {
            distance += tmp.distanceTo(location);
//            mTextView.setText(String.valueOf(Math.round(distance*10)/10) + " m");
        }
        updateTrack();
        if(trackingFragment != null) {
            ((TrackingFragment) trackingFragment).updateStatus(distance);
        }
    }

    /**
     * Cấu hình gửi yêu cầu update vị trí.
     */
    @SuppressWarnings("deprecation")
    protected void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    /**
     * tạm dừng update vị trí.
     */
    @SuppressWarnings("deprecation")
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
    }

    /**
     * update đường đi.
     */
    private void updateTrack() {
        if (!onGoing) return;
        List<LatLng> points = gpsTrack.getPoints();
        points.add(lastKnownLatLng);
        gpsTrack.setPoints(points);

    }

    /**
     * Hàm bắt đầu theo dõi.
     */
    public void startTracker() {
        if (!onGoing) onGoing = true;
//        startTime = System.currentTimeMillis();
//        Button start = (Button) findViewById(R.id.button3);
//        Button end = (Button) findViewById(R.id.end_button);
//        start.setEnabled(false);
//        end.setEnabled(true);
        List<LatLng> points = gpsTrack.getPoints();
        points.clear();
        gpsTrack.setPoints(points);
        startTime = System.currentTimeMillis();
//        mTextView.setText("let's go!");
    }

    /**
     * convert tọa độ đường đi gps sang string để lưu vào csdl.
     *
     * @param gpsTrack
     * @return
     */
    private String gpsTrackToString(Polyline gpsTrack) {
        String ret = "";
        List<LatLng> points = gpsTrack.getPoints();
        for (int i = 0; i < points.size(); i++) {
            ret += String.valueOf(points.get(i).latitude);
            ret += " ";
            ret += String.valueOf(points.get(i).longitude);
            ret += " ";
        }
        return ret;
    }

    /**
     * đọc string trong cơ sở dữ liệu và convert sang đường đi.
     *
     * @param s
     * @return
     */
    private Polyline StringToGpsTrack(String s) {
        List<LatLng> points = gpsTrack.getPoints();
        points.clear();
        if (s.length() == 0) {
            gpsTrack.setPoints(points);
            return gpsTrack;
        }

        double lat, log;
        String[] tmp = s.split(" ");
        for (int i = 0; i < tmp.length; i += 2) {
            lat = Double.valueOf(tmp[i]);
            log = Double.valueOf(tmp[i + 1]);
            points.add(new LatLng(lat, log));
        }
        gpsTrack.setPoints(points);
        return gpsTrack;
    }

    /**
     * Hàm dừng theo dõi.
     */
    public void stopTracker() {
        onGoing = false;
//        long finishTime = System.currentTimeMillis();
//        Walking mWalking = new Walking();
//        mWalking.setdistance(String.valueOf(distance));
//        mWalking.setroad(String.valueOf(gpsTrackToString(gpsTrack)));
//        mWalking.settime_begin(String.valueOf(startTime));
//        mWalking.settime_end(String.valueOf(finishTime));
//        WalkingDao.insertRecord(mWalking);
//
//        Button start = (Button) findViewById(R.id.button3);
//        Button end = (Button) findViewById(R.id.end_button);
//        start.setEnabled(true);
//        end.setEnabled(false);
//
//        startTime= 0;
//        gpsTrack.getPoints().clear();
//        distance = 0;
//
//        loadHistory();
    }

    // StartRouteTracker callback
    @Override
    public void onFragmentInteraction(double _goal) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (trackingFragment == null) {
            trackingFragment = new TrackingFragment();
            ((TrackingFragment)trackingFragment).setStartTime(System.currentTimeMillis());
        }
        transaction.replace(R.id.fragment_bottom, trackingFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        goal = _goal;
        startTracker();
    }

    // Tracking callback
    @Override
    public void onFragmentInteraction() {
        stopTracker();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (trackingResultsFragment == null) {
            trackingResultsFragment = new TrackingResultsFragment();
        }
        transaction.replace(R.id.fragment_bottom, trackingResultsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void backButtonOnclick(View v){
        finish();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}