package com.example.quiblafinder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static android.content.Context.SENSOR_SERVICE;

public class CompassFragment extends Fragment implements SensorEventListener {

    View view;
    SensorManager sensorManager;
    RelativeLayout relativeLayout;
    ImageView compassImage;
    double qiblaDegree = 145;
    float [] gravity = new float[3];
    float [] geometrica = new float[3];
    float azizure = 0f;
    boolean success;
    float currentAzizumtc = 0f;
    final float alpha =0.97f;
    float Rx [];
    float Ix [];
    float orientation [];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view  = inflater.inflate(R.layout.compass_fragment,container,false);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        compassImage = view.findViewById(R.id.compassImage);
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);


        RotateAnimation ra = new RotateAnimation(
                compassImage.getRotation(),
                (float) qiblaDegree, // a static vaule
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setDuration(500);
        ra.setRepeatCount(0);
        compassImage.setRotation((float) qiblaDegree);
        ra.setFillAfter(true);
        compassImage.startAnimation(ra);
        relativeLayout.setRotation((float) qiblaDegree);

        return view;
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
    public void onSensorChanged(SensorEvent event) {

        if (sensorManager != null){
            synchronized (this){
                if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                    gravity[0] = alpha * gravity[0] +(1-alpha)*event.values[0];
                    gravity[1] = alpha * gravity[1] +(1-alpha)*event.values[1];
                    gravity[2] = alpha * gravity[2] +(1-alpha)*event.values[2];

                }
                if (event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                    geometrica[0] = alpha * geometrica[0] +(1-alpha)*event.values[0];
                    geometrica[1] = alpha * geometrica[1] +(1-alpha)*event.values[1];
                    geometrica[2] = alpha * geometrica[2] +(1-alpha)*event.values[2];
                }
                 Rx= new float[9];
                Ix= new float[9];

                 success = SensorManager.getRotationMatrix(Rx,Ix, gravity,geometrica );
                if (success){
                     orientation = new float[3];
                    SensorManager.getOrientation(Rx,orientation);
                    azizure = (float) Math.toDegrees(orientation[0]);
                    azizure = (azizure+360)%360;
                    Animation animation = new RotateAnimation(-currentAzizumtc,-azizure,Animation.RELATIVE_TO_SELF,0.5f,
                            Animation.RELATIVE_TO_SELF,0.5f);
                    currentAzizumtc= azizure;


                    animation.setDuration(300);
                    animation.setRepeatCount(0);
                    animation.setFillAfter(true);
                    relativeLayout.startAnimation(animation);


                }
        }}
     }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
