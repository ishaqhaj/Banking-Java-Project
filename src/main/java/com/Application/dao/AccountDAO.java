package com.Application.dao;

import com.Application.model.Account;

import java.util.Map;
import java.util.Set;

public interface AccountDAO {
    public void insertAccount(Account account);
    public String findUserIdByAccountNumber(String accountNumber);
    public Account getAccount(String accountNumber);
    public boolean hasSingleAccount(String userId);
    public Account getUserAccount(String userId);
    public Set<String> getUserAccounts(String userId);
    public boolean addBeneficiaryAccount(String iban,String beneficiaryIban);
}
