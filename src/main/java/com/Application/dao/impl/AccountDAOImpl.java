package com.Application.dao.impl;

import com.Application.dao.AccountDAO;
import com.Application.dao.impl.BankDAOImpl;
import com.Application.model.Account;
import com.Application.model.Bank;
import com.Application.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class AccountDAOImpl implements AccountDAO {
    private BankDAOImpl bankDAO;
    // Constructeur pour injecter BankDAO
    public AccountDAOImpl() {
        this.bankDAO = new BankDAOImpl();
    }

    public void insertAccount(Account account)  {
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn=db.getConnection()){
            // Obtenir l'ID de la banque
            Integer bankId = bankDAO.getBankId(account.getBank());
            String query = "INSERT INTO accounts (account_number, account_type, bank_id, user_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
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
        finally{
            db.closeConnection();
        }
    }
    public String findUserIdByAccountNumber(String accountNumber)  {
        String query = "SELECT user_id FROM accounts WHERE account_number = ?";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn = db.getConnection()){
             PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("user_id");
            }
        }
        catch(SQLException e) {
            System.out.println("Erreur au niveau de la récupération de l'identifiant de l'utilisateur à partir du IBAN.");
            return null;
        }
        finally {
            db.closeConnection();
        }
        return null;
    }
    public Account getAccount(String accountNumber)  {
        String query = "SELECT account_type,bank_id FROM accounts WHERE account_number = ?";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Bank bank = bankDAO.getBank(resultSet.getInt("bank_id"));
                Account account = new Account(accountNumber, resultSet.getString("account_type"), bank);
                return account;
            }
            return null;
        }
        catch (SQLException e) {
            System.out.println("Erreur connexion");
            e.printStackTrace();
            return null;
        }
        finally{
            db.closeConnection();
        }
    }
}



