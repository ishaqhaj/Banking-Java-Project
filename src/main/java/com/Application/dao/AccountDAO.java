package com.Application.dao;

import com.Application.model.Account;

import java.util.Set;

public interface AccountDAO {
    public void insertAccount(Account account);
    public String findUserIdByAccountNumber(String accountNumber);
    public Account getAccount(String accountNumber);
}
