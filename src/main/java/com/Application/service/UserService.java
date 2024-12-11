package main.java.com.Application.service;




import main.java.com.Application.dao.impl.AccountDAOImpl;
import main.java.com.Application.dao.impl.BankDAOImpl;
import main.java.com.Application.dao.impl.UserDAOImpl;
import main.java.com.Application.model.Account;
import main.java.com.Application.model.Bank;
import main.java.com.Application.model.User;



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
            bankDAO.closeConnection();
            accountDAO.closeConnection();
            userDAO.closeConnection();
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



}
