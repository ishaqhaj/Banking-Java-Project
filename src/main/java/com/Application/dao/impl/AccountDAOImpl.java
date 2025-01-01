package com.Application.dao.impl;

import com.Application.dao.AccountDAO;
import com.Application.model.Account;
import com.Application.model.Bank;
import com.Application.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AccountDAOImpl implements AccountDAO {
    private BankDAOImpl bankDAO;
    private static final Logger LOGGER = Logger.getLogger(AccountDAOImpl.class.getName());
    // Constructeur pour injecter BankDAO
    public AccountDAOImpl() {
        this.bankDAO = new BankDAOImpl();
    }

    public void insertAccount(Account account) {
        DatabaseConnection db = new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(
                 "INSERT INTO accounts (account_number, account_type, bank_id, user_id) VALUES (?, ?, ?, ?)")) {
            
            // Obtenir l'ID de la banque
            Integer bankId = bankDAO.getBankId(account.getBank());
            
            preparedStatement.setString(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getAccountType());
            preparedStatement.setInt(3, bankId); // Utiliser l'ID de la banque
            preparedStatement.setString(4, account.getOwner().getUserId()); // Utiliser l'ID utilisateur incrémenté
            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur connexion lors de l'insertion des données dans la table accounts: ");
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
    }

    public String findUserIdByAccountNumber(String accountNumber)  {
        String query = "SELECT user_id FROM accounts WHERE account_number = ?";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn = db.getConnection();
        	PreparedStatement preparedStatement = conn.prepareStatement(query);){
            preparedStatement.setString(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("user_id");
            }
        }
        catch(SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur au niveau de la récupération de l'identifiant de l'utilisateur à partir du IBAN.");
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
                return new Account(accountNumber, resultSet.getString("account_type"), bank);
            }
            return null;
        }
        catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur connexion : ");
            e.printStackTrace();
            return null;
        }
        finally{
            db.closeConnection();
        }
    }
    public boolean hasSingleAccount(String userId) {
        String query = "SELECT COUNT(*) AS account_count FROM accounts WHERE user_id = ?";
        DatabaseConnection db=new DatabaseConnection();
        boolean hasSingleAccount = false;
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("account_count");
                hasSingleAccount = (count == 1); // True si un seul compte est trouvé
            }
            return hasSingleAccount;
        }
        catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de la vérification des comptes pour userId : " + userId);
            e.printStackTrace();
            return hasSingleAccount;
        }
        finally{
            db.closeConnection();
        }
    }
    public Account getUserAccount(String userId) {
        String query = "SELECT account_number FROM accounts WHERE user_id = ?";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return getAccount(resultSet.getString("account_number"));
                }
                return null;
            }

        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du compte pour userId : " + userId);
            e.printStackTrace();
            return null;
        }
        finally{
            db.closeConnection();
        }
    }

    public Set<String> getUserAccounts(String userId) {
        Set<String> accountNumbers = new HashSet<>();
        String query = "SELECT account_number FROM accounts WHERE user_id = ?";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accountNumbers.add(resultSet.getString("account_number"));
            }
            return accountNumbers;
        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des comptes pour userId : " + userId);
            e.printStackTrace();
            return accountNumbers;
        }
        finally{
            db.closeConnection();
        }

    }
    public boolean addBeneficiaryAccount(String iban,String beneficiaryIban){
        String query = "INSERT INTO account_beneficiaries (user_account, beneficiary_account) VALUES (?, ?)";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn=db.getConnection();
        	PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString(1, iban);
            preparedStatement.setString(2, beneficiaryIban);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l ajout du compte du bénéficiare");
            e.printStackTrace();
            return false;
        }
        finally {
            db.closeConnection();
        }
    }
    public boolean isBeneficiaryExists(String iban,String beneficiayIban){
        String query = "SELECT 1 FROM account_beneficiaries WHERE user_account = ? AND beneficiary_account = ?";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, iban);
            preparedStatement.setString(2, beneficiayIban);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        }
        catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de l ajout du compte du bénéficiare");
            e.printStackTrace();
            return false;
        }
        finally {
            db.closeConnection();
        }

    }
    public Map<String, String> getBeneficiaries(String iban) {
        Map<String, String> beneficiaries = new HashMap<>();
        String query = "SELECT b.beneficiary_account AS BeneficiaryAccount, " +
                "u.name AS BeneficiaryOwnerName " +
                "FROM account_beneficiaries b " +
                "INNER JOIN accounts a ON b.beneficiary_account = a.account_number " +
                "INNER JOIN users u ON a.user_id = u.user_id " +
                "WHERE b.user_account = ?;";
        DatabaseConnection db = new DatabaseConnection();
        try (Connection conn = db.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, iban);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        beneficiaries.put(resultSet.getString("BeneficiaryOwnerName"), resultSet.getString("BeneficiaryAccount"));
                    }
                }
            }
        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des bénéficiaires.");
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return beneficiaries;
    }
    public void deleteAccount(Account account){
        String query = "DELETE FROM accounts WHERE account_number = ?";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, account.getAccountNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            db.closeConnection();
        }
    }

    public void deleteAccountBeneficiary(String iban,String beneficiaryIban){
        String query = "DELETE FROM account_beneficiaries WHERE  user_account= ? AND beneficiary_account = ?";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, iban);
            preparedStatement.setString(2, beneficiaryIban);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            db.closeConnection();
        }
    }
}



