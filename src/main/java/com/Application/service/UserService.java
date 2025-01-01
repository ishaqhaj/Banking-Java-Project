package com.Application.service;

import com.Application.dao.impl.AccountDAOImpl;
import com.Application.dao.impl.BankDAOImpl;
import com.Application.dao.impl.UserDAOImpl;
import com.Application.model.Account;
import com.Application.model.Address;
import com.Application.model.User;
import com.Application.util.SessionManager;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserService {
	private static final SecureRandom RANDOM = new SecureRandom();
    private BankDAOImpl bankDAO;
    private AccountDAOImpl accountDAO;
    private UserDAOImpl userDAO;
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public UserService() {
        this.bankDAO=new BankDAOImpl();
        this.accountDAO=new AccountDAOImpl();
        this.userDAO = new UserDAOImpl(); // Initialisation de UserDAO
    }

    // Méthode pour créer un utilisateur
    public String createUser(String idValue, String name, String password, String email, Address address, Account account) {
        try {
            account.setBank(bankDAO.findBank(account.getBank())); 
            if (userDAO.isIdExist(idValue)) {
                return "Error";
            }
            String userId = generateUniqueUserId();
            User user=new User(idValue,userId,name,password,email,address,account);
            userDAO.insertUser(user);
            account.setOwner(user);
            accountDAO.insertAccount(account);
            return userId;

        } catch (Exception e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de la création de l'utilisateur.", e);
            return "Error";
        }
    }


    // Méthode pour générer un identifiant utilisateur unique de 6 caractères
    private String generateUniqueUserId(){
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
        StringBuilder userId = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            userId.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return userId.toString();
    }
    public boolean authenticateUser(String userId, String password) {
        // Utiliser le nouvel identifiant unique pour l'authentification
        return userDAO.authenticateUser(userId, password);
    }
    public boolean addBeneficiary(String beneficiaryName, String accountNumber)  {
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
        Account selectedAccount= SessionManager.getInstance().getSelectedAccount();
        // Ajouter le bénéficiaire
        return accountDAO.addBeneficiaryAccount(selectedAccount.getAccountNumber(),accountNumber);
    }
    public Map<String, String> getUserBeneficiaries(String iban) {
        return accountDAO.getBeneficiaries(iban);
    }
    public Set<String> getUserAccounts(String userId){
        return accountDAO.getUserAccounts(userId);
    }
    public boolean isBeneficiaryExists(String iban,String beneficiayIban){
        return accountDAO.isBeneficiaryExists(iban, beneficiayIban);
    }
}
