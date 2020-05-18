package dev.sejtam.Banka.Utils.Enums;

public enum AccountType {
    klasicky("Klasicky Ucet"),
    studentsky("Studentsky Ucet");

    String name;

    AccountType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

