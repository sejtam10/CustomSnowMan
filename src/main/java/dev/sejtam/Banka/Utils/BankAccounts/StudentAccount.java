package dev.sejtam.Banka.Utils.BankAccounts;

import dev.sejtam.Banka.Utils.BankAccount;
import dev.sejtam.Banka.Utils.Enums.AccountType;

public class StudentAccount extends BankAccount {

    public StudentAccount(String name, String password) {
        super(name, password, AccountType.studentsky);
    }

}
