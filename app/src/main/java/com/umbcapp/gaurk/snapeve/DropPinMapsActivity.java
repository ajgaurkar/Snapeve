package com.umbcapp.gaurk.snapeve;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.umbcapp.gaurk.snapeve.Controllers.Cood;

import java.util.ArrayList;

public class DropPinMapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private View circle_view;
    private ImageView ctr_pin;
    private ImageView options_image_view;
    private RelativeLayout heat_map_layout;
    private RelativeLayout selection_confirm_layout;
    private Button select_cood_btn;
    private Button cancel_cood_btn;
    private TextView selected_latlong_text_view;
    private FloatingActionButton filterFab;
    private FloatingActionButton add_item_Fab;
    private LatLng center_latlong;
    private TextView selected_latlong_confirm_btn;
    private Cood cood;
    private LocationManager locationManager;
    private ProgressBar selected_latlong_progressbar;
//    private HeatmapTileProvider mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        center_latlong = new LatLng(0.0, 0.0);

        setContentView(R.layout.maps_drop_pin_activity);
        getCurrentLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        cood = new Cood("0", "0");

        ctr_pin = (ImageView) findViewById(R.id.center_pin);
        selected_latlong_text_view = (TextView) findViewById(R.id.selected_latlong);
        selected_latlong_progressbar = (ProgressBar) findViewById(R.id.selected_latlong_progressbar);
        selected_latlong_confirm_btn = (TextView) findViewById(R.id.selected_latlong_confirm_btn);

        selected_latlong_progressbar.setVisibility(View.VISIBLE);
        selected_latlong_text_view.setText("Updating location...");

        selected_latlong_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("selected_latlong_confirm_btn PRESSED");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Lat", cood.getLat());
                returnIntent.putExtra("Lng", cood.getLng());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private void getCurrentLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("location :" + location);
        selected_latlong_text_view.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        selected_latlong_progressbar.setVisibility(View.GONE);

        center_latlong = new LatLng(location.getLatitude(), location.getLongitude());
        System.out.println("current_latlong :" + center_latlong);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center_latlong, 15.5f));

        center_latlong = mMap.getCameraPosition().target;
        float zoom = mMap.getCameraPosition().zoom;
        System.out.println("zoom : " + zoom);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                System.out.println("CAMERA MOVE ");
                float zoom = mMap.getCameraPosition().zoom;
                System.out.println("zoom : " + zoom);

                center_latlong = mMap.getCameraPosition().target;
                System.out.println("center_latlong : " + center_latlong);
                cood = formatLatLong(center_latlong);
                selected_latlong_text_view.setText(cood.getLat() + " , " + cood.getLng());

                System.out.println("COOD " + cood.getLat() + "  --  " + cood.getLng());
            }
        });

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        selected_latlong_progressbar.setVisibility(View.GONE);

    }

    @Override
    public void onProviderEnabled(String provider) {
        selected_latlong_progressbar.setVisibility(View.GONE);

    }

    @Override
    public void onProviderDisabled(String provider) {
        System.out.println("onProviderDisabled ");

        selected_latlong_text_view.setText("Please Enable GPS and Internet");
        selected_latlong_progressbar.setVisibility(View.GONE);

    }

    //google map methods
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_latlong, 14.0f));
//
//        center_latlong = mMap.getCameraPosition().target;
//        float zoom = mMap.getCameraPosition().zoom;
//        System.out.println("zoom : " + zoom);
//
//        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
//            @Override
//            public void onCameraMove() {
//                System.out.println("CAMERA MOVE ");
//                float zoom = mMap.getCameraPosition().zoom;
//                System.out.println("zoom : " + zoom);
//
//                center_latlong = mMap.getCameraPosition().target;
//                System.out.println("center_latlong : " + center_latlong);
//                cood = formatLatLong(center_latlong);
//                selected_latlong_text_view.setText(cood.getLat() + " , " + cood.getLng());
//
//                System.out.println("COOD " + cood.getLat() + "  --  " + cood.getLng());
//            }
//        });

    }

    private Cood formatLatLong(LatLng center_latlong) {
        String latLong = String.valueOf(center_latlong);
        cood = new Cood(latLong.substring(latLong.indexOf('(') + 1, latLong.indexOf(',')),
                latLong.substring(latLong.indexOf(',') + 1, latLong.indexOf(')')));
        return cood;
    }

    @Override
    public void onBackPressed() {

    }
}
