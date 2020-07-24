package uk.co.arcanegames.AutoUBL.commands.ubl;

import center.uhc.core.commons.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.co.arcanegames.AutoUBL.AutoUBL;
import uk.co.arcanegames.AutoUBL.commands.IUBLCommand;

public class StatusCommand implements IUBLCommand {

    private AutoUBL plugin;

    public StatusCommand(AutoUBL plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getUsage() {
        return "/ubl status";
    }

    @Override
    public String getPermission() {
        return "autoubl.commands.status";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        sender.sendMessage(Message.formatSystem(ChatColor.YELLOW, "AutoUBL", "UBL Status: " + (plugin.isUblEnabled() ? "§aEnabled" : "§cDisabled")));
        return true;
    }
}
