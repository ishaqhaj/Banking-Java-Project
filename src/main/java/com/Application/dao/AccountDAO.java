package com.Application.dao;

import java.util.Map;
import java.util.Set;

import com.Application.model.Account;

public interface AccountDAO {
    public void insertAccount(Account account);
    public String findUserIdByAccountNumber(String accountNumber);
    public Account getAccount(String accountNumber);
    public boolean hasSingleAccount(String userId);
    public Account getUserAccount(String userId);
    public Set<String> getUserAccounts(String userId);
    public boolean addBeneficiaryAccount(String iban,String beneficiaryIban);
    public Map<String, String> getBeneficiaries(String iban);
    public boolean isBeneficiaryExists(String iban,String beneficiayIban);
    public void deleteAccount(Account account);
    public void deleteAccountBeneficiary(String iban,String beneficiaryIban);
}
