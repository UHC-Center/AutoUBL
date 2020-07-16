package uk.co.arcanegames.AutoUBL.commands.ubl;

import center.uhc.core.commons.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.co.arcanegames.AutoUBL.AutoUBL;
import uk.co.arcanegames.AutoUBL.commands.IUBLCommand;

/**
 * This command attempts to update the ban-list
 *
 * @author XHawk87
 */
public class UpdateCommand implements IUBLCommand {

    private AutoUBL plugin;

    public UpdateCommand(AutoUBL plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getUsage() {
        return "/ubl update";
    }

    @Override
    public String getPermission() {
        return "autoubl.commands.update";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0) {
            return false; // Expected no args
        }

        plugin.updateBanlist();
        sender.sendMessage(Message.formatSystem(ChatColor.GREEN, "AutoUBL", "Updating the UBL banlist!"));
        return true;
    }
}
