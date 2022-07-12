package me.aglerr.donations.managers;

import me.aglerr.donations.api.events.DonationPerformEvent;
import me.aglerr.donations.objects.Product;
import me.aglerr.donations.objects.QueueDonation;
import me.aglerr.mclibs.libs.Executor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class QueueManager {

    private boolean IS_QUEUEING = false;

    public QueueManager(){
        startAnnounceTask();
    }

    private final List<QueueDonation> queueDonations = new ArrayList<>();

    public void addQueue(OfflinePlayer player, @NotNull Product product){
        this.queueDonations.add(new QueueDonation(player, product));
    }

    public void addQueue(QueueDonation donation){
        this.queueDonations.add(donation);
    }

    public boolean removeDonationFromQueue(QueueDonation donation){
        return this.queueDonations.remove(donation);
    }

    protected void startAnnounceTask(){
        Executor.asyncTimer(0L, 20L, () -> {
            // Return if still queueing
            if(IS_QUEUEING) return;
            // Return if the queue is empty
            if(this.queueDonations.isEmpty()) return;
            // If the the plugin isn't queueing and queue donations isn't empty
            // Get the queue donations first entry
            QueueDonation donation = this.queueDonations.get(0);
            // Create the custom event
            DonationPerformEvent event = new DonationPerformEvent(donation);
            // Call the event in sync thread
            Executor.sync(() -> Bukkit.getPluginManager().callEvent(event));
            // Stop the code if the event is cancelled
            if(event.isCancelled())
                return;
            // Set the IS_QUEUEING to true
            IS_QUEUEING = true;
            // Announce the donation message
            donation.announceDonation();
            // Remove the donation from the list
            this.queueDonations.remove(donation);
            // Set the is queueing back to false
            IS_QUEUEING = false;
        });
    }


}
