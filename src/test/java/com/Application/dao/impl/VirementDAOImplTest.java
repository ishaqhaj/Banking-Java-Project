package com.Application.dao.impl;

import com.Application.dao.impl.AccountDAOImpl;
import com.Application.dao.impl.UserDAOImpl;
import com.Application.dao.impl.VirementDAOImpl;
import com.Application.model.Account;
import com.Application.model.Address;
import com.Application.model.Bank;
import com.Application.model.User;
import com.Application.model.Virement;
import com.Application.util.DatabaseConnection;
import com.Application.util.SessionManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VirementDAOImplTest {
    private VirementDAOImpl virementDAO;
    private Virement virement;
    UserDAOImpl userDAO;
    AccountDAOImpl accountDAO;
    Account testAccount;
    User testUser;
    Account testAccount2;
    User testUser2;
    @BeforeEach
    public void setUp(){
        virementDAO = new VirementDAOImpl();
        userDAO = new UserDAOImpl();
        accountDAO = new AccountDAOImpl();
        Bank testBank = new Bank("CIH", "CIHMMAMC");
        testAccount = new Account("XK051212012345678906", "CACC", testBank);
        Address address=new Address("Washington city build.14", "Washington", "10000", "US");
        testUser = new User("AB78899", "AX6578", "Albert Roben", "rtyyu234@", "rober@gmail.com",address , testAccount);
        testAccount.setOwner(testUser);
        userDAO.insertUser(testUser);
        accountDAO.insertAccount(testAccount);
        SessionManager.getInstance().setSelectedAccount(testAccount);
        testAccount2 = new Account("AT62 2502 4070 4624 6822", "CACC", testBank);
        Address address2=new Address("Washington city build.20", "Washington", "10000", "US");
        testUser2 = new User("BE5678", "BX3556", "Rozalita Martin", "5678889@@@@", "r.m@gmail.com", address2, testAccount2);
        userDAO.insertUser(testUser2);
        testAccount2.setOwner(testUser2);
        accountDAO.insertAccount(testAccount2);
        SessionManager.getInstance().setSelectedAccountBeneficiary(testAccount2);
        virement=new Virement(new BigDecimal(13344),"MAD","Test avec JUnit","simple","SEPA");
        assertNotNull(SessionManager.getInstance().getSelectedAccount(), "Debtor account is not set.");
        assertNotNull(SessionManager.getInstance().getSelectedAccountBeneficiary(), "Creditor account is not set.");
    }

    @Test
    void isEndToEndIdUniqueTest(){
        virementDAO.insertVirement(virement);
        boolean unique=virementDAO.isEndToEndIdUnique(virement.getEndToEndId());
        assertEquals(false,unique,"Cet end_to_end n'est pas unique car il est déjà inséré a la base de données.");

        unique=virementDAO.isEndToEndIdUnique("trtyyyy");
        assertEquals(true,unique,"Cet end_to_end est unique car le format de end_to_end commence toujours par une date et de taille supérieure à 6");
    }

    @Test
    void insertVirementTest(){
        virementDAO.insertVirement(virement);
        String query = "SELECT debtor_account_number, creditor_account_number, amount, currency, timestamp, motif, type, methode_paiement FROM virement WHERE end_to_end_id = ?";
        DatabaseConnection db = new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, virement.getEndToEndId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String retrievedDebtorAccountNumber = resultSet.getString("debtor_account_number");
                String retrievedCreditorAccountNumber = resultSet.getString("creditor_account_number");
                BigDecimal retrievedAmount = resultSet.getBigDecimal("amount");
                String retrievedCurrency = resultSet.getString("currency");
                String retrievedTimestamp = resultSet.getString("timestamp");
                String retrievedMotif = resultSet.getString("motif");
                String retrievedType = resultSet.getString("type");
                String retrievedPaymentMethod = resultSet.getString("methode_paiement");
                // Assertions pour vérifier les données
                assertEquals(virement.getDebtorAccount().getAccountNumber(), retrievedDebtorAccountNumber);
                assertEquals(virement.getCreditorAccount().getAccountNumber(), retrievedCreditorAccountNumber);
                assertEquals(0, virement.getAmount().compareTo(retrievedAmount), "Les montants ne correspondent pas.");
                assertEquals(virement.getCurrency(), retrievedCurrency);
                assertEquals(virement.getTimestamp(), retrievedTimestamp);
                assertEquals(virement.getMotif(), retrievedMotif);
                assertEquals(virement.getType(), retrievedType);
                assertEquals(virement.getPayMethod(), retrievedPaymentMethod);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
    }

    @Test
    void insertVirements(){
        List<Virement> virements=new ArrayList<Virement>();
        virements.add(virement);
        assertNotNull(SessionManager.getInstance().getSelectedAccount(), "Debtor account is not set.");
        assertNotNull(SessionManager.getInstance().getSelectedAccountBeneficiary(), "Creditor account is not set.");
        Virement virement2=new Virement(new BigDecimal(5000),"USD","le deuxième exemplaire","simple","SEPA");
        virements.add(virement2);
        virementDAO.insertVirements(virements);
        String query = "SELECT debtor_account_number, creditor_account_number, amount, currency, timestamp, motif, type, methode_paiement FROM virement WHERE end_to_end_id = ? OR end_to_end_id = ?";
        DatabaseConnection db = new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, virement.getEndToEndId());
            preparedStatement.setString(2, virement2.getEndToEndId());
            ResultSet resultSet = preparedStatement.executeQuery();
            int compteur=0;
            while (resultSet.next()) {
                compteur++;
                String retrievedDebtorAccountNumber = resultSet.getString("debtor_account_number");
                String retrievedCreditorAccountNumber = resultSet.getString("creditor_account_number");
                BigDecimal retrievedAmount = resultSet.getBigDecimal("amount");
                String retrievedCurrency = resultSet.getString("currency");
                String retrievedTimestamp = resultSet.getString("timestamp");
                String retrievedMotif = resultSet.getString("motif");
                String retrievedType = resultSet.getString("type");
                String retrievedPaymentMethod = resultSet.getString("methode_paiement");
                // Assertions pour vérifier les données
                assertTrue(
                        retrievedDebtorAccountNumber.equals(virement.getDebtorAccount().getAccountNumber()) ||
                                retrievedDebtorAccountNumber.equals(virement2.getDebtorAccount().getAccountNumber()),
                        "retrievedDebtor_account_number doit correspondre à l'un des deux numéros de compte."
                );
                assertTrue(
                        retrievedCreditorAccountNumber.equals(virement.getCreditorAccount().getAccountNumber()) ||
                                retrievedCreditorAccountNumber.equals(virement2.getCreditorAccount().getAccountNumber())
                );

                assertTrue(
                        retrievedAmount.compareTo(virement.getAmount()) == 0 ||
                                retrievedAmount.compareTo(virement2.getAmount()) == 0,
                        "retrievedAmount doit correspondre à l'un des montants des virements."
                );
                assertTrue(
                        retrievedCurrency.equals(virement.getCurrency()) ||
                                retrievedCurrency.equals(virement2.getCurrency())
                );
                assertTrue(
                        retrievedTimestamp.equals(virement.getTimestamp()) ||
                                retrievedTimestamp.equals(virement2.getTimestamp())
                );
                assertTrue(
                        retrievedMotif.equals(virement.getMotif()) ||
                                retrievedMotif.equals(virement2.getMotif())
                );
                assertTrue(
                        retrievedType.equals(virement.getType()) ||
                                retrievedType.equals(virement2.getType())
                );
                assertTrue(
                        retrievedPaymentMethod.equals(virement.getPayMethod()) ||
                                retrievedTimestamp.equals(virement2.getPayMethod())
                );
            }
            assertEquals(2,compteur,"On doit trouver les deux virements insérés dans la base de données.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        virementDAO.deleteVirement(virement2.getEndToEndId());
    }

    @Test
    void getVirementsByUserIdTest(){
        Virement virement2=new Virement(new BigDecimal(5000),"USD","le deuxième exemplaire","simple","SEPA");
        virementDAO.insertVirement(virement);
        virementDAO.insertVirement(virement2);
        List<Virement> virements=virementDAO.getVirementsByUserId(testUser.getUserId());
        for( Virement virementElement : virements){
            assertTrue(
                    virementElement.getCreditorAccount().getAccountNumber().equals(virement.getCreditorAccount().getAccountNumber()) ||
                            virementElement.getCreditorAccount().getAccountNumber().equals(virement2.getCreditorAccount().getAccountNumber())
            );

            assertTrue(
                    virementElement.getAmount().compareTo(virement.getAmount()) == 0 ||
                            virementElement.getAmount().compareTo(virement2.getAmount()) == 0,
                    "retrievedAmount doit correspondre à l'un des montants des virements."
            );
            assertTrue(
                    virementElement.getCurrency().equals(virement.getCurrency()) ||
                            virementElement.getCurrency().equals(virement2.getCurrency())
            );
            assertTrue(
                    virementElement.getTimestamp().equals(virement.getTimestamp()) ||
                            virementElement.getTimestamp().equals(virement2.getTimestamp())
            );
            assertTrue(
                    virementElement.getMotif().equals(virement.getMotif()) ||
                            virementElement.getMotif().equals(virement2.getMotif())
            );
            assertTrue(
                    virementElement.getType().equals(virement.getType()) ||
                            virementElement.getType().equals(virement2.getType())
            );
            assertTrue(
                    virementElement.getPayMethod().equals(virement.getPayMethod()) ||
                            virementElement.getPayMethod().equals(virement2.getPayMethod())
            );
        }
        assertEquals(2,virements.size(),"La taille de la liste de virements effectués avec cet utilisateur doit être égale à 2.");
        virementDAO.deleteVirement(virement2.getEndToEndId());
    }


    @AfterEach
    public void tearDown(){
        virementDAO.deleteVirement(virement.getEndToEndId());
        accountDAO.deleteAccount(testAccount);
        userDAO.deleteUser(testUser);
        accountDAO.deleteAccount(testAccount2);
        userDAO.deleteUser(testUser2);
    }

}
