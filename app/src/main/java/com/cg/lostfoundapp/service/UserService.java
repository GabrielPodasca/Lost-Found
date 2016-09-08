package com.cg.lostfoundapp.service;

import android.content.Context;

import com.cg.lostfoundapp.dao.UserDao;
import com.cg.lostfoundapp.model.User;

public class UserService{

    private UserService(){}

    private static final class SingletonHolder{
        private static final UserService SINGLETON = new UserService();
    }

    public static UserService getInstance(){
        return SingletonHolder.SINGLETON;
    }

    public String register(Context context, String username, String password, String phoneNumber){
        UserDao userDao = new UserDao(context);
        User user = userDao.findUserByUsername(username);
        String message = "nok";
        if(user == null){
            user = new User(username,password,phoneNumber);
            userDao.addUser(user);
            message = "ok";
        }
        return message;
    }

    public User login(Context context, String username, String password){
        UserDao userDao = new UserDao(context);
        User user = userDao.findUserByUsername(username);
        if(user != null){
            if(user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    public String getPhoneNumberByUsername(Context context, String username){
        UserDao userDao = new UserDao(context);
        return userDao.getPhoneNumberByUsername(username);
    }

}
