package com.cg.lostfoundapp.service;

import android.util.Log;

import com.cg.lostfoundapp.model.User;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Client2_2 on 9/6/2016.
 */
public class LoginWSController {
    private static final String URL = "http://192.168.0.13:8080/LoginWS/LoginWS?wsdl";
    private static final String NAMESPACE = "http://ws/";
    private static final String METHOD_LOGIN = "login";
    private static final String METHOD_REGISTER = "register";

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";

    private LoginWSController(){}

    private static final class SingletonHolder{
        private static final LoginWSController SINGLETON = new LoginWSController();
    }

    public static LoginWSController getInstance(){
        return SingletonHolder.SINGLETON;
    }

    public User login(User user){


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_LOGIN);
            SoapSerializationEnvelope env =
                    new SoapSerializationEnvelope(SoapEnvelope.VER11);

            //for request parameter
            request.addProperty("user", user);

            //for response to be mapped to User object..
            SoapObject rtemplate = new SoapObject(NAMESPACE, "loginResponse");
            env.addTemplate(rtemplate);
            env.addMapping(NAMESPACE, "user", User.class);

            env.setOutputSoapObject(request);
            env.dotNet = false;
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(NAMESPACE + METHOD_LOGIN, env);

            User response = (User) env.getResponse();

            return response;


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public boolean register(User user){
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_REGISTER);
            SoapSerializationEnvelope env =
                    new SoapSerializationEnvelope(SoapEnvelope.VER11);

            //for request parameter
            request.addProperty("user", user);

            env.setOutputSoapObject(request);
            env.dotNet = false;
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(NAMESPACE + METHOD_REGISTER, env);

            SoapPrimitive response = (SoapPrimitive) env.getResponse();
            return Boolean.parseBoolean(response.toString());

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}
