package me.aglerr.donations.managers;

import me.aglerr.donations.objects.Product;
import me.aglerr.donations.objects.QueueDonation;
import me.aglerr.lazylibs.libs.Executor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QueueManager {

    private boolean IS_QUEUEING = false;

    private final List<QueueDonation> queueDonations = new ArrayList<>();

    public void addQueue(OfflinePlayer player, @Nullable Product product){
        this.queueDonations.add(new QueueDonation(player, product));
    }

    public void startAnnounceTask(){
        Executor.asyncTimer(0L, 20L, () -> {
            // Return if still queueing
            if(IS_QUEUEING) return;
            // Return if the queue is empty
            if(this.queueDonations.isEmpty()) return;
            // If the the plugin isn't queueing and queue donations isn't empty
            // Set the IS_QUEUEING to true
            IS_QUEUEING = true;
            // Get the queue donations first entry
            QueueDonation donation = this.queueDonations.get(0);
            // Announce the donation message
            donation.announceDonation();
            // Remove the donation from the list
            this.queueDonations.remove(donation);
            // Set the is queueing back to false
            IS_QUEUEING = false;
        });
    }


}
