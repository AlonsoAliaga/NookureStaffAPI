package com.alonsoaliaga.nookurestaffapi;

import com.alonsoaliaga.nookurestaffapi.metrics.Metrics;
import com.alonsoaliaga.nookurestaffapi.utils.LocalUtils;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.manager.PlayerWrapperManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class NookureStaffAPIPlugin extends JavaPlugin {
    private final long delayIfFailInTicks = 5 * 20L;
    private boolean initialized = false;
    private static NookureStaffAPIPlugin instance;
    private PlayerWrapperManager<Player> playerWrapperManager = null;
    @Override
    public void onEnable() {
        instance = this;
        if(Bukkit.getPluginManager().getPlugin("NookureStaff") == null) {
            LocalUtils.loge("===============================================================================");
            LocalUtils.loge("NookureStaff not found! This plugin was made to expose NookureStaff API");
            LocalUtils.loge("to be able to be used by other plugins without worrying too much.");
            LocalUtils.loge("Since NookureStaff is not installed, this plugin is not necessary.");
            LocalUtils.loge("Disabling NookureStaffAPI this time..");
            LocalUtils.loge("===============================================================================");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        Metrics metrics = new Metrics(this, 24958);
        Bukkit.getScheduler().runTaskLater(this,()-> {
            metrics.addCustomChart(new Metrics.AdvancedPie("plugins_from_alonsoaliaga", () -> {
                Map<String, Integer> pluginsFromAlonsoAliaga = new HashMap<>();
                for (Plugin registeredPlugin : getServer().getPluginManager().getPlugins()) {
                    if (registeredPlugin.getDescription().getAuthors().contains("AlonsoAliaga") && !registeredPlugin.getName().equalsIgnoreCase(getDescription().getName()))
                        pluginsFromAlonsoAliaga.put(registeredPlugin.getName(), 1);
                }
                return pluginsFromAlonsoAliaga;
            }));
        },60 * 20);
        if(!Bukkit.getPluginManager().isPluginEnabled("NookureStaff")) {
            LocalUtils.logp("===============================================================================");
            LocalUtils.logp("NookureStaff found! But somehow NookureStaff is not enabled?");
            LocalUtils.logp("Scheduling to load plugin in "+delayIfFailInTicks+" ticks..");
            LocalUtils.logp("===============================================================================");
            Bukkit.getScheduler().runTaskLater(this,()->{
                LocalUtils.logp("Scheduled start. Initializing stuff..");
                loadStuff();
            },delayIfFailInTicks);
        }else{
            loadStuff();
        }
    }
    private void loadStuff() {
        if(playerWrapperManager == null) {
            NookureStaff nookureStaff = (NookureStaff) Bukkit.getPluginManager().getPlugin("NookureStaff");
            if (nookureStaff != null) {
                LocalUtils.logp("Initializing PlayerWrapperManager<Player> value with TypeLiteral..");
                try{
                    TypeLiteral<PlayerWrapperManager<Player>> typeLiteral = new TypeLiteral<PlayerWrapperManager<Player>>() {};
                    playerWrapperManager = nookureStaff.getInjector().getInstance(Key.get(typeLiteral));
                }catch (Throwable e) {
                    LocalUtils.logp("Error initializing PlayerWrapperManager<Player> value with TypeLiteral. Trying 2nd method..");
                    try{
                        playerWrapperManager = nookureStaff.getInjector().getInstance(PlayerWrapperManager.class);
                    }catch (Throwable e2) {
                        LocalUtils.logp("Error initializing PlayerWrapperManager<Player> value with 2nd method..");
                        LocalUtils.logp("Plugin cannot be used. Please report this to our developer here in");
                        LocalUtils.logp("our discord server on https://alonsoaliaga.com/discord");
                    }
                }
                if (playerWrapperManager == null) {
                    LocalUtils.logp("Error initializing PlayerWrapperManager<Player> value..");
                } else {
                    LocalUtils.logp("Successfully initialized PlayerWrapperManager<Player> value!");
                    initialized = true;
                }
            }
        }
    }
    public PlayerWrapperManager<Player> getPlayerWrapperManager() {
        return playerWrapperManager;
    }
    public boolean isInitialized() {
        return initialized;
    }
    public static NookureStaffAPIPlugin getInstance() {
        return instance;
    }
    @Override
    public void onDisable() {
        LocalUtils.loge(" ");
        LocalUtils.loge("NookureStaffAPI has been disabled!");
        LocalUtils.loge("Thank you for using my plugin!");
        LocalUtils.loge(" ");
    }
}