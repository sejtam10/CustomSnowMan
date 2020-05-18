package dev.sejtam.Banka.Commands;

import dev.sejtam.Banka.Utils.BankAccount;
import dev.sejtam.Banka.Utils.Enums.AccountType;
import dev.sejtam.Banka.Utils.Enums.ActionType;
import dev.sejtam.Banka.Utils.Log;
import dev.sejtam.Banka.Utils.MySQL;
import dev.sejtam.Banka.Utils.Pagination;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class Bank implements CommandExecutor, Listener {

    Map<String, BankAccount> accounts = new HashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(accounts.get(event.getPlayer().getName()) != null)
            accounts.remove(event.getPlayer().getName());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            sendHelp(sender);
            return true;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?") ) {
                sendHelp(sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("logout")) {
                if(accounts.containsKey(sender.getName())) {
                    accounts.remove(sender.getName());
                    Log.log(sender, "Byl jsi odhlasen");
                } else Log.error(sender, "Nejsi prihlasen");
                return true;
            }

            if(args[0].equalsIgnoreCase("remove")) {
                if(accounts.containsKey(sender.getName())) {
                    if(accounts.get(sender.getName()).removeAccount())
                        Log.log(sender, "Ucet byl smazan a byl jsi odhlasen");
                    else { Log.error("Nastala chyba"); return false; }

                    accounts.remove(sender.getName());
                    return true;
                } else Log.error(sender, "Nejsi prihlasen");
                return true;
            }

            if(args[0].equalsIgnoreCase("status")) {
                if(accounts.containsKey(sender.getName())) {
                    BankAccount account = accounts.get(sender.getName());

                    Log.log(sender,"Nazev uctu: §c" + account.getName());
                    Log.log(sender,"Typ uctu: §c" + account.getAccountType());
                    Log.log(sender,"Zustatek: §c" + account.getMoney() + " §7Kc");
                } else Log.error(sender, "Nejsi prihlasen");
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
                    Log.error(sender, "Castka nemuze byt menci jak &c1");
                    return false;
                }

                BankAccount account = accounts.get(sender.getName());
                int vyber = account.action(ActionType.vyber, cislo);
                if(vyber == -1)
                    Log.error(sender, "Nemas dostatek penez nebo se vyber nepovedl");
                else Log.log(sender, "Vyber &c" + vyber + " Kc &7probehl v poradku §8(§c" + (cislo - vyber) + " Kc §7poplatek§8) §8- §7Zustatek: §c" + account.getMoney() + " Kc");
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
                    Log.error(sender, "Castka nemuze byt menci jak &c1");
                    return false;
                }

                BankAccount account = accounts.get(sender.getName());
                int vklad = account.action(ActionType.vklad, cislo);
                if(vklad == -1)
                    Log.error(sender, "Nemas dostatek penez nebo se vklad nepovedl");
                else Log.log(sender, "Vklad &c" + vklad + " Kc &7probehl v poradku §8(§c" + (cislo - vklad) + " Kc §7poplatek§8) §8- §7Zustatek: §c" + account.getMoney() + " Kc");
                return true;
            }
        }

        if(args.length >= 3) {
            if(args[0].equalsIgnoreCase("login")) {
                if(accounts.containsKey(sender.getName())) {
                    Log.error(sender, "Uz jsi prihlasen §8(§c" + accounts.get(sender.getName()).getName() + "§8)");
                    return false;
                }

                for(Map.Entry<String, BankAccount> entry : accounts.entrySet()) {
                    if(entry.getValue().getName().equals(args[1])) {
                        Log.error(sender, "Na tomto ucte je jiz nekdo prihlasen");
                        return false;
                    }
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

                    account.setPlayer(sender.getName());
                    accounts.put(sender.getName(), account.createAccount());
                    Log.log(sender, "Byl jsi prihlasen");
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("remove")) {
                BankAccount account = new BankAccount(args[1], args[2]);
                if(!account.isAccountExists()) {
                    Log.error(sender, "Tento ucet neexistuje");
                    return false;
                }

                if(!account.isPasswordSame()) {
                    Log.error(sender, "Heslo neni spravne");
                    return false;
                }

                accounts.forEach((k, v) -> {
                    if(v.getName().equals(account.getName())) {
                        Player player = Bukkit.getPlayer(k);
                        if(player != null)
                            Log.log(player, "Ucet byl smazan a byl jsi odhlasen");

                        accounts.remove(k);
                    }
                });

                Log.log(sender, "Ucet byl smazan");
            }

            if(args[0].equalsIgnoreCase("prevod")) {
                if(!accounts.containsKey(sender.getName())) {
                    Log.error(sender, "Nejsi prihlasen na zadnem ucte");
                    return false;
                }

                BankAccount account = new BankAccount(args[1]);
                if(!account.isAccountExists()) {
                    Log.error(sender, "Ucet neexistuje");
                    return false;
                }

                int cislo = -1;
                try {
                    cislo = Integer.parseInt(args[2]);
                } catch(NumberFormatException ex) {
                    Log.error(sender, "Castka neni cislo");
                    return false;
                }

                if(cislo <= 0) {
                    Log.error(sender, "Castka nemuze byt mensi jak §c1");
                    return false;
                }

                int prevod = accounts.get(sender.getName()).action(ActionType.prevod, cislo, true);
                if(prevod == -1) {
                    Log.error(sender, "Nemas dostatek penez");
                    return false;
                }

                account.action(ActionType.prevod, cislo, false);
                Log.log(sender, "Prevod &c" + prevod + " Kc &7probehl v poradku §8(§c" + (cislo - prevod) + " Kc §7poplatek§8) §8- §7Zustatek: §c" + account.getMoney() + " Kc");
                return true;
            }

            if(args[0].equalsIgnoreCase("log")) {
                if(args[1].equalsIgnoreCase("hrac") || args[1].equalsIgnoreCase("ucet")) {
                    Pagination<BankAccount.Log> pagination = null;
                    if(args[1].equalsIgnoreCase("hrac"))
                        pagination = MySQL.getPlayerLog(args[2]);
                    else if (args[1].equalsIgnoreCase("ucet"))
                        pagination = new BankAccount(args[2]).getLogs();

                    if(pagination == null) {
                        Log.error(sender, "Hrac nebyl nalezen nebo nema zadny log");
                        return false;
                    }

                    if(pagination.size() == 0) {
                        Log.error(sender, "Hrac nebyl nalezen nebo nema zadny log");
                        return false;
                    }

                    int page = 0;
                    if(args.length > 3) {
                        try {
                            page = Integer.parseInt(args[3]) - 1;
                        } catch (NumberFormatException ex) {
                            Log.error(sender, "Vlozene cislo neni cislo");
                            return true;
                        }
                    }

                    if(page < 0 || pagination.totalPages() <= page) {
                        Log.error(sender, "Maximalni stranka je §c" + (pagination.totalPages()));
                        return true;
                    }

                    Log.sendMessageWP(sender, "         &7-------- &8[&c" + (page + 1) + "&8/&c" + pagination.totalPages() + "&8] &7--------");
                    for(BankAccount.Log log : pagination.getPage(page)) {
                        Log.sendMessageWP(sender, "§8[§c" + log.time.toString() + "§8] &7" + log.type + " §8- §7" + log.money + " Kc");
                    }
                    return true;
                } else {
                    Log.error(sender, "/banka log <hrac/ucet> <jmeno> <stranka>");
                    return false;
                }
            }
        }

        if(args.length >= 4) {
            if(args[0].equalsIgnoreCase("create")) {
                AccountType type;
                try {
                    type = AccountType.valueOf(args[3]);
                } catch (IllegalArgumentException ex) {
                    Log.error(sender, "Typy uctu: klasicky, studentsky");
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
        Log.log(sender,"/banka help - §cUkaze napovedu");
        Log.log(sender,"/banka login <jmeno> <heslo> - §cPrihlasi k uctu");
        Log.log(sender,"/banka logout - §cOdhlasi z uctu");
        Log.log(sender,"/banka status - §cUkaze informace o ucte");
        Log.log(sender,"/banka create <jmeno> <heslo> <typUctu> - §cVytvori ucet");
        Log.log(sender,"/banka remove <jmeno> <heslo> - §cZrusi ucet");
        Log.log(sender,"/banka remove - §cZrusi aktualni ucet");
        Log.log(sender,"/banka vyber <castka> - §cVyber hotovosti z uctu");
        Log.log(sender,"/banka vklad <castka> - §cVlozi hotovost na ucet");
        Log.log(sender,"/banka prevod <ucet> <castka> - §cPrevod penez na druhy ucet");
        Log.log(sender,"/banka log <hrac/ucet> <jmeno> <stranka> - §cVypis logu");
    }
}
