package uk.co.arcanegames.AutoUBL.listeners;

import java.util.UUID;
import java.util.logging.Level;

import center.uhc.core.commons.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import uk.co.arcanegames.AutoUBL.AutoUBL;
import uk.co.arcanegames.AutoUBL.utils.UUIDFetcher;

/**
 * This listens for players attempting to connect to the server and checks
 * them against the ban-list asynchronously
 *
 * Login attempts when the ban list is not ready will be delayed until it is
 * ready.
 *
 * @author XHawk87
 */
public class LoginListener implements Listener {

    public AutoUBL plugin;

    public void registerEvents(AutoUBL plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        try {
            if (!plugin.isUblEnabled()) {
                event.allow();
                return;
            }

            if (!plugin.isReady()) {
                // AsyncPreLogin always gets fired on an User Authentifactor thread,
                // using Thread.sleep() won't kill the server.
                boolean proceed = waitUntilReady();
                if (!proceed) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                            Message.formatSystem(ChatColor.RED, "AutoUBL", "One moment please, the UBL isn't ready.")
                    );
                    return;
                }
            }
            if (plugin.isUUIDReady()) {
                try {
                    if (plugin.isBanned(event.getName(), event.getUniqueId())) {
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                                ChatColor.translateAlternateColorCodes('&', "&cThe UBL is enabled and you are on it!")
                        );
                    }
                    return;
                } catch (NoSuchMethodError ex) { // In case the server does not yet have AsyncPlayerPreLoginEvent.getUniqueId() method
                    plugin.getLogger().warning("This server is outdated so we are forced to fall back on a slow inefficient method fetcher player UUIDs from Mojangs API server. Please consider updating to at least the latest CB 1.7.5-R0.1-SNAPSHOT or later");
                    try {
                        String ign = event.getName();
                        UUID uuid = UUIDFetcher.getUUIDOf(ign);
                        if (plugin.isBanned(ign, uuid)) {
                            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, plugin.getBanMessage(uuid));
                        }
                    } catch (Exception ex1) { // The UUID could not be located, server down or not a real account
                        plugin.getLogger().log(Level.WARNING, "Failed to lookup UUID of " + event.getName(), ex1);
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Message.formatSystem(ChatColor.RED, "AutoUBL", "Authentication Error. Please contact an admin§r\n\n§cError: " + ex1.getLocalizedMessage()));
                    }
                    return;
                }
            }
            if (plugin.isBanned(event.getName())) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                        ChatColor.translateAlternateColorCodes('&', "&cThe UBL is enabled and you are on it!")
                );
            }
        } catch (Exception e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cError authenticating you.§r\n\n§cYou are probably UBL'ed. that or gabriella is a fucking idiot and screwed something up.§r\n\n§ceither way go moan to her. she can fix it. (sincerely, gabriella)");
        }
    }

    /**
     * Wait until the plugin is ready.
     *
     * @return True if operation should continue as normal, false if the
     * plugin was disabled mid-wait.
     */
    private boolean waitUntilReady() {
        while (!plugin.isReady()) {
            if (!plugin.isEnabled()) {
                return false;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        }
        return true;
    }
}
