package me.aglerr.donations.api;

import me.aglerr.donations.managers.DonationGoal;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DonationExpansion extends PlaceholderExpansion {

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     *
     * @return The name of the author as a String.
     */
    @NotNull
    @Override
    public String getAuthor(){
        return "aglerr";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>The identifier has to be lowercase and can't contain _ or %
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @NotNull
    @Override
    public String getIdentifier(){
        return "donations";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convienience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @NotNull
    @Override
    public String getVersion(){
        return "1.0.0";
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier){

        if(player == null){
            return "";
        }

        // %donations_donation_current%
        if(identifier.equalsIgnoreCase("donation_current")) {
            return DonationGoal.getCurrentDonation();
        }

        // %donations__donation_goal%
        if(identifier.equalsIgnoreCase("donation_goal")){
            return DonationGoal.getDonationGoal();
        }

        // %donations_donation_percentage%
        if(identifier.equalsIgnoreCase("donation_percentage")){
            return DonationGoal.getDonationPercentage();
        }

        // %donations_donation_progressbar%
        if(identifier.equalsIgnoreCase("donation_progressbar")) {
            return DonationGoal.getProgressBar();
        }

        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided
        return null;
    }

}
