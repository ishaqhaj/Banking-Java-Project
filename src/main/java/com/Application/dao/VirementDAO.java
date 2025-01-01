package com.Application.dao;

import com.Application.model.Virement;

public interface VirementDAO {

    /**
     * Vérifie si un identifiant End-to-End est unique dans la base de données.
     *
     * @param endToEndId l'identifiant End-to-End à vérifier
     * @return true si l'identifiant est unique, false sinon
     */
    boolean isEndToEndIdUnique(String endToEndId);

    /**
     * Insère un virement dans la base de données.
     *
     * @param virement l'objet Virement à insérer
     */
    void insertVirement(Virement virement);
    public void deleteVirement(String endToEnd);
}
