package com.example.cholov2;

import com.google.android.gms.maps.model.LatLng;

public class location_helper {
   String pnum;

    public location_helper(String pnum, double x, double y) {
        this.pnum = pnum;
        this.x = x;
        this.y = y;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    double x,y;


}
