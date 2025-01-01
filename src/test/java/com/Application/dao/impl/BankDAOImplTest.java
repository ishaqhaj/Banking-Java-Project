package com.Application.dao.impl;


import com.Application.dao.impl.BankDAOImpl;
import com.Application.model.Bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankDAOImplTest {
    private BankDAOImpl bankDAO;
    Bank bank;
    @BeforeEach
    public void setUp() {
       bankDAO = new BankDAOImpl();
       bank=new Bank("CIH","CIHMMAMC");
    }
    @Test
    void findBank(){
        Bank bankResult= bankDAO.findBank(bank);
        assertEquals("CIH", bankResult.getName(), "Le nom de banques doit se correspondre");
        assertEquals("CIHMMAMC", bankResult.getBic(), "Le BIC de banques doit correspondre.");

        Bank bank2 = new Bank("CIH", "INVALIDBIC");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bankDAO.findBank(bank2);
        });
        assertEquals("Le BIC INVALIDBIC ne correspond pas à la banque CIH", exception.getMessage());

        Bank bank3 = new Bank("InvalidBank", "CIHMMAMC");
       exception = assertThrows(IllegalArgumentException.class, () -> {
            bankDAO.findBank(bank3);
        });
        assertEquals("Le nom de la banque InvalidBank ne correspond pas au BIC CIHMMAMC", exception.getMessage());

        Bank bank4 = new Bank("NonExistentBank", "NONEXISTENTBIC");
        exception = assertThrows(IllegalArgumentException.class, () -> {
            bankDAO.findBank(bank4);
        });
        assertEquals("La banque NonExistentBank n'existe pas.", exception.getMessage());
    }

    @Test
    void getBankIdTest(){
        int id=bankDAO.getBankId(bank);
        assertEquals(1, id,"Le id de la banque extrait doit se correspondre à l'id actuel de la banque.");
    }

    @Test
    void getBankTest(){
        Bank bankResult= bankDAO.getBank(1);
        assertEquals("CIH", bankResult.getName(), "Le nom de banques doit se correspondre");
        assertEquals("CIHMMAMC", bankResult.getBic(), "Le BIC de banques doit correspondre.");
    }
}
