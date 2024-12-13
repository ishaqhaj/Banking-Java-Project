package com.Application.dao;

import com.Application.model.User;

import java.sql.SQLException;
import java.util.Map;

public interface UserDAO {
    public boolean isIdExist(String idValue) throws SQLException;
    public void insertUser(User user) throws SQLException;
    public boolean isUserIdUnique(String userId) throws SQLException;
    public boolean authenticateUser(String userId, String password);
    public  User getUser(String userId);
    public void closeConnection();
}
