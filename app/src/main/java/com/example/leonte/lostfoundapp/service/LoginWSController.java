package com.example.leonte.lostfoundapp.service;

import com.example.leonte.lostfoundapp.model.User;

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

    public boolean login(User user){

        boolean login = false;

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_LOGIN);
            SoapSerializationEnvelope env =
                    new SoapSerializationEnvelope(SoapEnvelope.VER11);

            PropertyInfo userPI = new PropertyInfo();
            userPI.setName("user");
            userPI.setValue(user);
            userPI.setType(user.getClass());

            request.addProperty(userPI);
            

            env.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(NAMESPACE + METHOD_LOGIN, env);

            SoapPrimitive response = (SoapPrimitive) env.getResponse();

            login = Boolean.parseBoolean(response.toString());


        }catch (Exception e){
            e.printStackTrace();
        }

        return login;
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
