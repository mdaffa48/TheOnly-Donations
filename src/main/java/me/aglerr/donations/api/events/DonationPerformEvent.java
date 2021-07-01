package me.aglerr.donations.api.events;

import me.aglerr.donations.DonationPlugin;
import me.aglerr.donations.managers.QueueManager;
import me.aglerr.donations.objects.QueueDonation;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DonationPerformEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;
    @NotNull private final QueueDonation donation;

    public DonationPerformEvent(@NotNull QueueDonation donation){
        this.donation = donation;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * QueueDonation is an object
     *
     * @return - A {@link me.aglerr.donations.objects.QueueDonation} object
     */
    @NotNull
    public QueueDonation getDonation() {
        return donation;
    }

    /**
     * Remove the donation from the queue, means the donation
     * will not be broadcasted
     *
     * @return - A boolean to check if the donations successfully removed
     */
    public boolean removeFromQueue(){
        // Get the queue manager
        QueueManager queueManager = DonationPlugin.getInstance().getQueueManager();
        // Remove the donation from the queue
        return queueManager.removeDonationFromQueue(donation);
    }
}
