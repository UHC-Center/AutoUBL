package uk.co.arcanegames.AutoUBL.commands.ubl;

import center.uhc.core.commons.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.co.arcanegames.AutoUBL.AutoUBL;
import uk.co.arcanegames.AutoUBL.commands.IUBLCommand;

public class ToggleCommand implements IUBLCommand {

    private AutoUBL plugin;

    public ToggleCommand(AutoUBL plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getUsage() {
        return "/ubl toggle";
    }

    @Override
    public String getPermission() {
        return "autoubl.commands.toggle";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (plugin.isUblEnabled()) {
            plugin.setUblEnabled(false);
            sender.sendMessage(Message.formatSystem(ChatColor.RED, "AutoUBL", "The UBL banlist has been disabled."));
        } else {
            plugin.setUblEnabled(true);
            sender.sendMessage(Message.formatSystem(ChatColor.GREEN, "AutoUBL", "The UBL banlist has been enabled."));
            plugin.updateBanlist();
        }
        return true;
    }
}
