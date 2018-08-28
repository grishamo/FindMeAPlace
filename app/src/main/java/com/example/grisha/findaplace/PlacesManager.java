package com.example.grisha.findaplace;

import java.io.Serializable;
import java.util.HashMap;

public class PlacesManager implements Serializable {

    private HashMap<String, PlaceItem> myPlaces = new HashMap<>();

    public HashMap<String, PlaceItem> getPlaces() {
        return myPlaces;
    }
}
