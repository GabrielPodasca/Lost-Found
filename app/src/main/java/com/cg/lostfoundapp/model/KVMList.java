package com.cg.lostfoundapp.model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by Gabi on 9/22/2016.
 */
public abstract class KVMList<T> extends ArrayList<T> implements KvmSerializable{

    //if list is used as request param
    private boolean reqParam = false;

    public boolean isReqParam() {
        return reqParam;
    }

    public void setReqParam(boolean reqParam) {
        this.reqParam = reqParam;
    }

    @Override
    public Object getProperty(int i) {
        return this.get(i);
    }

    @Override
    public int getPropertyCount() {
        return reqParam ? this.size() : 1;
    }

    @Override
    public void setProperty(int i, Object o) {
        this.add((T)o);
    }

    @Override
    public abstract void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo);
}
