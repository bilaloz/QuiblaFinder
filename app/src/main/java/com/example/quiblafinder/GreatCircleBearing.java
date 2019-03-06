package com.example.quiblafinder;


public class GreatCircleBearing {

    static public double initial (double lat1, double long1, double lat2,   double long2)
    {
        return (_bearing(lat1, long1, lat2, long2) + 360.0) % 360;
    }

    static public double final1(double lat1, double long1, double lat2, double long2)
    {
        return (_bearing(lat2, long2, lat1, long1) + 180.0) % 360;
    }

    static private double _bearing(double lat1, double long1, double lat2, double long2)
    {
        final double  degToRad = Math.PI / 180.0;
        double phi1 = lat1 * degToRad;
        double phi2 = lat2 * degToRad;
        double lam1 = long1 * degToRad;
        double lam2 = long2 * degToRad;

        return Math.atan2(Math.sin(lam2-lam1)*Math.cos(phi2),
                Math.cos(phi1)*Math.sin(phi2) - Math.sin(phi1)*Math.cos(phi2)*Math.cos(lam2-lam1)
        ) * 180/Math.PI;
    }
}