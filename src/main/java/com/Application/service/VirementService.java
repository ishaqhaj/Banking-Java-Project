package com.Application.service;

import com.Application.dao.impl.VirementDAOImpl;
import com.Application.model.Virement;

import java.util.List;

public class VirementService {

    public void insertVirement(Virement virement) {
        VirementDAOImpl virementDAO = new VirementDAOImpl();
        virementDAO.insertVirement(virement);
    }
    public List<Virement> getVirementsByUserId(String userId) {
        VirementDAOImpl virementDAO = new VirementDAOImpl();
        return virementDAO.getVirementsByUserId(userId);
    }
    public void insertVirements(List<Virement> virements){
        VirementDAOImpl virementDAO = new VirementDAOImpl();
        virementDAO.insertVirements(virements);
    }
}
