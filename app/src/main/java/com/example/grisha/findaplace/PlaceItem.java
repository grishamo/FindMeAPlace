package com.example.grisha.findaplace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class PlaceItem implements Serializable {

    private String m_Title = "";
    private String m_Description = "";
    private String m_Phone = "";
    private String m_Url = "";
    private String m_CustomAddress = "";

    transient private Location m_Location = null;
    private byte[] m_LocationByteArray = null;

    transient private Bitmap m_Image = null; // must be treated specially on serialization
    private byte[] m_ImageByteArray = null;


    public Bitmap getImage() {
        if (m_ImageByteArray != null && m_ImageByteArray.length > 0) {
            setImage(BitmapFactory.decodeByteArray(m_ImageByteArray, 0, m_ImageByteArray.length));
            m_ImageByteArray = null;
        }
        return m_Image;
    }

    public PlaceItem setImage(Bitmap m_Image) {
        this.m_Image = m_Image;
        return this;
    }

    public String getUrl() {
        return m_Url;
    }

    public PlaceItem setUrl(String m_Url) {
        this.m_Url = m_Url;
        return this;
    }

    public String getPhone() {
        return m_Phone;
    }

    public PlaceItem setPhone(String m_Phone) {
        this.m_Phone = m_Phone;
        return this;
    }

    public Location getLocation() {

        if (m_LocationByteArray != null) {
            m_Location = ParcelableUtil.unmarshall(m_LocationByteArray, Location.CREATOR);
        }

        return m_Location;
    }

    public String getCustomLocation() {
        return m_CustomAddress;
    }

    public PlaceItem setLocation(Location m_Location) {
        this.m_Location = m_Location;
        return this;
    }

    public PlaceItem setLocation(String m_Location) {
        this.m_CustomAddress = m_Location;
        return this;
    }

    public String getDescription() {
        return m_Description;
    }

    public PlaceItem setDescription(String m_Description) {
        this.m_Description = m_Description;
        return this;
    }

    public String getTitle() {
        return m_Title;
    }

    public PlaceItem setTitle(String m_Title) {
        this.m_Title = m_Title;
        return this;
    }

    public String getId() {
        return getTitle();
    }

    public PlaceItem toSerializable() {
        if (getImage() != null) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                getImage().compress(Bitmap.CompressFormat.JPEG, 50, stream);
                m_ImageByteArray = stream.toByteArray();

                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setImage(null);
        }

        if (m_Location != null) {
            m_LocationByteArray = ParcelableUtil.marshall(m_Location);
        }

        return this;
    }

    //Fields Serialization
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        toSerializable();
        out.defaultWriteObject();
    }

}
