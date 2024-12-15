package com.Application.service;




import com.Application.dao.impl.AccountDAOImpl;
import com.Application.dao.impl.BankDAOImpl;
import com.Application.dao.impl.UserDAOImpl;
import com.Application.model.Account;
import com.Application.model.Bank;
import com.Application.model.User;
import com.Application.util.SessionManager;


import java.sql.SQLException;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class UserService {
    private BankDAOImpl bankDAO;
    private AccountDAOImpl accountDAO;
    private UserDAOImpl userDAO;

    public UserService() {
        this.bankDAO=new BankDAOImpl();
        this.accountDAO=new AccountDAOImpl();
        this.userDAO = new UserDAOImpl(); // Initialisation de UserDAO
    }

    // Méthode pour créer un utilisateur
    public String createUser(String idValue, String name, String password, String email, String address, String city, String postalCode, String country, String accountNumber, String accountType, String bankName, String bic) {
        try {
            Bank bank = new Bank(bankName, bic);
            bank=bankDAO.findBank(bank);
            Account account=new Account(accountNumber,accountType,bank);
            if (userDAO.isIdExist(idValue)) {
                return "Error";
            }
            String userId = generateUniqueUserId();
            User user=new User(idValue,userId,name,password,email,address,city,postalCode,country,account);
            userDAO.insertUser(user);
            account.setOwner(user);
            accountDAO.insertAccount(account);
            return userId;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }


    // Méthode pour générer un identifiant utilisateur unique de 6 caractères
    private String generateUniqueUserId() throws SQLException {
        String userId;
        boolean isUnique;

        do {
            userId = generateRandomUserId(); // Générer un ID aléatoire
            isUnique = userDAO.isUserIdUnique(userId); // Vérifier l'unicité de l'ID via UserDAO
        } while (!isUnique); // Répéter jusqu'à obtenir un ID unique

        return userId;
    }

    // Générer un identifiant aléatoire de 6 caractères
    private String generateRandomUserId() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder userId = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            userId.append(characters.charAt(random.nextInt(characters.length())));
        }
        return userId.toString();
    }
    public boolean authenticateUser(String userId, String password) {
        // Utiliser le nouvel identifiant unique pour l'authentification
        return userDAO.authenticateUser(userId, password);
    }
    public boolean addBeneficiary(String userId, String beneficiaryName, String accountNumber)  {
        // Trouver l'ID utilisateur correspondant au numéro de compte
        String beneficiaryId = accountDAO.findUserIdByAccountNumber(accountNumber);
        if (beneficiaryId == null) {
            throw new IllegalArgumentException("Aucun utilisateur trouvé avec ce numéro de compte.");
        }

        // Vérifier que les noms correspondent
        String fetchedName = userDAO.getUserNameById(beneficiaryId);
        if (!fetchedName.equals(beneficiaryName)) {
            throw new IllegalArgumentException("Le nom ne correspond pas au compte donné.");
        }
        Account SelectedAccount= SessionManager.getInstance().getSelectedAccount();
        // Ajouter le bénéficiaire
        return accountDAO.addBeneficiaryAccount(SelectedAccount.getAccountNumber(),accountNumber);
    }
    public Map<String, String> getUserBeneficiaries(String iban) {
        return accountDAO.getBeneficiaries(iban);
    }
    public Set<String> getUserAccounts(String userId){
        return accountDAO.getUserAccounts(userId);
    }
}
