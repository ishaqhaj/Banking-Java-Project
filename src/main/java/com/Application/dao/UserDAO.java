package main.java.com.Application.dao;

import main.java.com.Application.model.User;

import java.sql.SQLException;
import java.util.Map;

public interface UserDAO {
    public boolean isIdExist(String idValue) throws SQLException;
    public void insertUser(User user) throws SQLException;
    public boolean isUserIdUnique(String userId) throws SQLException;
}
