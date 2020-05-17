package dev.sejtam.Banka.Commands;

import dev.sejtam.Banka.Utils.BankAccount;
import dev.sejtam.Banka.Utils.Enums.AccountType;
import dev.sejtam.Banka.Utils.Enums.ActionType;
import dev.sejtam.Banka.Utils.Log;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class Bank implements CommandExecutor {

    Map<String, BankAccount> accounts = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            sendHelp(sender);
            return true;
        }

        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?") ) {
                sendHelp(sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("logout")) {
                if(accounts.containsKey(sender.getName())) {
                    accounts.remove(sender.getName());
                    Log.log(sender, "Byl jsi odhlasen!");
                } else Log.error(sender, "Nejsi prihlasen!");
                return true;
            }
        }

        if(args.length >= 2) {
            if(args[0].equalsIgnoreCase("vyber")) {
                if(!accounts.containsKey(sender.getName())) {
                    Log.error(sender, "Nejsi prihlasen na zadnem ucte");
                    return false;
                }

                int cislo = -1;
                try {
                    cislo = Integer.parseInt(args[1]);
                } catch(NumberFormatException ex) {
                    Log.error(sender, "Castka neni cislo");
                    return false;
                }

                if(cislo <= 0) {
                    Log.error(sender, "Castka nemuze byt menci jak 1");
                    return false;
                }

                BankAccount account = accounts.get(sender.getName());
                int vyber = account.action(ActionType.vyber, cislo);
                if(vyber == -1)
                    Log.error(sender, "Nemas dostatek penez nebo se vyber nepovedl");
                else Log.log(sender, "Vyber " + vyber + " Kc probehl v poradku");
                return true;
            }

            if(args[0].equalsIgnoreCase("vklad")) {
                if(!accounts.containsKey(sender.getName())) {
                    Log.error(sender, "Nejsi prihlasen na zadnem ucte");
                    return false;
                }

                int cislo = -1;
                try {
                    cislo = Integer.parseInt(args[1]);
                } catch(NumberFormatException ex) {
                    Log.error(sender, "Castka neni cislo");
                    return false;
                }

                if(cislo <= 0) {
                    Log.error(sender, "Castka nemuze byt menci jak 1");
                    return false;
                }

                BankAccount account = accounts.get(sender.getName());
                int vklad = account.action(ActionType.vklad, cislo);
                if(vklad == -1)
                    Log.error(sender, "Nemas dostatek penez nebo se vklad nepovedl");
                else Log.log(sender, "Vklad " + vklad + " Kc probehl v poradku");
                return true;
            }
        }

        if(args.length >= 3) {
            if(args[0].equalsIgnoreCase("login")) {
                if(accounts.containsKey(sender.getName())) {
                    Log.error(sender, "Uz jsi prihlasen");
                    return false;
                }

                if(accounts.containsValue(args[1])) {
                    Log.error(sender, "Na tomto ucte je jiz nekdo prihlasen");
                    return false;
                }

                BankAccount account = new BankAccount(args[1], args[2]);
                if(!account.isAccountExists()) {
                    Log.error(sender, "Tento ucet neexistuje");
                    return false;
                } else {
                    if(!account.isPasswordSame()) {
                        Log.error(sender, "Heslo neni spravne");
                        return false;
                    }

                    accounts.put(sender.getName(), account.createAccount());
                    Log.log(sender, "Byl jsi prihlasen");
                }
                return true;
            }
        }

        if(args.length >= 4) {
            if(args[0].equalsIgnoreCase("create")) {
                AccountType type = AccountType.valueOf(args[3]);
                if(type == null) {
                    Log.error(sender, "Tento typ uctu neexistuje");
                    return false;
                }

                BankAccount account = new BankAccount(args[1], args[2], type);
                if(account.isAccountExists()) {
                    Log.error(sender, "Tento ucet jiz existuje");
                    return false;
                }

                if(account.createAccount() == null)
                    Log.error(sender, "Pri vytvareni nastala chyba");
                else Log.log(sender, "Ucet byl vytvoren!");
                return true;
            }
        }

        sendHelp(sender);
        return false;
    }

    public void sendHelp(CommandSender sender) {
        Log.log(sender,"/banka help - Ukaze napovedu");
        Log.log(sender,"/banka login <jmeno> <heslo> - Prihlasi k uctu");
        Log.log(sender,"/banka logout - Odhlasi z uctu");
        Log.log(sender,"/banka create <jmeno> <heslo> <typUctu> - Vytvori ucet");
        Log.log(sender,"/banka remove <jmeno> <heslo> - Zrusi ucet");
        Log.log(sender,"/banka remove - Zrusi aktualni ucet");
        Log.log(sender,"/banka vyber <castka> - Vyber hotovosti z uctu");
        Log.log(sender,"/banka vklad <castka> - Vlozi hotovost na ucet");
        Log.log(sender,"/banka prevod <ucet> <castka> - Prevod penez na druhy ucet");
    }
}
