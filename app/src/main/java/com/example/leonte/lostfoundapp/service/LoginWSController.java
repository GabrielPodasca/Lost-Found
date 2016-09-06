package com.example.leonte.lostfoundapp.service;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Client2_2 on 9/6/2016.
 */
public class LoginWSController {
    private static final String URL = "http://192.168.1.8:8080/LoginWS/LoginWS?wsdl";
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

    public String login(String username, String password){
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_LOGIN);
            SoapSerializationEnvelope env =
                    new SoapSerializationEnvelope(SoapEnvelope.VER11);
            request.addProperty(PARAM_USERNAME, username);
            request.addProperty(PARAM_PASSWORD, password);

            env.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(NAMESPACE + METHOD_LOGIN, env);

            SoapPrimitive respose = (SoapPrimitive) env.getResponse();
            return respose.toString();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public String register(String username, String password){
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_REGISTER);
            SoapSerializationEnvelope env =
                    new SoapSerializationEnvelope(SoapEnvelope.VER11);
            request.addProperty(PARAM_USERNAME, username);
            request.addProperty(PARAM_PASSWORD, password);

            env.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(NAMESPACE + METHOD_REGISTER, env);

            SoapPrimitive respose = (SoapPrimitive) env.getResponse();
            return respose.toString();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
