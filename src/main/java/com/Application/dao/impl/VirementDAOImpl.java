package com.Application.dao.impl;

import com.Application.dao.VirementDAO;
import com.Application.model.Virement;
import com.Application.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VirementDAOImpl implements VirementDAO {
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
        System.err.println("Erreur lors de la vérification de l'ID end_to_end_id : " + endToEndId);
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
            System.err.println("Erreur lors de l'insertion du virement : " + e.getMessage());
        }
        finally{
            db.closeConnection();
        }
    }
}
