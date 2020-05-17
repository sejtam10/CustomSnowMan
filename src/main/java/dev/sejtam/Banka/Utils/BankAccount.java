package dev.sejtam.Banka.Utils;

import dev.sejtam.Banka.Banka;
import dev.sejtam.Banka.Utils.BankAccounts.ClassicAccount;
import dev.sejtam.Banka.Utils.BankAccounts.StudentAccount;
import dev.sejtam.Banka.Utils.Enums.AccountType;
import dev.sejtam.Banka.Utils.Enums.ActionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class BankAccount {

    int id;
    String name;
    String password;
    int money;
    AccountType accountType;


    public String getName() {
        return name;
    }

    //Poplatek při odeslání peněz
    public float poplatekO() { return 0f; }
    //Poplatek při přijmu peněz
    public float poplatekP() { return 0f; }
    //Poplatek při převodu peněz
    public float poplatekU() { return 0f; }

    public BankAccount(String name) {
        this.name = name;
    }
    public BankAccount(String name, String password) {
        this.name = name;
        this.password = Base64.getEncoder().encodeToString(password.getBytes());
        this.accountType = AccountType.klasicky;
    }
    public BankAccount(String name, String password, int money, AccountType accountType) {
        this.name = name;
        this.password = Base64.getEncoder().encodeToString(password.getBytes());
        this.money = money;
        this.accountType = accountType;
    }
    public BankAccount(int id, String name, String password, int money, AccountType accountType) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.money = money;
        this.accountType = accountType;
    }
    public BankAccount(String name, String password, AccountType accountType) {
        this(name, password, 0, accountType);
    }

    //MySQL Management
    public BankAccount createAccount() {
        try {
            if(Banka.getConnection() == null)
                return null;

            if(Banka.getConnection().isClosed())
                return null;

            ResultSet resultSet = getAccount();
            if(resultSet != null) {
                this.id = resultSet.getInt("id");
                this.name = resultSet.getString("name");
                this.password = resultSet.getString("password");
                this.accountType = AccountType.valueOf(resultSet.getString("type"));
                this.money = resultSet.getInt("money");

                switch(this.accountType) {
                    case klasicky:
                        return new ClassicAccount(this.id, this.name, this.password, this.money, this.accountType);
                    case studentsky:
                        return new StudentAccount(this.id, this.name, this.password, this.money, this.accountType);
                    default:
                        return new BankAccount(this.id, this.name, this.password, this.money, this.accountType);
                }
            }

            Statement statement = Banka.getConnection().createStatement();
            statement.execute(String.format("INSERT INTO banka.ucty (name, password, type, money) VALUES ('%s', '%s', '%s', '%s')",
                                            this.name, this.password, this.accountType.toString(), this.money));

            resultSet = statement.executeQuery("SELECT t.id FROM banka.ucty t WHERE t.name = '" + this.name + "'");
            if(resultSet.next()) {
                this.id = resultSet.getInt("id");
                return this;
            }

            return null;
        } catch (SQLException ex) { ex.printStackTrace(); return null; }
    }
    public boolean isPasswordSame() {
        ResultSet resultSet = getAccount();
        if(resultSet == null) {
            return false;
        }

        try {
            String password = resultSet.getString("password");
            return this.password.equals(password);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
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
       return getAccount() != null;
    }
    public ResultSet getAccount() {
        try {
            if (Banka.getConnection() == null)
                return null;

            if (Banka.getConnection().isClosed())
                return null;

            Statement statement = Banka.getConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT t.* FROM banka.ucty t WHERE t.name = '" + this.name + "'");
            if (result.next()) {
                return result;
            }
            return null;
        } catch (SQLException ex) { return null; }
    }

    //Log Management
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
            statement.execute(String.format("INSERT INTO banka.logs (id, name, type, money) VALUES ('%s', '%s', '%s', '%s')", this.id, this.name, action, money));
            return true;
        } catch(SQLException ex) {
            ex.printStackTrace();
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
        } catch (SQLException ex) { return -1; }
    }
    private boolean setMoney(int money) {
        try {
            if (Banka.getConnection() == null)
                return false;

            if (Banka.getConnection().isClosed())
                return false;

            Statement statement = Banka.getConnection().createStatement();
            statement.execute("UPDATE banka.ucty SET money = '" + money + "' WHERE name = '" + this.name + "'");
            return true;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
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
            money = (int) (money * (100 - poplatekP()) / 100);
            createLog(ActionType.poplatek, money_cache - money);
        }

        if(setMoney(_money + money))
            return money;
        else return -1;
    }

    private int removeMoney(int money) {
        return removeMoney(money, false);
    }
    private int removeMoney(int money, boolean transaction) {
        int _money = getMoney();
        if(_money == -1)
            return -1;

        int money_cache = money;
        if(poplatekO() != 0 && transaction == false) {
            money = (int) (money * (100 + poplatekO()) / 100);
            createLog(ActionType.poplatek, money - money_cache);
        } else if(poplatekU() != 0 && transaction == true) {
            money = (int) (money * (100 + poplatekU()) / 100);
            createLog(ActionType.poplatek, money_cache - money);
        }

        int remove = _money - money;
        if(remove < 0)
            return -1;

        if(setMoney(remove))
            return (money_cache - (money - money_cache));
        else return -1;
    }

    public int action(ActionType action, int money, boolean ... prevod) {
        int number = -1;
        switch(action) {
            case vklad:
                number = addMoney(money);
                break;
            case vyber:
                number = removeMoney(money);
                break;
            case prevod:
                if(prevod[0])
                    number = removeMoney(money, true);
                else number = addMoney(money, true);
                break;
        }
        createLog(action, money);
        return number;
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
