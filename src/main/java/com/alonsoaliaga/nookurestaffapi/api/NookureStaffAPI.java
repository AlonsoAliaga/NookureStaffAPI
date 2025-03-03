package com.alonsoaliaga.nookurestaffapi.api;

import com.alonsoaliaga.nookurestaffapi.NookureStaffAPIPlugin;
import com.nookure.staff.api.StaffPlayerWrapper;
import org.bukkit.entity.Player;

import java.util.Optional;

public class NookureStaffAPI {
    static NookureStaffAPIPlugin plugin = NookureStaffAPIPlugin.getInstance();
    public static boolean isStaffChatAsDefault(Player player) {
        if(!plugin.isInitialized()) {
            return false;
        }
        Optional<StaffPlayerWrapper> optionalStaffPlayerWrapper = plugin.getPlayerWrapperManager().getStaffPlayer(player.getUniqueId());
        if (!optionalStaffPlayerWrapper.isPresent()) return false;
        StaffPlayerWrapper staffPlayerWrapper = optionalStaffPlayerWrapper.get();
        return staffPlayerWrapper.isStaffChatAsDefault();
    }
    public static boolean isInStaffMode(Player player) {
        if(!plugin.isInitialized()) {
            return false;
        }
        Optional<StaffPlayerWrapper> optionalStaffPlayerWrapper = plugin.getPlayerWrapperManager().getStaffPlayer(player.getUniqueId());
        if (!optionalStaffPlayerWrapper.isPresent()) return false;
        StaffPlayerWrapper staffPlayerWrapper = optionalStaffPlayerWrapper.get();
        return staffPlayerWrapper.isInStaffMode();
    }
    public static boolean toggleStaffMode(Player player, boolean state) {
        if(!plugin.isInitialized()) {
            return false;
        }
        Optional<StaffPlayerWrapper> optionalStaffPlayerWrapper = getStaffPlayer(player);
        if (!optionalStaffPlayerWrapper.isPresent()) return false;
        StaffPlayerWrapper staffPlayerWrapper = optionalStaffPlayerWrapper.get();
        staffPlayerWrapper.toggleStaffMode(state);
        return true;
    }
    public static boolean isInVanish(Player player) {
        if(!plugin.isInitialized()) {
            return false;
        }
        Optional<StaffPlayerWrapper> optionalStaffPlayerWrapper = getStaffPlayer(player);
        if (!optionalStaffPlayerWrapper.isPresent()) return false;
        StaffPlayerWrapper staffPlayerWrapper = optionalStaffPlayerWrapper.get();
        return staffPlayerWrapper.isInVanish();
    }
    public static Optional<StaffPlayerWrapper> getStaffPlayer(Player player) {
        if(!plugin.isInitialized()) {
            return Optional.empty();
        }
        return plugin.getPlayerWrapperManager().getStaffPlayer(player.getUniqueId());
    }
}