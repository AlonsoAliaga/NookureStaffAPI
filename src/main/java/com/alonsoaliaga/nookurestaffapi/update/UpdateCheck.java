package com.alonsoaliaga.nookurestaffapi.update;

import com.alonsoaliaga.nookurestaffapi.utils.JsonUtils;
import com.alonsoaliaga.nookurestaffapi.utils.LocalUtils;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateCheck {
    private JavaPlugin plugin;
    private String resourceID;
    private boolean availableOnSpigot;
    private int updateCheckCounter = 0;
    private UpdateFound updateFound = null;
    public UpdateCheck(JavaPlugin plugin, String resourceID) {
        this.plugin = plugin;
        this.resourceID = resourceID;
        this.availableOnSpigot = resourceID.matches("[0-9]+");
        startChecking();
        new UpdateJoinListener(plugin,"nookurestaffapi.update");
    }
    private void startChecking() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!plugin.isEnabled()){
                    cancel();
                    return;
                }
                checkUpdate();
            }
        }.runTaskTimerAsynchronously(plugin, ThreadLocalRandom.current().nextInt(10) * 10,1000 * 60 * 60 * 24);
    }
    private void checkUpdate() {
        if(!availableOnSpigot) { //GitHub Name
            String url = "https://api.github.com/repos/AlonsoAliaga/"+resourceID+"/releases/latest";
            Bukkit.getScheduler().runTaskAsynchronously(plugin,()-> {
                try (InputStream inputStream = new URL(url).openStream(); Scanner scanner = new Scanner(inputStream)) {
                    if(scanner.hasNext()) {
                        String toParse = scanner.nextLine();
                        JsonObject bodyObject = JsonUtils.parseObject(toParse);
                        if(bodyObject.has("tag_name")) {
                            LocalUtils.logp("Checking for updates...");
                            String tagVersion = bodyObject.get("tag_name").getAsString();
                            Pattern pattern = Pattern.compile("^(v?)[0-9]+(\\.[0-9]+)*",Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(tagVersion);
                            if(matcher.find()) {
                                String cleanNewVersion = tagVersion.toLowerCase(Locale.ROOT).startsWith("v") ? tagVersion.substring(1) : tagVersion;
                                VersionComparator versionComparator = new VersionComparator(plugin.getDescription().getVersion(),cleanNewVersion);
                                if(versionComparator.getUpdateFound() == null) {
                                    updateFound = null;
                                    if (updateCheckCounter % 3 == 0) {
                                        LocalUtils.logp("&ePlugin up-to-date! You have the latest version!");
                                    }
                                }else{
                                    updateFound = versionComparator.getUpdateFound();
                                    LocalUtils.logp(String.format("&aNew version available: %s", cleanNewVersion));
                                    LocalUtils.logp("&aPlease download the latest version to get support!");
                                    LocalUtils.logp("&aDownload: "+updateFound.getDownloadLink());
                                }
                            }
                        }
                        updateCheckCounter++;
                    }
                } catch (Throwable ignored) {}
            });
        }//Other
    }
    public class VersionComparator {
        private final String installedVersion;
        private final String foundVersion;
        private UpdateFound updateFound = null;
        public VersionComparator(String installedVersion, String foundVersion) {
            this.installedVersion = installedVersion;
            this.foundVersion = foundVersion;
            compare();
        }
        Pattern pattern = Pattern.compile("^[0-9]+(\\.[0-9]+)*");
        private void compare() {
            Matcher installed = pattern.matcher(installedVersion);
            Matcher found = pattern.matcher(foundVersion);
            if(installed.find() && found.find()) {
                String iVersion = installed.group();
                String fVersion = found.group();
                int maxDigits = getMaxDigits(iVersion, fVersion);
                int maxParts = Math.max(iVersion.split("\\.").length, fVersion.split("\\.").length);
                long installedNumber = versionToNumber(installedVersion, maxDigits, maxParts);
                long foundNumber = versionToNumber(foundVersion, maxDigits, maxParts);
                if(installedNumber == foundNumber) {
                    if(isDevBuild(installedVersion)) {
                        updateFound = new UpdateFound(foundVersion);
                    }
                }else if(installedNumber < foundNumber) {
                    updateFound = new UpdateFound(foundVersion);
                }
            }
        }
        public UpdateFound getUpdateFound() {
            return updateFound;
        }
        private boolean isDevBuild(String version) {
            return version.contains("-DEV");
        }
        private int getMaxDigits(String version1, String version2) {
            int maxDigits = 0;
            for (String part : version1.split("\\.")) {
                maxDigits = Math.max(maxDigits, part.length());
            }
            for (String part : version2.split("\\.")) {
                maxDigits = Math.max(maxDigits, part.length());
            }
            return maxDigits;
        }
        private long versionToNumber(String version, int maxDigits, int maxParts) {
            String[] parts = version.split("\\.");
            List<String> partsList = new ArrayList<>(Arrays.asList(parts));
            while(partsList.size() < maxParts) {
                partsList.add("0");
                parts = partsList.toArray(new String[0]);
            }
            System.out.println("Original: "+version+" Filled: "+String.join(".", partsList));
            long result = 0;
            for (int i = 0; i < maxParts; i++) {
                int part = 0;
                if(i + 1 != maxParts) {
                    int exponent = (maxParts - i - 1) * maxDigits;
                    part = Integer.parseInt(parts[i]);
                    part = part * (int) (Math.pow(10,exponent));
                }
                result = result  + part;
            }
            return result;
        }
    }
    public class UpdateFound {
        private String newVersion;
        public UpdateFound(String newVersion) {
            this.newVersion = newVersion;
        }
        public String getNewVersion(){
            return newVersion;
        }
        public String getDownloadLink(){
            return availableOnSpigot ? (String.format("https://www.spigotmc.org/resources/%s/history",resourceID)) : ("https://github.com/AlonsoAliaga/"+resourceID+"/releases/");
        }
    }
    public class UpdateJoinListener implements Listener {
        private JavaPlugin plugin;
        private String notifyPermission;
        private String notificationMessage = LocalUtils.colorize(
                "&9[NookureStaffAPI] &eNew update found! You are using version {CURRENT}.{NEWLINE}&eDownload version {NEW} here &c{LINK}"
        );
        public UpdateJoinListener(JavaPlugin plugin, @Nullable String notifyPermission) {
            this.plugin = plugin;
            this.notifyPermission = (notifyPermission == null || notifyPermission.equalsIgnoreCase("none")) ? null : notifyPermission;
            plugin.getServer().getPluginManager().registerEvents(this,plugin);
        }
        @EventHandler
        public void onJoinUpdate(PlayerJoinEvent e){
            if(updateFound != null){
                if(e.getPlayer().isOp() || (notifyPermission != null && e.getPlayer().hasPermission(notifyPermission))){
                    e.getPlayer().sendMessage(notificationMessage
                            .replace("{NEWLINE}","\n")
                            .replace("{CURRENT}",plugin.getDescription().getVersion())
                            .replace("{NEW}",updateFound.getNewVersion())
                            .replace("{LINK}",updateFound.getDownloadLink()));
                }
            }
        }
    }
}