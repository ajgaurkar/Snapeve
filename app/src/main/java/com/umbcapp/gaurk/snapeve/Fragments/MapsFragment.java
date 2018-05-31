package com.umbcapp.gaurk.snapeve.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.umbcapp.gaurk.snapeve.Adapters.Dash_Event_ListAdapter;
import com.umbcapp.gaurk.snapeve.Controllers.Event_dash_list_obj;
import com.umbcapp.gaurk.snapeve.MainActivity;
import com.umbcapp.gaurk.snapeve.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MapsFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private ArrayList<Event_dash_list_obj> event_main_list;
    private CardView maps_loading_events_cardview;
    private long sessionCounter;
    private FloatingActionButton maps_frag_filter_fab;
    private CheckBox filter_dialog_event_time_upcoming_chk_box;
    private CheckBox filter_dialog_event_time_ongoing_chk_box;
    private CheckBox filter_dialog_event_time_completed_chk_box;
    private CheckBox filter_dialog_event_type_incident_chk_box;
    private CheckBox filter_dialog_event_type_event_chk_box;
    int staus_completed = 1;
    int staus_ongoing = 1;
    int staus_upcoming = 1;
    int event_type_event = 1;
    int event_type_incident = 1;
    int event_dt_all = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionCounter = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String text = formatter.format(new Date(sessionCounter));
        System.out.println("Start @ sessionCounter : " + text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.maps_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        maps_loading_events_cardview = (CardView) rootView.findViewById(R.id.maps_loading_events_cardview);
        maps_frag_filter_fab = (FloatingActionButton) rootView.findViewById(R.id.maps_frag_filter_fab);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        getMapEvents();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(39.255613039, -76.710975076);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15.5f).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        maps_frag_filter_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        return rootView;
    }

    private void showFilterDialog() {
        final Dialog alertDialog = new Dialog(getActivity());
        LayoutInflater flater = getActivity().getLayoutInflater();
        final ViewGroup nullParent = null;
        View view = flater.inflate(R.layout.maps_filter_dialog, null);

        filter_dialog_event_type_event_chk_box = (CheckBox) view.findViewById(R.id.filter_dialog_event_type_event_chk_box);
        filter_dialog_event_type_incident_chk_box = (CheckBox) view.findViewById(R.id.filter_dialog_event_type_incident_chk_box);
        filter_dialog_event_time_completed_chk_box = (CheckBox) view.findViewById(R.id.filter_dialog_event_time_completed_chk_box);
        filter_dialog_event_time_ongoing_chk_box = (CheckBox) view.findViewById(R.id.filter_dialog_event_time_ongoing_chk_box);
        filter_dialog_event_time_upcoming_chk_box = (CheckBox) view.findViewById(R.id.filter_dialog_event_time_upcoming_chk_box);

        TextView filter_dialog_cancel_btn_text_view = (TextView) view.findViewById(R.id.filter_dialog_cancel_btn_text_view);
        TextView filter_dialog_set_btn_text_view = (TextView) view.findViewById(R.id.filter_dialog_set_btn_text_view);
        final TextView filter_dialog_error_textview = (TextView) view.findViewById(R.id.filter_dialog_error_textview);
        filter_dialog_error_textview.setVisibility(View.INVISIBLE);

        alertDialog.setContentView(view);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);

        filter_dialog_cancel_btn_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        filter_dialog_set_btn_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateCheckBoxes()) {
                    getCheckedStatus();
                    alertDialog.dismiss();
                    getMapEvents();
                    filter_dialog_error_textview.setVisibility(View.INVISIBLE);
                } else {
                    filter_dialog_error_textview.setVisibility(View.VISIBLE);
                }

            }
        });

        alertDialog.show();
    }

    private void getCheckedStatus() {

        //need some condition
        event_dt_all = 1;


        if (filter_dialog_event_time_completed_chk_box.isChecked()) {
            staus_completed = 1;
        } else {
            staus_completed = 0;
        }
        if (filter_dialog_event_time_ongoing_chk_box.isChecked()) {
            staus_ongoing = 1;
        } else {
            staus_ongoing = 0;
        }
        if (filter_dialog_event_time_upcoming_chk_box.isChecked()) {
            staus_upcoming = 1;
        } else {
            staus_upcoming = 0;
        }

        if (filter_dialog_event_type_event_chk_box.isChecked()) {
            event_type_event = 1;
        } else {
            event_type_event = 0;
        }
        if (filter_dialog_event_type_incident_chk_box.isChecked()) {
            event_type_incident = 1;
        } else {
            event_type_incident = 0;
        }

    }

    private Boolean validateCheckBoxes() {
        if (filter_dialog_event_time_completed_chk_box.isChecked() || filter_dialog_event_time_ongoing_chk_box.isChecked() || filter_dialog_event_time_upcoming_chk_box.isChecked()) {
            if (filter_dialog_event_type_event_chk_box.isChecked() || filter_dialog_event_type_incident_chk_box.isChecked()) {

                return true;
            }
        }

        return false;
    }

    private void getMapEvents() {
        JsonObject jsonObjectLoginParameters = new JsonObject();
        jsonObjectLoginParameters.addProperty("studentId", "check123");
        jsonObjectLoginParameters.addProperty("staus_completed", staus_completed);
        jsonObjectLoginParameters.addProperty("staus_ongoing", staus_ongoing);
        jsonObjectLoginParameters.addProperty("staus_upcoming", staus_upcoming);
        jsonObjectLoginParameters.addProperty("event_type_event", event_type_event);
        jsonObjectLoginParameters.addProperty("event_type_incident", event_type_incident);
        jsonObjectLoginParameters.addProperty("event_dt_to", "");
        jsonObjectLoginParameters.addProperty("event_dt_from", "");
        jsonObjectLoginParameters.addProperty("event_dt_all", event_dt_all);

        maps_loading_events_cardview.setVisibility(View.VISIBLE);

        final SettableFuture<JsonElement> resultFuture = SettableFuture.create();
//        ListenableFuture<JsonElement> serviceFilterFuture = mClient.invokeApi("Login_api", jsonObjectLoginParameters);
        ListenableFuture<JsonElement> serviceFilterFuture = MainActivity.mClient.invokeApi("get_event_feeds_api", jsonObjectLoginParameters);

        Futures.addCallback(serviceFilterFuture, new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exception) {
                resultFuture.setException(exception);
                System.out.println(" executeLoginApi exception    " + exception);
                maps_loading_events_cardview.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(JsonElement response) {
                resultFuture.set(response);
                System.out.println(" executeLoginApi success response    " + response);
                maps_loading_events_cardview.setVisibility(View.INVISIBLE);
                poupulateList(response);

            }
        });
    }

    private void poupulateList(JsonElement response) {


        System.out.println(" IN PARSE JASON");

        JsonArray feedsJsonArray = response.getAsJsonArray();

        for (int j = 0; j < feedsJsonArray.size(); j++) {
            JsonObject feeds_list_object = feedsJsonArray.get(j).getAsJsonObject();
            System.out.println(" feeds_list_object  " + feeds_list_object);

            String img_comment = feeds_list_object.get("img_comment").toString();
            String feed_img_url = feeds_list_object.get("img_url").toString();
            String event_lat = feeds_list_object.get("lattitude").toString();
            String event_long = feeds_list_object.get("longitude").toString();
            String event_type = feeds_list_object.get("event_type").toString();
            System.out.println(" img_comment " + img_comment);
            System.out.println(" feed_img_url " + feed_img_url);


            //Remove " from start and end from every string
            img_comment = img_comment.substring(1, img_comment.length() - 1);
            event_lat = event_lat.substring(1, event_lat.length() - 1);
            event_long = event_long.substring(1, event_long.length() - 1);
            event_type = event_type.substring(1, event_type.length() - 1);
            feed_img_url = feed_img_url.substring(1, feed_img_url.length() - 1);

            System.out.println("event_lat : " + event_lat);
            System.out.println("event_long : " + event_long);


            LatLng event_marker = new LatLng(Double.parseDouble(event_lat), Double.parseDouble(event_long));

            if (event_type.equals("0")) {
                googleMap.addMarker(new MarkerOptions().position(event_marker).title(img_comment).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_blue_32)));
            } else {
                googleMap.addMarker(new MarkerOptions().position(event_marker).title(img_comment).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_32_trai)));
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        sessionCounter = System.currentTimeMillis() - sessionCounter;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(sessionCounter);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(sessionCounter);

        System.out.println("MAPS sessionCounter : " + minutes + "m " + seconds + "s");
    }
}