package me.aglerr.donations.objects;

import com.muhammaddaffa.mdlib.utils.Executor;
import me.aglerr.donations.managers.DonationGoal;
import me.aglerr.donations.utils.Events;
import me.aglerr.donations.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class QueueDonation {

    @NotNull private final OfflinePlayer player;
    @NotNull private final Product product;

    public QueueDonation(@NotNull OfflinePlayer player, @NotNull Product product) {
        this.player = player;
        this.product = product;
    }

    @NotNull
    public OfflinePlayer getPlayer() {
        return player;
    }

    @NotNull
    public Product getProduct() {
        return product;
    }

    public void announceDonation(){
        Executor.sync(() -> Events.playAllEvents(this.getPlayer()));
        Executor.sync(() -> DonationGoal.handleDonation(this.getProduct()));
        Executor.async(() -> Utils.broadcastDonation(this));
    }
}
