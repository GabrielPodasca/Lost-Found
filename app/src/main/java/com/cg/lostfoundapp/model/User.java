package com.cg.lostfoundapp.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class User implements KvmSerializable{


    private int id;
    private String username;
    private String password;
    private String telephone;

    public User(){
    }

    public User(String username, String password, String telephone){
        this.username = username;
        this.password = password;
        this.telephone = telephone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return telephone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.telephone = phoneNumber;
    }

    @Override
    public Object getProperty(int i) {
        switch(i)
        {
            case 0:
                return id;
            case 1:
                return username;
            case 2:
                return password;
            case 3:
                return telephone;
        }

        return null;
    }

    @Override
    public int getPropertyCount() {
        return 4;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i)
        {
            case 0:
                id = Integer.parseInt(o.toString());
                break;
            case 1:
                username = o.toString();
                break;
            case 2:
                password = o.toString();
                break;
            case 3:
                telephone = o.toString();
                break;
            default:
                break;
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
                propertyInfo.name = "username";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "password";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "telephone";
                break;
            default:
                break;
        }
    }
}
