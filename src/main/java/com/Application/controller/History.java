package com.Application.controller;

import com.Application.dao.impl.AccountDAOImpl;
import com.Application.dao.impl.UserDAOImpl;
import com.Application.model.Account;
import com.Application.model.Virement;
import com.Application.service.VirementService;
import com.Application.util.SessionManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class History {
    @FXML
    private TableView<Virement> virementTableView;
    @FXML
    private TableColumn<Virement, String> dateColumn;
    @FXML
    private TableColumn<Virement, String> destinataireColumn;
    @FXML
    private TableColumn<Virement, String> montantColumn;
    @FXML
    private TableColumn<Virement, String> typeVirementColumn;
    @FXML
    private TableColumn<Virement, String> motifColumn;

    @FXML
    public void initialize() {
        String userId = SessionManager.getInstance().getAuthenticatedUser().getUserId();
        VirementService virementService = new VirementService();
        List<Virement> virements = virementService.getVirementsByUserId(userId);
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        UserDAOImpl userDAO = new UserDAOImpl();

        // Associer les noms des bénéficiaires
        for (Virement virement : virements) {
            String creditorAccountNumber = virement.getCreditorAccount().getAccountNumber();
            Account creditorAccount = accountDAO.getAccount(creditorAccountNumber);
            if (creditorAccount != null) {
                String creditorUserId = accountDAO.findUserIdByAccountNumber(creditorAccount.getAccountNumber());
                virement.getCreditorAccount().setOwner(userDAO.getUser(creditorUserId));
            }
        }

        // Configurer les colonnes de la TableView
        virementTableView.setItems(FXCollections.observableArrayList(virements));

        // Configuration des colonnes
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        destinataireColumn.setCellValueFactory(cellData -> {
            String name = cellData.getValue().getCreditorAccount().getOwner().getName();
            return new SimpleStringProperty(name);
        });

        // Utiliser le montant formaté
        montantColumn.setCellValueFactory(cellData -> {
            String formattedAmount = cellData.getValue().getFormattedAmount();
            return new SimpleStringProperty(formattedAmount);
        });

        typeVirementColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        motifColumn.setCellValueFactory(new PropertyValueFactory<>("motif"));
    }



}
