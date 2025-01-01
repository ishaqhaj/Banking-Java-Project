package com.Application.dao.impl;

import com.Application.dao.impl.AccountDAOImpl;
import com.Application.dao.impl.UserDAOImpl;
import com.Application.model.Account;
import com.Application.model.Bank;
import com.Application.model.User;
import com.Application.model.Address;
import com.Application.util.DatabaseConnection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AccountDAOImplTest {
    private AccountDAOImpl accountDAO;
    private UserDAOImpl userDAO;
    private Address address;
    private Bank testBank;
    private Account testAccount;
    private User testUser;

    @BeforeEach
    public void setUp() {
        accountDAO = new AccountDAOImpl();
        userDAO = new UserDAOImpl();
        testBank = new Bank("CIH", "CIHMMAMC");
        testAccount = new Account("XK051212012345678906", "CACC", testBank);
        address=new Address("Washington city build.14", "Washington", "10000", "US");
        testUser = new User("AB78899", "AX6578", "Albert Roben", "rtyyu234@", "rober@gmail.com",address , testAccount);
        testAccount.setOwner(testUser);
        userDAO.insertUser(testUser);
    }

    @Test
    void insertAccountTest() {

        // Insertion de l'account
        accountDAO.insertAccount(testAccount);

        // Vérification de l'insertion dans la base de données
        String query = "SELECT account_number, account_type FROM accounts WHERE account_number = ?";
        DatabaseConnection db = new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, testAccount.getAccountNumber());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String accountNumber = resultSet.getString("account_number");
                String accountType = resultSet.getString("account_type");

                // Assertions pour vérifier les données
                assertEquals(testAccount.getAccountNumber(), accountNumber);
                assertEquals(testAccount.getAccountType(), accountType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
    }

    @Test
    void FindUserIdByAccountNumberTest() {
        accountDAO.insertAccount(testAccount);
        String userId = accountDAO.findUserIdByAccountNumber(testAccount.getAccountNumber());

        // Assertions pour valider le résultat
        assertNotNull(userId, "L'identifiant de l'utilisateur ne doit pas être nul.");
        assertEquals(testUser.getUserId(), userId, "L'identifiant de l'utilisateur retourné doit correspondre à l'identifiant inséré.");
    }

    @Test
    void getAccountTest() {
        // Insertion du compte
        accountDAO.insertAccount(testAccount);

        // Récupération du compte avec getAccount
        Account retrievedAccount = accountDAO.getAccount(testAccount.getAccountNumber());

        // Vérifications
        assertNotNull(retrievedAccount, "Le compte récupéré ne doit pas être nul.");
        assertEquals(testAccount.getAccountNumber(), retrievedAccount.getAccountNumber(), "Le numéro de compte doit correspondre.");
        assertEquals(testAccount.getAccountType(), retrievedAccount.getAccountType(), "Le type de compte doit correspondre.");
        assertNotNull(retrievedAccount.getBank(), "La banque associée au compte ne doit pas être nulle.");
        assertEquals(testBank.getName(), retrievedAccount.getBank().getName(), "Le nom de la banque doit correspondre.");
        assertEquals(testBank.getBic(), retrievedAccount.getBank().getBic(), "Le code de la banque doit correspondre.");
    }

    @Test
   void hasSingleAccountTest() {
        accountDAO.insertAccount(testAccount);
        boolean result = accountDAO.hasSingleAccount(testUser.getUserId());
        assertEquals(result, true, "L'utilisateur a pour le moment un seul compte La fonction hasSingleAccount doit retourner true");
        Account testAccount2 = new Account("DE89 4203 0043 6410 2400 06", "CACC", testBank);
        testAccount2.setOwner(testUser);
        accountDAO.insertAccount(testAccount2);
        result = accountDAO.hasSingleAccount(testUser.getUserId());
        assertEquals(result, false, "L'utilisateur a pour le moment plus qu'un compte La fonction hasSingleAccount doit retourner false");
        accountDAO.deleteAccount(testAccount2);

    }

    @Test
    void getUserAccountTest() {
        accountDAO.insertAccount(testAccount);
        // Récupération du compte avec getUserAccount
        Account retrievedAccount = accountDAO.getUserAccount(testUser.getUserId());
        // Vérifications
        assertNotNull(retrievedAccount, "Le compte récupéré ne doit pas être nul.");
        assertEquals(testAccount.getAccountNumber(), retrievedAccount.getAccountNumber(), "Le numéro de compte doit correspondre.");
        assertEquals(testAccount.getAccountType(), retrievedAccount.getAccountType(), "Le type de compte doit correspondre.");
        assertNotNull(retrievedAccount.getBank(), "La banque associée au compte ne doit pas être nulle.");
        assertEquals(testBank.getName(), retrievedAccount.getBank().getName(), "Le nom de la banque doit correspondre.");
        assertEquals(testBank.getBic(), retrievedAccount.getBank().getBic(), "Le code de la banque doit correspondre.");

    }

    @Test
    void getUserAccountsTest() {
        accountDAO.insertAccount(testAccount);
        Account testAccount2 = new Account("DE89 4203 0043 6410 2400 06", "CACC", testBank);
        testAccount2.setOwner(testUser);
        accountDAO.insertAccount(testAccount2);
        // Appel de la méthode à tester
        Set<String> userAccounts = accountDAO.getUserAccounts(testUser.getUserId());

        // Assertions
        assertNotNull(userAccounts, "Le Set retourné ne doit pas être nul.");
        assertEquals(2, userAccounts.size(), "Le nombre de comptes doit être égal à 2.");

        // Vérification que les deux numéros de comptes sont présents dans le Set
        assertTrue(userAccounts.contains(testAccount.getAccountNumber()),
                "Le Set doit contenir le premier numéro de compte.");
        assertTrue(userAccounts.contains(testAccount2.getAccountNumber()),
                "Le Set doit contenir le deuxième numéro de compte.");
        accountDAO.deleteAccount(testAccount2);
    }

    @Test
    void addBeneficiaryAccountTest() {
        accountDAO.insertAccount(testAccount);
        Account testAccount2 = new Account("DE89 4203 0043 6410 2400 06", "CACC", testBank);
        testAccount2.setOwner(testUser);
        accountDAO.insertAccount(testAccount2);
        accountDAO.addBeneficiaryAccount("XK051212012345678906", "DE89 4203 0043 6410 2400 06");
        // Vérification de l'insertion dans la base de données
        String query = "SELECT user_account, beneficiary_account FROM account_beneficiaries WHERE user_account = ? and beneficiary_account = ?";
        DatabaseConnection db = new DatabaseConnection();
        try (Connection conn = db.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, "XK051212012345678906");
            preparedStatement.setString(2, "DE89 4203 0043 6410 2400 06");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String userIban = resultSet.getString("user_account");
                String beneficiaryIban = resultSet.getString("beneficiary_account");

                // Assertions pour vérifier les données
                assertEquals("XK051212012345678906", userIban);
                assertEquals("DE89 4203 0043 6410 2400 06", beneficiaryIban);
            }
            accountDAO.deleteAccountBeneficiary("XK051212012345678906", "DE89 4203 0043 6410 2400 06");
            accountDAO.deleteAccount(testAccount2);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
    }

    @Test
    void getBeneficiariesTest() {
        // Insérer le compte principal
        accountDAO.insertAccount(testAccount);

        // Créer et insérer un premier compte bénéficiaire
        Account testAccount2 = new Account("DE89 4203 0043 6410 2400 06", "CACC", testBank);
        testAccount2.setOwner(testUser);
        accountDAO.insertAccount(testAccount2);
        accountDAO.addBeneficiaryAccount(testAccount.getAccountNumber(), testAccount2.getAccountNumber());

        Account testAccount3 = new Account("AT62 2502 4070 4624 6822", "CACC", testBank);
        Address address1=new Address("Washington city build.20", "Washington", "10000", "US");
        User testUser2 = new User("BE5678", "BX3556", "Rozalita Martin", "5678889@@@@", "r.m@gmail.com",address1, testAccount3);
        userDAO.insertUser(testUser2);
        testAccount3.setOwner(testUser2);
        accountDAO.insertAccount(testAccount3);
        accountDAO.addBeneficiaryAccount(testAccount.getAccountNumber(), testAccount3.getAccountNumber());

        Map<String, String> beneficiaries = accountDAO.getBeneficiaries(testAccount.getAccountNumber());
        assertNotNull(beneficiaries, "La liste des bénéficiaires ne doit pas être nulle.");
        assertEquals(2, beneficiaries.size(), "Il doit y avoir deux bénéficiaires.");
        assertTrue(beneficiaries.containsValue(testAccount2.getAccountNumber()), "Le premier bénéficiaire doit être présent.");
        assertTrue(beneficiaries.containsValue(testAccount3.getAccountNumber()), "Le deuxième bénéficiaire doit être présent.");
        assertTrue(beneficiaries.containsKey(testUser.getName()),
                "La liste des bénéficiaires doit contenir le propriétaire du premier compte bénéficiaire.");
        // Nettoyage
        accountDAO.deleteAccountBeneficiary(testAccount.getAccountNumber(), testAccount2.getAccountNumber());
        accountDAO.deleteAccountBeneficiary(testAccount.getAccountNumber(), testAccount3.getAccountNumber());
        accountDAO.deleteAccount(testAccount2);
        accountDAO.deleteAccount(testAccount3);
        userDAO.deleteUser(testUser2);
    }

    @Test
    void isBeneficiaryExistsTest() {
        accountDAO.insertAccount(testAccount);
        Account testAccount2 = new Account("AT62 2502 4070 4624 6822", "CACC", testBank);
        Address address2=new Address("Washington city build.20", "Washington", "10000", "US");
        User testUser2 = new User("BE5678", "BX3556", "Rozalita Martin", "5678889@@@@", "r.m@gmail.com", address2, testAccount2);
        userDAO.insertUser(testUser2);
        testAccount2.setOwner(testUser2);
        accountDAO.insertAccount(testAccount2);
        accountDAO.addBeneficiaryAccount(testAccount.getAccountNumber(), testAccount2.getAccountNumber());

        boolean result = accountDAO.isBeneficiaryExists(testAccount.getAccountNumber(), testAccount2.getAccountNumber());
        assertEquals(true, result, "Le compte du bénéficiaire doit être ajouté.");

        Account testAccount3 = new Account("DE89 4203 0043 6410 2400 06", "CACC", testBank);
        testAccount3.setOwner(testUser2);
        accountDAO.insertAccount(testAccount3);

        result = accountDAO.isBeneficiaryExists(testAccount.getAccountNumber(), testAccount3.getAccountNumber());
        assertEquals(false, result, "Ce compte du bénéficiaire n'est pas encore ajouté.");

        accountDAO.deleteAccountBeneficiary(testAccount.getAccountNumber(), testAccount2.getAccountNumber());
        accountDAO.deleteAccount(testAccount2);
        accountDAO.deleteAccount(testAccount3);
        userDAO.deleteUser(testUser2);
    }

    @AfterEach
    public void tearDown() {
        // Nettoyer la base de données après le test
        accountDAO.deleteAccount(testAccount);
        userDAO.deleteUser(testUser);
    }

}
