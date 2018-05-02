package com.umbcapp.gaurk.snapeve;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

public class DropPinMapsActivity extends FragmentActivity implements OnMapReadyCallback {

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
//    private HeatmapTileProvider mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.maps_drop_pin_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        cood = new Cood("0","0");

        ctr_pin = (ImageView) findViewById(R.id.center_pin);
        selected_latlong_text_view = (TextView) findViewById(R.id.selected_latlong);
        selected_latlong_confirm_btn = (TextView) findViewById(R.id.selected_latlong_confirm_btn);

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

    //google map methods
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng centennial_High_School = new LatLng(39.294541, -76.613114);
//        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.test_bmp);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centennial_High_School, 14.0f));

        center_latlong = mMap.getCameraPosition().target;
        float zoom = mMap.getCameraPosition().zoom;
        System.out.println("zoom : " + zoom);

        LatLng police_stn_1 = new LatLng(39.322534, -76.602527);

//      mMap.addMarker(new MarkerOptions().position(police_stn_1).title("Police station, 21334").icon(BitmapDescriptorFactory.fromResource(R.drawable.police_station_32)));

        //test method. used for debugging
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                System.out.println("LAT LONG : " + latLng);
                mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        //test method. used for debugging
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

    private Cood formatLatLong(LatLng center_latlong) {

        String latLong = String.valueOf(center_latlong);

        cood = new Cood(latLong.substring(latLong.indexOf('(') + 1, latLong.indexOf(',')),
                latLong.substring(latLong.indexOf(',') + 1, latLong.indexOf(')')));

//        cood.setLat(latLong.substring(latLong.indexOf('('), latLong.indexOf(',')));

        return cood;
    }

    @Override
    public void onBackPressed() {

    }
}
