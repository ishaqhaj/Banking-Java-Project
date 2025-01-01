package com.Application.dao;

import com.Application.model.User;

public interface UserDAO {
    public boolean isIdExist(String idValue);
    public void insertUser(User user);
    public boolean isUserIdUnique(String userId);
    public boolean authenticateUser(String userId, String password);
    public  User getUser(String userId);
    public String getUserNameById(String userId);
    public void deleteUser(User user);
}
