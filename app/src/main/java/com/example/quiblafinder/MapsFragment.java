package com.example.quiblafinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import static android.content.Context.SENSOR_SERVICE;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationButtonClickListener, SensorEventListener {




    View view;
    private GoogleMap mMap;
    LocationManager locationManager;
    Location location;
    LatLng userLocation,kabeLocation;
    Geocoder geocoder;
    List<Address> userAdressList;
    LatLng lastLocation;
    double qiblaDegree;
    double magnitude;
    private static final int REQ_PERMISSION = 0;
    Marker kabe;
    float [] gravity=new float[3] ;
    float [] geometrica = new float[3];
    float azizure = 0f;
    float currentAzizumtc = 0f;
    private SensorManager sensorManager;
    TextView distanceText,degreeText,magneticField;
    Marker currentArrow;
    PolylineOptions polylineOptions;
    float orientation [];
    Animation animation;
    boolean success;
    int bas,son;
    float Rx [];
    float Ix [];
    FloatingActionButton myFab;
    ConstraintLayout constraintLayout;
    ImageButton roadMap,sataliteMap,hybridMap,qiblaMarker,myGpsBtn;

    public static DecimalFormat DECIMAL_FORMATTER;

    final float alpha =0.97f;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment,container,false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);


        Instance();

        return view;
    }

    private void Instance() {
        distanceText = view.findViewById(R.id.distanceText);
        degreeText = view.findViewById(R.id.qiblaDegree);
        myFab= view.findViewById(R.id.mapType);
        roadMap = view.findViewById(R.id.roadMap);
        sataliteMap = view.findViewById(R.id.satelliteMap);
        hybridMap = view.findViewById(R.id.hybridMap);
        qiblaMarker = view.findViewById(R.id.qiblaMarker);
        magneticField = view.findViewById(R.id.magneticField);
        myGpsBtn = view.findViewById(R.id.myLocationBtn);
        constraintLayout = view.findViewById(R.id.map_type_selection);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("#.000", symbols);


        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (constraintLayout.getVisibility()==View.VISIBLE){
                    constraintLayout.setVisibility(View.INVISIBLE);
                }
                else
                    constraintLayout.setVisibility(View.VISIBLE);
            }
        });

        sataliteMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                constraintLayout.setVisibility(View.INVISIBLE);

            }
        });

        hybridMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                constraintLayout.setVisibility(View.INVISIBLE);
            }
        });

        roadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                //ROAD MAP DİYE BİR MAP BULAMADIM

                constraintLayout.setVisibility(View.INVISIBLE);
            }
        });
        qiblaMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kabeLocation, 15));
            }
        });
        myGpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lastLocation != null){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 15));
                }
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {


        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        kabeLocation = new LatLng(21.4224779,39.8240889);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.isMyLocationEnabled();

        if (!checkPermission()) {
            askPermission();

        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 100, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                lastLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 15));
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    userAdressList = geocoder.getFromLocation(lastLocation.latitude, lastLocation.longitude, 1);
                    if (userAdressList.size() > 0 && userAdressList != null) {

                        distanceText.setText(distanceText.getText()+""+DistanceCalculator(location,kabeLocation)+" KM");
                        AddMarker(location);
                        DrawLine(location);
                        CalculateQibla(location.getLatitude(),location.getLongitude());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Object DistanceCalculator(Location location, LatLng kabeLocation) {
        double theta = location.getLatitude() - kabeLocation.latitude;
        double dist = Math.sin(Math.toRadians(location.getLatitude())) * Math.sin(Math.toRadians(kabeLocation.latitude))
                + Math.cos(Math.toRadians(location.getLatitude())) *
                Math.cos(Math.toRadians(kabeLocation.latitude)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return ((int)dist);
        }
    public  void DrawLine(Location location){
        polylineOptions = new PolylineOptions();
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        polylineOptions.add(new LatLng(latLng.latitude,latLng.longitude))
                .add(new LatLng(kabeLocation.latitude,kabeLocation.longitude));
        polylineOptions.width(15);
        polylineOptions.geodesic(true);
        mMap.addPolyline(polylineOptions);}

    private void AddMarker(Location location) {
        mMap.clear();
        currentArrow = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(),location.getLongitude()))
                .anchor(0.5f,0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green)));
        kabe = mMap.addMarker(new MarkerOptions()
                .position(kabeLocation)
                .anchor(0.5f,0.5f));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (requestCode == 1) {

            }
        }
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    if (checkPermission()) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            if (lastLocation != null) {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 100, this);
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 15));
                                AddMarker(location);
                                DrawLine(location);
                            }

                        }
                    }

                } else {
                    askPermission();

                }
                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(Location location) {
        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        lastLocation = userLocation;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        AddMarker(location);
        CalculateQibla(location.getLatitude(),location.getLongitude());
        DrawLine(location);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMyLocationButtonClick() {

        if (checkPermission()){
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(),location.getLongitude()))
                    .zoom(20).build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            CalculateQibla(userLocation.latitude,userLocation.longitude);
            AddMarker(location);
            DrawLine(location);
        }







        return false;
    }

    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){

            if (sensorManager != null){

                if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){

                    gravity[0] = alpha * gravity[0] +(1-alpha)*event.values[0];
                    gravity[1] = alpha * gravity[1] +(1-alpha)* event.values[1];
                    gravity[2] = alpha * gravity[2] +(1-alpha)*event.values[2];

                }

                if (event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){

                    geometrica[0] = alpha * geometrica[0] +(1-alpha)*event.values[0];
                    geometrica[1] = alpha * geometrica[1] +(1-alpha)* event.values[1];
                    geometrica[2] = alpha * geometrica[2] +(1-alpha)*event.values[2];

                    magnitude= Math.sqrt((event.values[0] * event.values[0]) + ( event.values[1] *  event.values[1]) + (event.values[2] * event.values[2]));
                }
                if (magnitude >80 && magnitude <170){
                    magneticField.setText("Manyetik Alan Artmaya Başladı ! " +DECIMAL_FORMATTER.format(magnitude) + " \u00B5Tesla");
                    magneticField.setVisibility(View.VISIBLE);
                    magneticField.setBackgroundColor(getResources().getColor(R.color.orrange));
                }
                else if (magnitude >=170){
                    magneticField.setVisibility(View.VISIBLE);
                    magneticField.setText("Manyetik Alan Aşırı Fazla !"+DECIMAL_FORMATTER.format(magnitude) + " \u00B5Tesla");
                    magneticField.setBackgroundColor(getResources().getColor(R.color.red));
                }
                else
                    magneticField.setVisibility(View.INVISIBLE);
                 Rx= new float[9];
                 Ix = new float[9];

                 success = SensorManager.getRotationMatrix(Rx,Ix, gravity,geometrica );
                if (success){
                    orientation= new float[3];
                    SensorManager.getOrientation(Rx,orientation);
                    azizure = (float) Math.toDegrees(orientation[0]);
                    azizure = (azizure+360)%360;
                    animation = new RotateAnimation(-currentAzizumtc,-azizure,Animation.RELATIVE_TO_SELF,0.5f,
                            Animation.RELATIVE_TO_SELF,0.5f);
                    currentAzizumtc = azizure;

                    if (currentArrow!=null){
                        currentArrow.setRotation((azizure));
                         bas= (int) azizure;
                         son= (int) qiblaDegree;
                        if (bas-son > -10 && bas-son<10){
                            polylineOptions.color(Color.GREEN);
                        }
                        else{
                            polylineOptions.color(Color.BLACK);
                        }
                        polylineOptions.geodesic(true);
                        animation.setDuration(100);
                        animation.setRepeatCount(0);

                        animation.setFillAfter(true);
                        animation.start();
                        mMap.addPolyline(polylineOptions);
                    }
                    geometrica = new float[3];
                    gravity = new float[3];

                }
            }


        }
    }

    public void CalculateQibla(double latitude, double longitude){

        qiblaDegree =
                GreatCircleBearing.initial(latitude,longitude,kabeLocation.latitude,kabeLocation.longitude);
        degreeText.setText("Kıble Uzaklığı: "+(int)qiblaDegree +"\u00b0");
        CompassFragment compassFragment = new CompassFragment();
        compassFragment.qiblaDegree = this.qiblaDegree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




}