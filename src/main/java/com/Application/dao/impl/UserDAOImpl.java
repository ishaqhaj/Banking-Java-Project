package com.Application.dao.impl;

import com.Application.dao.UserDAO;
import com.Application.model.User;
import com.Application.util.DatabaseConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    private static Connection connection;

    public UserDAOImpl() {
        // Initialisation de la connexion à la base de données
        try {
            connection = DatabaseConnection.getConnection(); // Appel direct de la méthode statique
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour vérifier si le CIN ou le passeport existe déjà
    public boolean isIdExist(String idValue) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE cin_or_passport = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, idValue);
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        return resultSet.getInt(1) > 0; // Retourner true si le CIN ou passeport existe déjà
    }

    // Méthode pour insérer un utilisateur dans la base de données
    public void insertUser(User user){
        String query = "INSERT INTO users (user_id, name, password, email, cin_or_passport, address, city, postal_code, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getIdValue());
            preparedStatement.setString(6, user.getAddress());
            preparedStatement.setString(7, user.getCity());
            preparedStatement.setString(8, user.getPostalCode());
            preparedStatement.setString(9, user.getCountry());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            System.out.println("Erreur lors de l'insertion de user dans la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Méthode pour vérifier l'unicité de l'identifiant utilisateur
    public boolean isUserIdUnique(String userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        return resultSet.getInt(1) == 0; // Retourner true si l'ID est unique
    }
    public boolean authenticateUser(String userId, String password) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();  // Obtenir la connexion à la base de données

            // Requête SQL pour vérifier les identifiants de connexion
            String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, userId);  // Utiliser l'ID unique (String)
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Si un résultat est retourné, l'utilisateur est authentifié
            return resultSet.next();  

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public  User getUser(String userId){

        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT  name, password, email, cin_or_passport, address, city, postal_code, country FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, userId);  // Utiliser l'ID unique (String)
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                User user = new User(resultSet.getString("cin_or_passport"), userId, resultSet.getString("name"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getString("address"), resultSet.getString("city"), resultSet.getString("postal_code"), resultSet.getString("country"));
                return user;
            }
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    public void closeConnection(){
        try{
            this.connection.close();
        }
        catch(SQLException e){
            System.out.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        }
    }

}
