package com.Application.service;

import com.Application.dao.impl.VirementDAOImpl;
import com.Application.model.Virement;

public class VirementService {

    public void insertVirement(Virement virement) {
        VirementDAOImpl virementDAO = new VirementDAOImpl();
        virementDAO.insertVirement(virement);
    }
}
