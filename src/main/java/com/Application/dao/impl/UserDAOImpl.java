package com.Application.dao.impl;

import com.Application.dao.UserDAO;
import com.Application.model.Address;
import com.Application.model.User;
import com.Application.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAOImpl implements UserDAO {
	private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());
    // Méthode pour vérifier si le CIN ou le passeport existe déjà
    public boolean isIdExist(String idValue) {
        String query = "SELECT COUNT(*) FROM users WHERE cin_or_passport = ?";
        DatabaseConnection db=new DatabaseConnection();
        try(Connection conn=db.getConnection();
        	PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, idValue);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        }// Retourner true si le CIN ou passeport existe déjà
        catch(SQLException e){
        	LOGGER.log(Level.SEVERE, "Erreur lors de l'extraction de données de la table users: ",e);
            return false;
        }
        finally{
            db.closeConnection();
        }
    }

    // Méthode pour insérer un utilisateur dans la base de données
    public void insertUser(User user){
        String query = "INSERT INTO users (user_id, name, password, email, cin_or_passport, address, city, postal_code, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        DatabaseConnection db=new DatabaseConnection();
        try(Connection conn=db.getConnection();
        	PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getIdValue());
            preparedStatement.setString(6, user.getAddress().getAddress());
            preparedStatement.setString(7, user.getAddress().getCity());
            preparedStatement.setString(8, user.getAddress().getPostalCode());
            preparedStatement.setString(9, user.getAddress().getCountry());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
        	LOGGER.log(Level.SEVERE, "Erreur lors de l'insertion de user dans la base de données: " + e.getMessage());
        }
        finally{
            db.closeConnection();
        }
    }


    // Méthode pour vérifier l'unicité de l'identifiant utilisateur
    public boolean isUserIdUnique(String userId) {
        DatabaseConnection db=new DatabaseConnection();
        String query = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        try (Connection conn = db.getConnection();
        	PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            return resultSet.getInt(1) == 0; // Retourner true si l'ID est unique
        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de l'insertion de user dans la base de données: " + e.getMessage());
            return false;
        } finally {
            db.closeConnection();
        }
    }
    public boolean authenticateUser(String userId, String password) {
        DatabaseConnection db=new DatabaseConnection();
        String sql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
        try (Connection conn = db.getConnection();
        	PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            // Requête SQL pour vérifier les identifiants de connexion
            preparedStatement.setString(1, userId);  // Utiliser l'ID unique (String)
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Si un résultat est retourné, l'utilisateur est authentifié
            return resultSet.next();  

        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de l'authentification de l'utilisateur.", e);
            return false;
        }
        finally {
            db.closeConnection();
        }
    }
    public  User getUser(String userId){
        DatabaseConnection db=new DatabaseConnection();
        String query = "SELECT  name, password, email, cin_or_passport, address, city, postal_code, country FROM users WHERE user_id = ?";
        try (Connection conn = db.getConnection();
        	PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString(1, userId);  // Utiliser l'ID unique (String)
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
            	Address address=new Address(resultSet.getString("address"), resultSet.getString("city"), resultSet.getString("postal_code"), resultSet.getString("country"));
                return new User(resultSet.getString("cin_or_passport"), userId, resultSet.getString("name"), resultSet.getString("password"), resultSet.getString("email"), address);
            }
            return null;
        }
        catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de l'obtention de l'utilisateur.", e);
            return null;
        }
        finally {
            db.closeConnection();
        }
    }
    public String getUserNameById(String userId) {
        String query = "SELECT name FROM users WHERE user_id = ?";
        DatabaseConnection db = new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
            return null;
        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur de connexion lors de la récupération du nom de l'utilisateur à partir de son identifiant.");
            return null;
        } finally {
            db.closeConnection();
        }
    }
    public void deleteUser(User user){
        String query = "DELETE FROM users WHERE user_id = ?";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUserId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'utilisateur.", e);
        }
        finally{
            db.closeConnection();
        }
    }
}
