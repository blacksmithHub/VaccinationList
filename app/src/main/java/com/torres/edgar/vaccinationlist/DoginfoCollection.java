package com.torres.edgar.vaccinationlist;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by emtorres on 1/31/2018.
 */

public class DoginfoCollection implements Serializable {


    public ArrayList<String> getDoginfo() {
        return doginfo;
    }

    public void setDoginfo(ArrayList<String> doginfo) {
        this.doginfo = doginfo;
    }

    private ArrayList<String> doginfo;


}
