package com.cg.lostfoundapp.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by Gabi on 9/8/2016.
 */
public class LoginWSResponse implements KvmSerializable {
    private User user;
    private String message;

    public LoginWSResponse() {
    }

    public LoginWSResponse(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Object getProperty(int i) {
        switch(i)
        {
            case 0:
                return user;
            case 1:
                return message;
        }

        return null;
    }

    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i)
        {
            case 0:
                user = (User)o;
                break;
            case 1:
                message = o.toString();
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
                propertyInfo.type = User.class;
                propertyInfo.name = "user";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "message";
                break;
            default:
                break;
        }
    }
}
