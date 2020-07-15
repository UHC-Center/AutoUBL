package uk.co.arcanegames.AutoUBL.commands.ubl;

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
        return "/ubl toggle - Enabled and Disable the UBL";
    }

    @Override
    public String getPermission() {
        return "autoubl.commands.toggle";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (plugin.isUblEnabled()) {
            plugin.setUblEnabled(false);
            sender.sendMessage("Disabled the UBL.");
        } else {
            plugin.setUblEnabled(true);
            sender.sendMessage("Enabled the UBL. Reloading config.");
            plugin.updateBanlist();
        }
        return true;
    }
}
