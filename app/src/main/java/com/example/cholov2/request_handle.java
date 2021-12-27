package com.example.cholov2;

import com.google.android.gms.maps.model.LatLng;

public class request_handle {
    String dum,cnum,cost;
 LatLng strt;

    public request_handle( String cnum,String cost, LatLng des , LatLng dloc,String dum,LatLng strt) {
        this.dum = dum;
        this.cnum = cnum;
        this.cost = cost;
        this.strt = strt;
        this.des = des;
        this.dloc = dloc;
    }

    public String getDum() {
        return dum;
    }

    public void setDum(String dum) {
        this.dum = dum;
    }

    public String getCnum() {
        return cnum;
    }

    public void setCnum(String cnum) {
        this.cnum = cnum;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public LatLng getStrt() {
        return strt;
    }

    public void setStrt(LatLng strt) {
        this.strt = strt;
    }

    public LatLng getDes() {
        return des;
    }

    public void setDes(LatLng des) {
        this.des = des;
    }

    public LatLng getDloc() {
        return dloc;
    }

    public void setDloc(LatLng dloc) {
        this.dloc = dloc;
    }

    LatLng des;
    LatLng dloc;


}
