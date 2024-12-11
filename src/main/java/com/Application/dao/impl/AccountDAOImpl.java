package main.java.com.Application.dao.impl;

import main.java.com.Application.dao.AccountDAO;
import main.java.com.Application.dao.impl.BankDAOImpl;
import main.java.com.Application.model.Account;
import main.java.com.Application.model.Bank;
import main.java.com.Application.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


public class AccountDAOImpl implements AccountDAO {
    private Connection connection;
    private BankDAOImpl bankDAO;

    // Constructeur pour injecter BankDAO
    public AccountDAOImpl() {
        try {
            connection = DatabaseConnection.getConnection(); // Appel direct de la méthode statique
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.bankDAO = new BankDAOImpl();
    }

    public void insertAccount(Account account)  {
        try {
            // Obtenir l'ID de la banque
            Integer bankId = bankDAO.getBankId(account.getBank());
            String query = "INSERT INTO accounts (account_number, account_type, bank_id, user_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getAccountType());
            preparedStatement.setInt(3, bankId); // Utiliser l'ID de la banque
            preparedStatement.setString(4, account.getOwner().getUserId()); // Utiliser l'ID utilisateur incrémenté
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println("Erreur connexion lors de l'insertion des données dans la table accounts");
            e.printStackTrace();
        }
    }

}



