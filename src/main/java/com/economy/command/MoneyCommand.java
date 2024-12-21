package com.economy.command;

import com.economy.DatabaseManager;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {
    private final DatabaseManager databaseManager;

    public MoneyCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
         Player player = (Player) commandSender;
        MongoCollection<Document> collection = databaseManager.getCollection("players");

        if (strings.length == 0) {
            Document docPlayer = collection.find(new Document("uuid", player.getUniqueId().toString())).first();
            if (docPlayer != null) {
                int money = docPlayer.getInteger("money", 0);
                player.sendMessage("You have " + money + " money.");
            } else {
                player.sendMessage("You don't have any money.");
            }
            return true;
        }

        if (strings.length == 2) {
            String action = strings[0];
            int amount;
            try {
                amount = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("Enter a valid amount!");
                return true;
            }

            Document docPlayer = collection.find(new Document("uuid", player.getUniqueId().toString())).first();

            if (docPlayer == null) {
                docPlayer = new Document("uuid", player.getUniqueId().toString()).append("money", 0);
                collection.insertOne(docPlayer);
            }

            int money = docPlayer.getInteger("money");

            if (action.equalsIgnoreCase("add")) {
                money += amount;
                collection.updateOne(new Document("uuid", player.getUniqueId().toString()),
                        new Document("$set", new Document("money", money)));
                player.sendMessage("+" + amount + " money. New amount: " + money);
            } else if (action.equalsIgnoreCase("remove")) {
                money -= amount;
                if (money < 0) money = 0;
                collection.updateOne(new Document("uuid", player.getUniqueId().toString()),
                        new Document("$set", new Document("money", money)));
                player.sendMessage("-" + amount + " money. New amount: " + money);
            } else {
                player.sendMessage("Invalid command action. Use 'add' or 'remove'!");
            }
            return true;
        }

        player.sendMessage("Usage : /money [add|remove] [amount]");
        return true;
    }
}
