package com.example.grisha.findaplace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class PlaceItem implements Serializable {

    private String m_Title = "";
    private String m_Description = "";
    private String m_Location = "";
    private String m_Phone = "";
    private String m_Url = "";
    private Bitmap m_Image = null; // must be treated specially on serialization
    private byte[] m_ImageByteArray = null;


    public Bitmap getImage() {
        if(m_ImageByteArray != null && m_ImageByteArray.length > 0)
        {
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

    public String getLocation() {
        return m_Location;
    }

    public PlaceItem setLocation(String m_Location) {
        this.m_Location = m_Location;
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
        if(getImage() !=  null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            getImage().compress(Bitmap.CompressFormat.JPEG, 50, stream);
            m_ImageByteArray = stream.toByteArray();

            setImage(null);
        }
        return this;
    }

    //Fields Serialization
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if(m_Image != null)
        {
            m_Image.compress(Bitmap.CompressFormat.JPEG, 50, out);
        }
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        m_Image = BitmapFactory.decodeStream(in);
        in.defaultReadObject();
    }
}
