package com.cg.lostfoundapp.model;

import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by Gabi on 9/23/2016.
 */
public class KVMListItem extends KVMList<Item>{
    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {


        propertyInfo.name = "item";
        propertyInfo.type = Item.class;
    }
}
