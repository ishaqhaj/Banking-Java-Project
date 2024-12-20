package com.Application.dao.impl;

import com.Application.dao.BankDAO;
import com.Application.model.Bank;
import com.Application.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankDAOImpl implements BankDAO {
    public  Bank findBank(Bank bank) {
        String query;
        DatabaseConnection db=new DatabaseConnection();
        PreparedStatement preparedStatement;

        // Si le BIC est fourni, on cherche avec le name et le BIC
        try(Connection conn=db.getConnection()){

            query = "SELECT bank_id, name, bic FROM bank WHERE name = ? AND bic = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, bank.getName());
            preparedStatement.setString(2, bank.getBic());

            ResultSet resultSet = preparedStatement.executeQuery();

            // Si on trouve la banque avec les critères fournis
            if (resultSet.next()) {
                return new Bank(resultSet.getString("name"), resultSet.getString("bic"));
            } else {
                // Sinon, on teste séparément pour donner des messages d'erreur spécifiques
                if (bank.getBic() != null && !bank.getBic().trim().isEmpty()) {
                    // Vérifier uniquement par name
                    query = "SELECT name FROM bank WHERE name = ?";
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, bank.getName());
                    ResultSet nameResult = preparedStatement.executeQuery();
                    if (nameResult.next()) {
                        throw new IllegalArgumentException("Le BIC " + bank.getBic() + " ne correspond pas à la banque " + bank.getName());
                    }

                    // Vérifier uniquement par BIC
                    query = "SELECT bic FROM bank WHERE bic = ?";
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, bank.getBic());
                    ResultSet bicResult = preparedStatement.executeQuery();
                    if (bicResult.next()) {
                        throw new IllegalArgumentException("Le nom de la banque " + bank.getName() + " ne correspond pas au BIC " + bank.getBic());
                    }
                }

                // Si aucune correspondance trouvée
                throw new IllegalArgumentException("La banque " + bank.getName() + " n'existe pas.");
            }
        }
        catch (SQLException e) {
            System.out.println("Erreur de connexion lors de l'essai de récupération des données de la table bank" + e.getMessage());
            return null;
        }
        finally{
            db.closeConnection();
        }
    }


    public Integer getBankId(Bank bank){
        String query;
        DatabaseConnection db=new DatabaseConnection();
        try(Connection conn=db.getConnection()) {
            PreparedStatement preparedStatement;
            if (bank.getBic() != null) {
                // Rechercher par name et bic
                query = "SELECT bank_id FROM bank WHERE name = ? AND bic = ?";

                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, bank.getName());
                preparedStatement.setString(2, bank.getBic());
            } else {
                // Rechercher uniquement par name si bic est nul
                query = "SELECT bank_id FROM bank WHERE name = ?";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, bank.getName());
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("bank_id");
            } else {
                throw new IllegalArgumentException("Aucune banque trouvée pour le nom '" + bank.getName() +
                        (bank.getBic() != null ? "' et le BIC '" + bank.getBic() + "'" : "'"));
            }
        }
        catch (SQLException e){
            System.out.println("Erreur lors de l'extraction de la banque de la base de données.");
            e.printStackTrace();
            return -1;
        }
        finally{
            db.closeConnection();
        }
    }
    public Bank getBank(int bank_id) {
        String query = "SELECT name, bic FROM bank WHERE bank_id = ?";
        DatabaseConnection db=new DatabaseConnection();
        try (Connection conn = db.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, bank_id);
            ResultSet bankResult = preparedStatement.executeQuery();
            if (bankResult.next()) {
                Bank bank = new Bank(bankResult.getString("name"), bankResult.getString("bic"));
                return bank;
            }
            return null;
        }
        catch(SQLException e){
            System.out.println("Erreur lors de l'extraction de la banque de la base de données.");
            e.printStackTrace();
            return null;
        }
        finally{
            db.closeConnection();
        }
    }
}


