package com.example.grisha.findaplace;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static android.content.Context.MODE_PRIVATE;

//TODO change to Singelton
// private constructor, static final instance.... etc.
public class DataManager {

    private static PlacesManager m_MyPlaces = null;
    private static final String MY_PLACES_DIRECTORY = "places_data";
    private static final String MY_PLACES_CATEGORY_NAME = "my_places";

    public static PlacesManager GetMyPlacesManager(Context context)
    {
        PlacesManager returnValue = m_MyPlaces;

        if(returnValue == null)
        {
            try {
                FileInputStream fis = context.openFileInput(MY_PLACES_DIRECTORY);
                ObjectInputStream ois = new ObjectInputStream(fis);
                m_MyPlaces = (PlacesManager)ois.readObject();
                ois.close();

                returnValue = m_MyPlaces;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return returnValue;

    }

    public static void SavePlace(Context context, PlaceItem placeItem)
    {
        if(m_MyPlaces == null)
        {
            m_MyPlaces = new PlacesManager();
        }

        m_MyPlaces.getPlaces().put(placeItem.getTitle(), placeItem);

        try {

            FileOutputStream fos = context.openFileOutput(MY_PLACES_DIRECTORY, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(m_MyPlaces);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GetLocalData(Context context)
    {
        m_MyPlaces = getCageoryPlacesByName(context, MY_PLACES_CATEGORY_NAME);
    }

    public static int LocalDataSize()
    {
        int returnValue = 0;
        if(m_MyPlaces != null)
        {
            returnValue = m_MyPlaces.getPlaces().size();
        }

        return returnValue;
    }

    public static PlacesManager getCageoryPlacesByName(Context context, String categoryName) {

        PlacesManager returnValue = null;

        if( categoryName.equals(MY_PLACES_CATEGORY_NAME))
        {
            returnValue = DataManager.GetMyPlacesManager(context);
        };

        return  returnValue;
    }
}
