package com.Application.dao.impl;

import com.Application.model.Account;
import com.Application.model.Bank;
import com.Application.model.User;
import com.Application.util.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDAOImplTest {
    UserDAOImpl userDAO;
    User testUser;

    @BeforeEach
    public void setUp(){
        userDAO = new UserDAOImpl();
        Bank testBank = new Bank("CIH", "CIHMMAMC");
        Account testAccount = new Account("XK051212012345678906", "CACC", testBank);
        testUser = new User("AB78899", "AX6578", "Albert Roben", "rtyyu234@", "rober@gmail.com", "Washington city build.14", "Washington", "10000", "US", testAccount);
    }

    @Test
    public void isIdExistTest(){
        userDAO.insertUser(testUser);
        boolean exists=userDAO.isIdExist("AB78899");
        assertEquals(true,exists,"Cet identifiant d'identité doit être existant.");

        exists=userDAO.isIdExist("000000");
        assertEquals(false,exists,"Cet identifiant n'est pas présent pour le moment.");
        userDAO.deleteUser(testUser);
    }

    @Test
    public void insertUserTest() {
        userDAO.insertUser(testUser);
        String query = "SELECT  name, password, email, cin_or_passport, address, city, postal_code, country FROM users WHERE user_id = ?";
        DatabaseConnection db = new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, testUser.getUserId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String retrievedName = resultSet.getString("name");
                String retrievedPassword = resultSet.getString("password");
                String retrievedEmail = resultSet.getString("email");
                String retrievedCinOrPassport = resultSet.getString("cin_or_passport");
                String retrievedAddress = resultSet.getString("address");
                String retrievedCity = resultSet.getString("city");
                String retrievedPostal_code = resultSet.getString("postal_code");
                String retrievedCountry = resultSet.getString("country");
                // Assertions pour vérifier les données
                assertEquals(testUser.getName(), retrievedName);
                assertEquals(testUser.getPassword(), retrievedPassword);
                assertEquals(testUser.getEmail(), retrievedEmail);
                assertEquals(testUser.getIdValue(), retrievedCinOrPassport);
                assertEquals(testUser.getAddress(), retrievedAddress);
                assertEquals(testUser.getCity(), retrievedCity);
                assertEquals(testUser.getPostalCode(), retrievedPostal_code);
                assertEquals(testUser.getCountry(), retrievedCountry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
    }

    @Test
    public void isUserIdUniqueTest(){
        userDAO.insertUser(testUser);
        boolean exists=userDAO.isUserIdUnique(testUser.getUserId());
        assertEquals(false,exists,"Cet Id existe déjà.");
        exists=userDAO.isUserIdUnique("00000");
        assertEquals(true,exists,"Cet Id ne doit jamais existé car la taille d'un identifiant généré est toujours égale à 6    .");
    }

    @Test
    public void authenticateUserTest(){
        userDAO.insertUser(testUser);
        boolean isAuthenticated=userDAO.authenticateUser(testUser.getUserId(), testUser.getPassword());
        assertEquals(true,isAuthenticated,"Cet utiliisateur doit être connecté pour le moment.");

        isAuthenticated=userDAO.authenticateUser(testUser.getUserId(), "5667777");
        assertEquals(false,isAuthenticated,"Le mot de passe est incorrect.");
    }

    @Test
    public void getUserTest(){
        userDAO.insertUser(testUser);
        User userResult=userDAO.getUser(testUser.getUserId());
        assertEquals(testUser.getName(), userResult.getName());
        assertEquals(testUser.getPassword(), userResult.getPassword());
        assertEquals(testUser.getEmail(), userResult.getEmail());
        assertEquals(testUser.getIdValue(), userResult.getIdValue());
        assertEquals(testUser.getAddress(), userResult.getAddress());
        assertEquals(testUser.getCity(), userResult.getCity());
        assertEquals(testUser.getPostalCode(), userResult.getPostalCode());
        assertEquals(testUser.getCountry(), userResult.getCountry());
    }

    @Test
    public void getUserNameByIdTest(){
        userDAO.insertUser(testUser);
        String retrievedName=userDAO.getUserNameById(testUser.getUserId());
        assertEquals(testUser.getName(),retrievedName);
    }

    @AfterEach
    public void tearDown() {
        // Nettoyer la base de données après le test
        userDAO.deleteUser(testUser);
    }
}
