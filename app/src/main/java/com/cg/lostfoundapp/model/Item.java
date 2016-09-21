package com.cg.lostfoundapp.model;

import org.kobjects.isodate.IsoDate;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.PropertyInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 * Created by Gabi on 9/21/2016.
 */
public class Item implements KvmSerializable{

    public static final String LOST  = "LOST";
    public static final String FOUND = "FOUND";
    private int id;
    private String name;
    private String description;
    private double lat;
    private double lng;
    private String address;
    private Date when;
    private String type;
    private User user;

    private Item() {

    }

    public Item(int id, String name, String description, double lat, double lng, String address, Date when, String type, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.when = when;
        this.type = type;
        this.user = user;
    }

    public Item(String name, String description, double lat, double lng, String address, Date when, String type, User user) {
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.when = when;
        this.type = type;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Object getProperty(int i) {
        switch(i)
        {
            case 0:
                return id;
            case 1:
                return name;
            case 2:
                return description;
            case 3:
                return lat;
            case 4:
                return lng;
            case 5:
                return address;
            case 6:
                return when;
            case 7:
                return type;
            case 8:
                return user;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 9;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i)
        {
            case 0:
                id = Integer.parseInt(o.toString());
            case 1:
                name = o.toString();
            case 2:
                description = o.toString();
            case 3:
                lat = Double.parseDouble(o.toString());
            case 4:
                lng = Double.parseDouble(o.toString());
            case 5:
                address = o.toString();
            case 6:
                when = (Date)o;
            case 7:
                type = o.toString();
            case 8:
                user = (User)o;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch(i)
        {
            case 0:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "id";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "name";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "description";
                break;
            case 3:
                propertyInfo.type = Double.class;
                propertyInfo.name = "lat";
                break;
            case 4:
                propertyInfo.type = Double.class;
                propertyInfo.name = "lng";
                break;
            case 5:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "address";
                break;
            case 6:
                propertyInfo.type = MarshalDate.DATE_CLASS;
                propertyInfo.name = "when";
                break;
            case 7:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "type";
                break;
            case 8:
                propertyInfo.type = User.class;
                propertyInfo.name = "user";
                break;
        }
    }
}
