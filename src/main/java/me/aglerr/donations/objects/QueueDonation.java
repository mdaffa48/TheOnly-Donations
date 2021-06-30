package me.aglerr.donations.objects;

import me.aglerr.donations.utils.Events;
import me.aglerr.donations.utils.Utils;
import me.aglerr.lazylibs.libs.Executor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        Executor.async(() -> Utils.broadcastDonation(this));
        Executor.sync(() -> Events.playAllEvents(this.getPlayer()));
    }
}
