package uk.co.arcanegames.AutoUBL.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import center.uhc.core.commons.Message;
import center.uhc.core.commons.center.Centered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.co.arcanegames.AutoUBL.AutoUBL;
import uk.co.arcanegames.AutoUBL.commands.ubl.*;

/**
 * The base for all UBL commands
 *
 * @author XHawk87
 */
public class UBLCommand implements CommandExecutor {

    /**
     * All subcommands of the UBL command, stored by their name
     */
    private Map<String, IUBLCommand> subCommands = new HashMap<>();

    private AutoUBL plugin;

    public UBLCommand(AutoUBL plugin) {
        this.plugin = plugin;
        subCommands.put("exempt", new ExemptCommand(plugin));
        subCommands.put("unexempt", new UnexemptCommand(plugin));
        subCommands.put("update", new UpdateCommand(plugin));
        subCommands.put("reload", new ReloadCommand(plugin));
        subCommands.put("toggle", new ToggleCommand(plugin));
        subCommands.put("status", new StatusCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // It is assumed that entering the menu command without parameters is an
        // attempt to get information about it. So let's give it to them.
        if (args.length == 0) {
            boolean sent = false;
            sender.sendMessage(Message.formatSystem(ChatColor.RED, "Error", "Correct Usage:"));
            for (IUBLCommand menuCommand : subCommands.values()) {
                String permission = menuCommand.getPermission();
                if (permission != null && sender.hasPermission(permission)) {
                    sent = true;
                    sender.sendMessage("" + ChatColor.RED + menuCommand.getUsage());
                }
            }

            if (!sent) {
                sender.sendMessage(Centered.create("§8»§m--------------------§r§8«"));
                sender.sendMessage(Centered.create("§c§lAuto UBL §8(§7" + AutoUBL.version + "§8)"));
                sender.sendMessage(Centered.create("A fixed version of the AutoUBL plugin"));
                sender.sendMessage(Centered.create("It broke our server, so we redid it ourselves"));
                sender.sendMessage(" ");
                sender.sendMessage(Centered.create("UBL Status: " + (plugin.isUblEnabled() ? "§aEnabled" : "§cDisabled")));
                sender.sendMessage(" ");
                sender.sendMessage(Centered.create("§aCreated by XHawk87, edited by Gabrlella"));
                sender.sendMessage(Centered.create("§8»§m--------------------§r§8«"));
            }
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        IUBLCommand ublCommand = subCommands.get(subCommandName);
        if (ublCommand == null) {
            return false; // They mistyped or entered an invalid subcommand
        }
        // Handle the permissions check
        String permission = ublCommand.getPermission();
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage(Message.formatSystem(ChatColor.RED, "Error", "No permission."));
            return true;
        }
        // Remove the sub-command from the args list and pass along the rest
        if (!ublCommand.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length))) {
            // A sub-command returning false should display the usage information for that sub-command
            sender.sendMessage(Message.formatSystem(ChatColor.RED, "Error", "Correct Usage: " + ublCommand.getUsage()));
        }
        return true;
    }
}
