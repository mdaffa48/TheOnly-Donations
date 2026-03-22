package me.aglerr.donations.managers;

import com.muhammaddaffa.mdlib.utils.Common;
import me.aglerr.donations.ConfigValue;
import me.aglerr.donations.DonationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import org.bukkit.scheduler.BukkitRunnable;

public class DonationBossBar {

    private static BossBar bossBar;

    public static void runnableBossBar() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!ConfigValue.PROGRESS_BOSSBAR_ENABLED) {
                    resetBossBar();
                    return;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    checkAllPlayer(player);
                    getBossBar(player);
                    updateBossBar();
                }
            }
        }.runTaskTimer(DonationPlugin.getInstance(), 0L, 20L);
    }

    private static void getBossBar(Player player) {
        if (bossBar == null) {

            BarColor barColor = getBarColor(ConfigValue.PROGRESS_BOSSBAR_COLOR);
            BarStyle barStyle = getBarStyle(ConfigValue.PROGRESS_BOSSBAR_STYLE);

            String title = Common.color(ConfigValue.PROGRESS_BOSSBAR_TITLE
                    .replace("{current}", DonationGoal.getCurrentDonation())
                    .replace("{goal}", DonationGoal.getDonationGoal())
                    .replace("{percentage}", DonationGoal.getDonationPercentage() + "%"));

            bossBar = Bukkit.createBossBar(title, barColor, barStyle);
        }

        bossBar.addPlayer(player);

        updateBossBar();
    }

    private static void checkAllPlayer(Player player) {
        if (bossBar == null) return;
        if (!bossBar.getPlayers().contains(player)) {
            bossBar.addPlayer(player);
        }
    }

    private static void updateBossBar() {
        if (bossBar == null) return;

        String title = ConfigValue.PROGRESS_BOSSBAR_TITLE;
        bossBar.setTitle(Common.color(title
                .replace("{current}", DonationGoal.getCurrentDonation())
                .replace("{goal}", DonationGoal.getDonationGoal())
                .replace("{percentage}", DonationGoal.getDonationPercentage() + "%")));
        try {
            // Get percentage from getDonationPercentage(), remove commas, and convert to double
            double percentage = Double.parseDouble(DonationGoal.getDonationPercentage());

            // Convert percentage to a range between 0.0 and 1.0
            double progress = Math.max(0.0, Math.min(1.0, percentage / 100.0));

            bossBar.setProgress(progress);
        } catch (NumberFormatException e) {
            Bukkit.getLogger().warning("[TheOnly-Donations] Failed to parse donation percentage! Check config formatting.");
            e.printStackTrace();
        }
    }

    private static BarColor getBarColor(String colorName) {
        try {
            return BarColor.valueOf(colorName);
        } catch (IllegalArgumentException e) {
            return BarColor.YELLOW;
        }
    }

    private static BarStyle getBarStyle(String styleName) {
        try {
            return BarStyle.valueOf(styleName);
        } catch (IllegalArgumentException e) {
            return BarStyle.SEGMENTED_10;
        }
    }

    public static void resetBossBar() {
        if (bossBar != null) {
            bossBar.removeAll();
            bossBar = null;
        }
    }

    public static BossBar getBossBar() {
        return bossBar;
    }
}
