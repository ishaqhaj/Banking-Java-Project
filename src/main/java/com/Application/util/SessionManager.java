package main.java.com.Application.util;

import main.java.com.Application.model.Account;
import main.java.com.Application.model.User;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static SessionManager instance;

    // Gestion des données de session
    private User authenticatedUser;


    // Gestion des contrôleurs
    private final Map<String, Object> controllers = new HashMap<>();

    private SessionManager() {}

    // Singleton pour obtenir l'instance unique de SessionManager
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Gestion des utilisateurs
    public void setAuthenticatedUser(User user) {
        this.authenticatedUser = user;
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }


}
