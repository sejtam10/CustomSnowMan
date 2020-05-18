package dev.sejtam.Banka.Utils.BankAccounts;

import dev.sejtam.Banka.Utils.BankAccount;
import dev.sejtam.Banka.Utils.Enums.AccountType;

public class StudentAccount extends BankAccount {

    public StudentAccount(String name, String password) {
        super(name, password, AccountType.studentsky);
    }

    public StudentAccount(int id, String name, String password, int money, AccountType accountType) {
        super(id, name, password, money, accountType);
    }

    public StudentAccount(int id, String name, String password, int money, AccountType accountType, String player) {
        super(id, name, password, money, accountType, player);
    }

}
