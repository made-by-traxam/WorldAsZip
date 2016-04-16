package de.tr808axm.worldaszip.command;

import de.tr808axm.worldaszip.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Maximilian on 16.04.2016.
 */
public class WorldAsZipCommandExecutor implements CommandExecutor {
    private Main plugin;

    public WorldAsZipCommandExecutor(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp() && commandSender instanceof Player) {
            commandSender.sendMessage("Building zip...");
            if(plugin.build(((Player) commandSender).getLocation().getWorld())) {
                commandSender.sendMessage("Successfully build zip!");
            } else {
                commandSender.sendMessage("Could not build zip.");
            }
        } else {
            commandSender.sendMessage("You don't have permission!");
        }
        return true;
    }
}
