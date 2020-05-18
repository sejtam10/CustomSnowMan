package dev.sejtam.Banka.Utils.BankAccounts;

import dev.sejtam.Banka.Utils.BankAccount;
import dev.sejtam.Banka.Utils.Enums.AccountType;

public class ClassicAccount extends BankAccount {

    //Poplatek při odeslání peněz
    @Override
    public float poplatekO() { return 2.5f; }
    //Poplatek při přijmu peněz
    @Override
    public float poplatekP() { return 0.5f; }

    public ClassicAccount(String name, String password) {
        super(name, password, AccountType.klasicky);
    }

    public ClassicAccount(int id, String name, String password, int money, AccountType accountType, String player) {
        super(id, name, password, money, accountType, player);
    }
}
