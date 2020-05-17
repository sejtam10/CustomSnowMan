package dev.sejtam.Banka.Utils;

import dev.sejtam.Banka.Banka;
import dev.sejtam.Banka.Utils.Enums.AccountType;
import dev.sejtam.Banka.Utils.Enums.ActionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    int id;
    String name;
    String password;
    int money;
    AccountType accountType;

    //Poplatek při odeslání peněz
    public float poplatekO() { return 0f; }
    //Poplatek při přijmu peněz
    public float poplatekP() { return 0f; }
    //Poplatek při převodu peněz
    public float poplatekU() { return 0f; }

    public BankAccount(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public BankAccount(String name, String password, int money, AccountType accountType) {
        this.name = name;
        this.password = password;
        this.money = money;
        this.accountType = accountType;
        createAccount();
    }
    public BankAccount(String name, String password, AccountType accountType) {
        this(name, password, 0, accountType);
    }

    //MySQL Management
    public boolean createAccount() {
        try {
            if(Banka.getConnection() == null)
                return false;

            if(Banka.getConnection().isClosed())
                return false;

            ResultSet resultSet = getAccount();
            if(resultSet.next()) {
                this.id = resultSet.getInt("id");
                this.name = resultSet.getString("name");
                this.password = resultSet.getString("password");
                this.accountType = AccountType.valueOf(resultSet.getString("type"));
                this.money = resultSet.getInt("money");
                return true;
            }

            Statement statement = Banka.getConnection().createStatement();
            statement.execute(String.format("INSERT INTO banka.ucty (name, password, type, money) VALUES (%s, %s, %s, %s)",
                                            this.name, this.password, this.accountType.toString(), this.money));

            resultSet = statement.executeQuery("SELECT t.id FROM banka.ucty");
            if(resultSet.next()) {
                this.id = resultSet.getInt("id");
                return true;
            }

            return false;
        } catch (Exception ex) { return false; }
    }
    public boolean removeAccount() {
        try {
            if (Banka.getConnection() == null)
                return false;

            if (Banka.getConnection().isClosed())
                return false;

            Statement statement = Banka.getConnection().createStatement();
            if(isAccountExists()) {
                statement.execute("DELETE FROM banka.ucty WHERE name = '" + this.name + "' AND password = '" + this.password + "'");
                return true;
            } else return false;
        } catch(SQLException ex) {
            return false;
        }
    }
    public boolean isAccountExists() {
        try {
            return getAccount().next();
        } catch(SQLException ex) {
            return false;
        }
    }
    public ResultSet getAccount() {
        try {
            if (Banka.getConnection() == null)
                return null;

            if (Banka.getConnection().isClosed())
                return null;

            Statement statement = Banka.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT t.* FROM banka.ucty t WHERE t.name = '" + this.name + "' AND t.password = '" + this.password + "'");
            if (result.next()) {
                return result;
            }
            return null;
        } catch (Exception ex) { return null; }
    }

    private boolean createLog(BankAccount.Log log) {
        return createLog(log.type, log.money);
    }
    public boolean createLog(ActionType action, int money) {
        try {
            if (Banka.getConnection() == null)
                return false;

            if (Banka.getConnection().isClosed())
                return false;

            Statement statement = Banka.getConnection().createStatement();
            statement.execute(String.format("INSERT INTO banka.logs (id, name, type, money) VALUES (%s, %s, %s, %s)", this.id, this.name, action, money));
            return true;
        } catch(SQLException ex) {
            return false;
        }
    }
    public List<BankAccount.Log> getLogs() {
        try {
            if (Banka.getConnection() == null)
                return null;

            if (Banka.getConnection().isClosed())
                return null;

            Statement statement = Banka.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT t.* FROM banka.logs t WHERE t.name = '" + this.name + "' AND t.id = '" + this.id + "'");
            List<BankAccount.Log> logs = new ArrayList<>();
            while(resultSet.next()) {
                logs.add(new BankAccount.Log(resultSet.getTimestamp("time"),
                                ActionType.valueOf(resultSet.getString("type")),
                                resultSet.getInt("money")));
            }

            if(logs.isEmpty())
                return null;

            return logs;
        } catch (SQLException ex) {
            return null;
        }
    }

    //Money Management
    public int getMoney() {
        ResultSet resultSet = getAccount();
        if(resultSet == null)
            return -1;

        try {
            return this.money = resultSet.getInt("money");
        } catch (Exception ex) { return -1; }
    }
    private boolean setMoney(int money) {
        try {
            if (Banka.getConnection() == null)
                return false;

            if (Banka.getConnection().isClosed())
                return false;

            Statement statement = Banka.getConnection().createStatement();
            statement.execute("INSERT INTO banka.ucty t (t.money) VALUES (" + money + ") WHERE t.name = '" + this.name + "'");
            return true;
        } catch (SQLException ex) { return false; }
    }

    private int addMoney(int money) {
        return addMoney(money, false);
    }
    private int addMoney(int money, boolean transaction) {
        int _money = getMoney();
        if(_money == -1)
            return -1;

        if(poplatekP() != 0 && transaction == false) {
            int money_cache = money;
            money = (int) (money * (100 - poplatekO()) / 100);
            createLog(ActionType.poplatek, money_cache - money);
        }

        if(setMoney(_money + money))
            return money;
        else return -1;
    }

    private int removeMoney(int money) {
        return removeMoney(money, true);
    }
    private int removeMoney(int money, boolean transaction) {
        int _money = getMoney();
        if(_money == -1)
            return -1;

        if(poplatekO() != 0 && transaction == false) {
            int money_cache = money;
            money = (int) (money * (100 + poplatekO()) / 100);
            createLog(ActionType.poplatek, money - money_cache);
        }

        int remove = _money - money;
        if(remove < 0)
            return -1;

        if(setMoney(remove))
            return money;
        else return -1;
    }

    public void action(ActionType action, int money, boolean ... prevod) {
        switch(action) {
            case vklad:
                addMoney(money);
                break;
            case vyber:
                removeMoney(money);
                break;
            case prevod:
                if(prevod[0])
                    removeMoney(money, true);
                else addMoney(money, true);
                break;
        }
        createLog(action, money);
    }

    public class Log {
        public Timestamp time;
        public ActionType type;
        public int money;

        public Log(Timestamp time, ActionType type, int money) {
            this.time = time;
            this.type = type;
            this.money = money;
        }
        public Log(ActionType type, int money) {
            this.type = type;
            this.money = money;
        }
    }
}
