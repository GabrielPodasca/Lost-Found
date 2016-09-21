package com.cg.lostfoundapp.service;

import com.cg.lostfoundapp.model.Item;
import com.cg.lostfoundapp.model.MarshalDouble;
import com.cg.lostfoundapp.model.User;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Gabi on 9/21/2016.
 */
public class ItemWSController {
    private static final String URL = "http://192.168.0.13:8080/ItemsWS/ItemsWS?wsdl";
    private static final String NAMESPACE = "http://ws/";
    private static final String METHOD_ADD = "add";

    private ItemWSController(){}

    private static final class SingletonHolder{
        private static final ItemWSController SINGLETON = new ItemWSController();
    }

    public static ItemWSController getInstance(){
        return SingletonHolder.SINGLETON;
    }

    public ItemWSController add(Item item){


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_ADD);
            SoapSerializationEnvelope env =
                    new SoapSerializationEnvelope(SoapEnvelope.VER11);

            //for request parameter
            request.addProperty("item", item);


            env.addMapping(NAMESPACE, "item", Item.class);
            env.addMapping(NAMESPACE, "user", User.class);

            MarshalDate marshalDate = new MarshalDate();
            MarshalDouble marshalDouble = new MarshalDouble();
            marshalDate.register(env);
            marshalDouble.register(env);


            env.setOutputSoapObject(request);
            env.dotNet = false;

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(NAMESPACE + METHOD_ADD, env);


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
