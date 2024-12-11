package main.java.com.Application.dao;

import main.java.com.Application.model.Bank;

import java.sql.SQLException;

public interface BankDAO {
    public Bank findBank(Bank bank);
    public Integer getBankId(Bank bank) throws SQLException;
    public Bank getBank(int bank_id) throws SQLException;
}
