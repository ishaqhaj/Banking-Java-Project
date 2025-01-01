package com.Application.dao.impl;

import com.Application.dao.VirementDAO;
import com.Application.model.Virement;
import com.Application.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VirementDAOImpl implements VirementDAO {
	private static final Logger LOGGER = Logger.getLogger(VirementDAOImpl.class.getName());
// Vérifie si un end_to_end_id existe déjà
public boolean isEndToEndIdUnique(String endToEndId) {
    String query = "SELECT 1 FROM virement WHERE end_to_end_id = ? LIMIT 1";
    boolean isUnique = true;
    DatabaseConnection db= new DatabaseConnection();
    try (Connection conn = db.getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(query)) {

        preparedStatement.setString(1, endToEndId);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                isUnique = false; // Si une ligne est trouvée, l'ID n'est pas unique
            }
        }

    } catch (SQLException e) {
    	LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'ID end_to_end_id : " + endToEndId);
        e.printStackTrace();
    }
    finally{
        db.closeConnection();
    }
    return isUnique;
}
    public void insertVirement(Virement virement) {
        String query = "INSERT INTO virement (end_to_end_id, debtor_account_number, creditor_account_number, amount, currency, timestamp, motif, type,methode_paiement) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        DatabaseConnection db= new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, virement.getEndToEndId());
            preparedStatement.setString(2, virement.getDebtorAccount().getAccountNumber());
            preparedStatement.setString(3, virement.getCreditorAccount().getAccountNumber());
            preparedStatement.setBigDecimal(4, virement.getAmount());
            preparedStatement.setString(5, virement.getCurrency());
            preparedStatement.setString(6, virement.getTimestamp());
            preparedStatement.setString(7, virement.getMotif());
            preparedStatement.setString(8, virement.getType());
            preparedStatement.setString(9, virement.getPayMethod());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de l'insertion du virement : " + e.getMessage());
        }
        finally{
            db.closeConnection();
        }
    }
    public void insertVirements(List<Virement> virements) {
        String query = "INSERT INTO virement (end_to_end_id, debtor_account_number, creditor_account_number, amount, currency, timestamp, motif, type, methode_paiement) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false); // Désactiver l'auto-commit pour une transaction manuelle
            preparedStatement = conn.prepareStatement(query);

            for (Virement virement : virements) {
                preparedStatement.setString(1, virement.getEndToEndId());
                preparedStatement.setString(2, virement.getDebtorAccount().getAccountNumber());
                preparedStatement.setString(3, virement.getCreditorAccount().getAccountNumber());
                preparedStatement.setBigDecimal(4, virement.getAmount());
                preparedStatement.setString(5, virement.getCurrency());
                preparedStatement.setString(6, virement.getTimestamp());
                preparedStatement.setString(7, virement.getMotif());
                preparedStatement.setString(8, virement.getType());
                preparedStatement.setString(9, virement.getPayMethod());
                preparedStatement.addBatch(); // Ajouter l'instruction dans le batch
            }

            preparedStatement.executeBatch(); // Exécuter le batch
            conn.commit(); // Valider les changements
        } catch (SQLException e) {
            // Gestion des erreurs et rollback en cas de problème
            if (conn != null) {
                try {
                    conn.rollback(); // Annuler les changements en cas d'erreur
                } catch (SQLException rollbackEx) {
                	LOGGER.log(Level.SEVERE, "Erreur lors du rollback : " + rollbackEx.getMessage());
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de l'insertion des virements : " + e.getMessage());
        } finally {
            // Fermeture des ressources
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                	LOGGER.log(Level.SEVERE, "Erreur lors de la fermeture du PreparedStatement : " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                	LOGGER.log(Level.SEVERE, "Erreur lors de la fermeture de la connexion : " + e.getMessage());
                }
            }
        }
    }

    public List<Virement> getVirementsByUserId(String userId) {
        String query = """
                SELECT v.end_to_end_id, 
                       v.debtor_account_number, 
                       v.creditor_account_number, 
                       v.amount, 
                       v.currency, 
                       v.timestamp, 
                       v.motif, 
                       v.type,
                       v.methode_paiement
                FROM virement v
                INNER JOIN accounts a ON v.debtor_account_number = a.account_number
                WHERE a.user_id = ?
                ORDER BY v.timestamp DESC
                LIMIT 10;
                """;
        List<Virement> virements = new ArrayList<>();
        DatabaseConnection db = new DatabaseConnection();

        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            // Set the userId parameter in the query
            preparedStatement.setString(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                AccountDAOImpl accountDAO=new AccountDAOImpl();
                // Process the result set and create Virement objects
                while (resultSet.next()) {
                    Virement virement = new Virement(resultSet.getString("timestamp"),accountDAO.getAccount(resultSet.getString("creditor_account_number")),resultSet.getBigDecimal("amount"),resultSet.getString("currency"),resultSet.getString("motif"),resultSet.getString("type"),resultSet.getString("methode_paiement"));
                    virements.add(virement);
                }
            }

        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des virements de l'utilsateur courant.");
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

        return virements;
    }

    public void deleteVirement(String endToEnd){
        String query = "DELETE FROM virement WHERE end_to_end_id = ?";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, endToEnd);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            db.closeConnection();
        }
    }

}
